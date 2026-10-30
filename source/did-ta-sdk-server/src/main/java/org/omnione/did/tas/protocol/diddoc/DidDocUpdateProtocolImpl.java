package org.omnione.did.tas.protocol.diddoc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.datamodel.enums.did.ProofType;
import org.omnione.did.common.domain.did.DidDocCryptoHelper;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.DateTimeUtil;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.data.DidAuth;
import org.omnione.did.tas.model.data.SignedDidDoc;
import org.omnione.did.tas.model.domain.User;
import org.omnione.did.tas.model.enums.UserStatus;
import org.omnione.did.tas.model.request.protocol.user.ConfirmUpdateDidDocRequest;
import org.omnione.did.tas.model.request.protocol.user.ProposeUpdateDidDocRequest;
import org.omnione.did.tas.model.request.protocol.user.RequestUpdateDidDocRequest;
import org.omnione.did.tas.model.response.protocol.user.ConfirmUpdateDidDocResponse;
import org.omnione.did.tas.model.response.protocol.user.ProposeUpdateDidDocResponse;
import org.omnione.did.tas.model.response.protocol.user.RequestUpdateDidDocResponse;
import org.omnione.did.tas.provider.CertificateProvider;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.StorageProvider;
import org.omnione.did.tas.provider.TokenProvider;
import org.omnione.did.tas.provider.TransactionProvider;
import org.omnione.did.tas.provider.UserProvider;
import org.omnione.did.tas.provider.WalletProvider;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * P141: DID Document 갱신 프로토콜 구현체.
 *
 * <p>사용자 DID Document를 안전하게 갱신하는 프로토콜을 구현한다.
 * DB 접근과 트랜잭션 관리는 Provider 인터페이스를 통해 서버에 위임한다.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TokenProvider} - 서버 토큰 검증 (Step 4, 5)</li>
 *   <li>{@link UserProvider} - 사용자 조회 및 상태 확인 (Step 1)</li>
 *   <li>{@link DidDocumentProvider} - 현재 DID Document 조회 (Step 4)</li>
 *   <li>{@link WalletProvider} - TAS invoke 키로 InvokedDidDoc 서명 (Step 4)</li>
 *   <li>{@link StorageProvider} - 블록체인 DID 갱신 (Step 4)</li>
 *   <li>{@link CertificateProvider} - TAS certVcRef URL 조회 (Step 4)</li>
 *   <li>{@link TransactionProvider} - Transaction 조회 (Step 4, 5)</li>
 * </ul>
 *
 * <p><b>Server responsibilities (outside this class):</b></p>
 * <ul>
 *   <li>Transaction 상태 검증 및 관리 (각 Step 호출 전)</li>
 *   <li>Transaction 저장 (Step 1 SDK 반환 후, did + authNonce 저장)</li>
 *   <li>SubTransaction 저장 (각 Step SDK 반환 후)</li>
 *   <li>거래 완료 처리 (Step 5 SDK 반환 후)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class DidDocUpdateProtocolImpl implements DidDocUpdateProtocol {

    private final TokenProvider tokenProvider;
    private final UserProvider userProvider;
    private final DidDocumentProvider didDocumentProvider;
    private final WalletProvider walletProvider;
    private final StorageProvider storageProvider;
    private final CertificateProvider certificateProvider;
    private final TransactionProvider transactionProvider;

    // =========================================================================
    // Step 1: Propose Update DidDoc
    // =========================================================================

    /**
     * Step 1: DID Document 갱신 거래를 시작하고 txId 및 authNonce를 생성한다.
     */
    @Override
    public ProposeUpdateDidDocResponse proposeUpdateDidDoc(ProposeUpdateDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P141 Step 1: Propose Update DID Document ===");
        log.debug("Request DID: {}", request.getDid());

        try {
            // 1. Generate txId
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            // 2. Generate authNonce (16-byte)
            // Note: User status validation is the server's responsibility.
            // Server validates user status BEFORE calling this SDK method.
            log.debug("Step 1.1: Generating authNonce");
            String authNonce = generateAuthNonce();

            ProposeUpdateDidDocResponse response = ProposeUpdateDidDocResponse.builder()
                    .txId(txId)
                    .authNonce(authNonce)
                    .build();

            log.info("P141 Step 1 completed - txId: {}, authNonce: {}", txId, authNonce);
            return response;

        } catch (TaSdkException e) {
            log.error("P141 Step 1 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P141 Step 1 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.PROPOSE_UPDATE_DIDDOC_FAILED,
                "Failed to propose update DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 4: Request Update DidDoc
    // =========================================================================

    /**
     * Step 4: 갱신된 DID Document를 검증하고 블록체인에 등록한다.
     */
    @Override
    public RequestUpdateDidDocResponse requestUpdateDidDoc(RequestUpdateDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P141 Step 4: Request Update DID Document ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 4.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Parse and verify ownerDidDoc
            log.debug("Step 4.2: Parsing and verifying ownerDidDoc");
            DidDocument updatedDidDoc = parseAndVerifyOwnerDidDoc(
                request.getSignedDidDoc().getOwnerDidDoc());
            String userDid = updatedDidDoc.getId();
            log.debug("User DID: {}", userDid);

            // 3. Validate SignedDidDoc wallet proof
            log.debug("Step 4.3: Validating SignedDidDoc wallet proof");
            validateSignedDidDocWalletProof(request.getSignedDidDoc(), updatedDidDoc);

            // 4. Retrieve current DID Document
            log.debug("Step 4.4: Retrieving current DID Document");
            DidDocument currentDidDoc = didDocumentProvider.getDidDocument(userDid);
            if (currentDidDoc == null) {
                throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_NOT_FOUND,
                    "Current DID Document not found: " + userDid);
            }

            // 5. Validate DID Auth
            log.debug("Step 4.5: Validating DID Auth");
            validateDidAuth(request.getDidAuth(), request.getTxId(), userDid);

            // 6. Validate DID Document update (핵심 검증 로직)
            log.debug("Step 4.6: Validating DID Document update");
            validateDidDocumentUpdate(currentDidDoc, updatedDidDoc);

            // 7. Create InvokedDidDoc
            log.debug("Step 4.7: Creating InvokedDidDoc");
            InvokedDidDoc invokedDidDoc = createInvokedDidDoc(updatedDidDoc);

            // 8. Register updated DID Document to blockchain
            log.debug("Step 4.8: Registering updated DID Document to blockchain");
            try {
                storageProvider.registerDidDocument(invokedDidDoc, RoleType.ETC);
            } catch (TaSdkException e) {
                throw e;
            } catch (Exception e) {
                throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_REGISTRATION_FAILED,
                    "Blockchain DID registration failed: " + e.getMessage());
            }
            log.debug("DID Document updated on blockchain - userDid: {}", userDid);

            RequestUpdateDidDocResponse response = RequestUpdateDidDocResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P141 Step 4 completed - userDid: {}", userDid);
            return response;

        } catch (TaSdkException e) {
            log.error("P141 Step 4 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P141 Step 4 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.REQUEST_UPDATE_DIDDOC_FAILED,
                "Failed to request update DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 5: Confirm Update DidDoc
    // =========================================================================

    /**
     * Step 5: 서버 토큰을 검증하고 DID Document 갱신 완료를 확인한다.
     */
    @Override
    public ConfirmUpdateDidDocResponse confirmUpdateDidDoc(ConfirmUpdateDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P141 Step 5: Confirm Update DID Document ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            log.debug("Step 5.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            ConfirmUpdateDidDocResponse response = ConfirmUpdateDidDocResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P141 Step 5 completed - txId: {}", request.getTxId());
            return response;

        } catch (TaSdkException e) {
            log.error("P141 Step 5 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P141 Step 5 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.CONFIRM_UPDATE_DIDDOC_FAILED,
                "Failed to confirm update DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Private helper methods
    // =========================================================================

    /**
     * 사용자 상태를 검증한다 (ACTIVATED 필수).
     *
     * @deprecated This method is replaced by {@link UserProvider#validateUserStatusForUpdate(String)}.
     *             Status validation logic should be delegated to the server via Provider pattern,
     *             allowing different server implementations to define their own eligibility criteria.
     */
    @Deprecated
    private void validateUserStatus(User user) {
        if (user.getStatus() != UserStatus.ACTIVATED) {
            log.error("User is not activated: status={}", user.getStatus());
            throw new TaSdkException(TaSdkErrorCode.USER_NOT_ACTIVATED,
                "User status must be ACTIVATED, current: " + user.getStatus());
        }
        log.debug("User status validated: ACTIVATED");
    }

    /**
     * authNonce 생성 (16-byte, multibase).
     */
    private String generateAuthNonce() {
        try {
            byte[] nonceBytes = new byte[16];
            new java.security.SecureRandom().nextBytes(nonceBytes);
            return BaseMultibaseUtil.encode(nonceBytes);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.NONCE_GENERATION_FAILED,
                "Failed to generate authNonce: " + e.getMessage());
        }
    }

    /**
     * ownerDidDoc (multibase 인코딩)을 파싱하고 각 키의 자기서명을 검증한다.
     */
    private DidDocument parseAndVerifyOwnerDidDoc(String ownerDidDocMultibase) throws TaSdkException {
        byte[] decoded;
        try {
            decoded = BaseMultibaseUtil.decode(ownerDidDocMultibase);
        } catch (Exception e) {
            log.error("Failed to decode ownerDidDoc multibase: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.MULTIBASE_DECODE_FAILED,
                "Failed to decode ownerDidDoc: " + e.getMessage());
        }

        String ownerDidDocJson = new String(decoded, StandardCharsets.UTF_8);
        DidDocument ownerDidDoc;
        try {
            ownerDidDoc = BaseCoreDidUtil.parseDidDoc(ownerDidDocJson).getDocument();
        } catch (Exception e) {
            log.error("Failed to parse ownerDidDoc JSON: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_PARSING_FAILED,
                "Failed to parse ownerDidDoc: " + e.getMessage());
        }

        try {
            BaseCoreDidUtil.verifyDidDocKeyProofs(ownerDidDoc);
        } catch (Exception e) {
            log.error("OwnerDidDoc key proof verification failed: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.OWNER_DID_DOC_KEY_PROOF_FAILED,
                "OwnerDidDoc key proof verification failed: " + e.getMessage());
        }

        log.debug("OwnerDidDoc verified - did: {}", ownerDidDoc.getId());
        return ownerDidDoc;
    }

    /**
     * SignedDidDoc의 월렛 서명(proof)을 검증한다.
     */
    private void validateSignedDidDocWalletProof(
            SignedDidDoc signedDidDoc, DidDocument ownerDidDoc) throws TaSdkException {

        Proof proof = signedDidDoc.getProof();
        if (proof == null) {
            throw new TaSdkException(TaSdkErrorCode.PROOF_MISSING,
                "SignedDidDoc proof is missing");
        }

        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_VERIFICATION_METHOD,
                "Invalid SignedDidDoc verificationMethod: " + verificationMethod);
        }

        // Verify proofPurpose is ASSERTION_METHOD
        if (proof.getProofPurpose() != ProofPurpose.ASSERTION_METHOD) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_PROOF_PURPOSE,
                "SignedDidDoc proofPurpose must be assertionMethod, got: " + proof.getProofPurpose());
        }

        String walletDid = signedDidDoc.getWallet().getDid();

        // Verify wallet DID matches DID extracted from verificationMethod
        String didFromVerificationMethod = DidUtil.extractDid(verificationMethod);
        if (!walletDid.equals(didFromVerificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "Wallet DID mismatch: walletDid=" + walletDid + ", verificationMethod DID=" + didFromVerificationMethod);
        }

        DidDocument walletDidDoc;
        try {
            walletDidDoc = didDocumentProvider.getDidDocument(walletDid);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_NOT_FOUND,
                "Wallet DID Document not found: " + walletDid);
        }

        String publicKey;
        try {
            publicKey = BaseCoreDidUtil.getPublicKey(walletDidDoc, DidUtil.extractKeyId(verificationMethod));
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.PUBLIC_KEY_NOT_FOUND,
                "Public key not found: " + verificationMethod);
        }

        Proof proofWithoutValue = Proof.builder()
                .type(proof.getType())
                .created(proof.getCreated())
                .verificationMethod(proof.getVerificationMethod())
                .proofPurpose(proof.getProofPurpose())
                .proofValue(null)
                .build();

        SignedDidDoc signedDidDocForSigning = SignedDidDoc.builder()
                .ownerDidDoc(signedDidDoc.getOwnerDidDoc())
                .wallet(signedDidDoc.getWallet())
                .nonce(signedDidDoc.getNonce())
                .proof(proofWithoutValue)
                .build();

        byte[] signatureMessage;
        try {
            signatureMessage = SignatureCryptoHelper.generateSignatureMessage(signedDidDocForSigning);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.SIGNED_DID_DOC_WALLET_PROOF_FAILED,
                "Failed to generate SignedDidDoc signature message: " + e.getMessage());
        }

        try {
            SignatureCryptoHelper.verifySignature(
                publicKey, proof.getProofValue(), signatureMessage, proof.getType());
        } catch (Exception e) {
            log.error("SignedDidDoc wallet proof verification failed: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SIGNED_DID_DOC_WALLET_PROOF_FAILED,
                "SignedDidDoc wallet proof verification failed: " + e.getMessage());
        }

        log.debug("SignedDidDoc wallet proof verified");
    }

    /**
     * DID Auth를 검증한다.
     *
     * <p>검증 항목:</p>
     * <ol>
     *   <li>authNonce 일치 검증</li>
     *   <li>didAuth.did == expectedDid (ownerDidDoc.id) 일치</li>
     *   <li>didAuth.did == transaction.did 교차 검증 (Step 1에서 제출한 DID)</li>
     *   <li>DID 형식 검증</li>
     *   <li>proof null 검증</li>
     *   <li>verificationMethod 유효성 검증</li>
     *   <li>verificationMethod DID == didAuth.did 일치</li>
     *   <li>proofPurpose == AUTHENTICATION 검증</li>
     *   <li>사용자 DID Document 조회 → 공개키 추출</li>
     *   <li>DID Auth 서명(proof) 암호학적 검증</li>
     * </ol>
     */
    private void validateDidAuth(DidAuth didAuth, String txId, String expectedDid) {
        // 1. Retrieve transaction and check authNonce
        var transaction = transactionProvider.getTransaction(txId);
        String storedAuthNonce = transaction.getAuthNonce();

        if (storedAuthNonce == null || !storedAuthNonce.equals(didAuth.getAuthNonce())) {
            log.error("AuthNonce mismatch");
            throw new TaSdkException(TaSdkErrorCode.AUTH_NONCE_MISMATCH,
                "AuthNonce mismatch");
        }

        // 2. Verify didAuth.did matches expectedDid (= ownerDidDoc.id)
        String clientDid = didAuth.getDid();
        if (!expectedDid.equals(clientDid)) {
            log.error("DID mismatch: expectedDid={}, didAuth.did={}", expectedDid, clientDid);
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DID mismatch in DidAuth");
        }

        // 3. Cross-validate didAuth.did against transaction.did (set in Step 1)
        if (transaction.getDid() != null && !transaction.getDid().equals(clientDid)) {
            log.error("DID mismatch: didAuth.did={}, transaction.did={}", clientDid, transaction.getDid());
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DidAuth DID does not match transaction DID");
        }

        // 4. Validate DID format
        if (!DidValidator.isValidDid(clientDid)) {
            log.error("Invalid DID format: {}", clientDid);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
                "Invalid DID format: " + clientDid);
        }

        // 5. Validate proof is present
        Proof proof = didAuth.getProof();
        if (proof == null) {
            throw new TaSdkException(TaSdkErrorCode.PROOF_MISSING,
                "DidAuth proof is missing");
        }

        // 6. Validate verificationMethod
        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            log.error("Invalid verificationMethod: {}", verificationMethod);
            throw new TaSdkException(TaSdkErrorCode.INVALID_VERIFICATION_METHOD,
                "Invalid DidAuth verificationMethod: " + verificationMethod);
        }

        // 7. Verify DID in verificationMethod matches didAuth.did
        String didFromVm = DidUtil.extractDid(verificationMethod);
        if (!clientDid.equals(didFromVm)) {
            log.error("DID mismatch: didAuth.did={}, verificationMethod DID={}", clientDid, didFromVm);
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DidAuth DID does not match verificationMethod DID");
        }

        // 8. Validate proofPurpose is AUTHENTICATION
        if (proof.getProofPurpose() != ProofPurpose.AUTHENTICATION) {
            log.error("Invalid proofPurpose: {}", proof.getProofPurpose());
            throw new TaSdkException(TaSdkErrorCode.INVALID_PROOF_PURPOSE,
                "DidAuth proofPurpose must be authentication, got: " + proof.getProofPurpose());
        }

        // 9. Get user DID Document and extract public key
        DidDocument userDidDoc;
        try {
            userDidDoc = didDocumentProvider.getDidDocument(clientDid);
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_NOT_FOUND,
                "User DID Document not found for DidAuth: " + clientDid);
        }

        String keyId = verificationMethod.contains("#")
            ? verificationMethod.substring(verificationMethod.lastIndexOf("#") + 1)
            : verificationMethod;
        String publicKey;
        try {
            publicKey = BaseCoreDidUtil.getPublicKey(userDidDoc, keyId);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.PUBLIC_KEY_NOT_FOUND,
                "Public key not found for DidAuth: " + verificationMethod);
        }

        // 10. Extract signature message (removes proofValue) and verify signature
        byte[] signatureMessage;
        try {
            signatureMessage = SignatureCryptoHelper.extractSignatureMessage(didAuth);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_VERIFICATION_FAILED,
                "Failed to extract DidAuth signature message: " + e.getMessage());
        }

        try {
            SignatureCryptoHelper.verifySignature(
                publicKey, proof.getProofValue(), signatureMessage, proof.getType());
        } catch (Exception e) {
            log.error("DidAuth signature verification failed: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_VERIFICATION_FAILED,
                "DidAuth signature verification failed: " + e.getMessage());
        }

        log.debug("DID Auth validated - did: {}", clientDid);
    }

    /**
     * DID Document 갱신을 검증한다 (핵심 로직).
     */
    private void validateDidDocumentUpdate(DidDocument current, DidDocument updated) {
        // 1. Validate version
        validateVersion(current, updated);

        // 2. Validate updated time
        validateUpdatedTime(current, updated);

        // 3. Validate immutable fields
        validateImmutableFields(current, updated);
    }

    /**
     * 버전 검증: updated.versionId == current.versionId + 1
     */
    private void validateVersion(DidDocument current, DidDocument updated) {
        int currentVersion = Integer.parseInt(current.getVersionId());
        int updatedVersion = Integer.parseInt(updated.getVersionId());

        if (updatedVersion != currentVersion + 1) {
            log.error("Invalid version: current={}, updated={}", currentVersion, updatedVersion);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_VERSION,
                    "Version must be incremented by 1: current=" + currentVersion + ", updated=" + updatedVersion);
        }

        log.debug("Version validated: {} -> {}", currentVersion, updatedVersion);
    }

    /**
     * 시간 검증: updated.updated > current.updated
     */
    private void validateUpdatedTime(DidDocument current, DidDocument updated) {
        String currentUpdated = current.getUpdated();
        String updatedUpdated = updated.getUpdated();

        boolean isLater = DateTimeUtil.isSecondDateTimeLater(currentUpdated, updatedUpdated);

        if (!isLater) {
            log.error("Invalid updated time: current={}, updated={}",
                      currentUpdated, updatedUpdated);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_UPDATED,
                    "Updated time must be later than current: current=" + currentUpdated + ", updated=" + updatedUpdated);
        }

        log.debug("Updated time validated: {} -> {}", currentUpdated, updatedUpdated);
    }

    /**
     * 불변 필드 검증.
     */
    private void validateImmutableFields(DidDocument current, DidDocument updated) {
        // 1. @context 검증
        validateContext(current.getContext(), updated.getContext());

        // 2. id 검증
        validateId(current.getId(), updated.getId());

        // 3. controller 검증
        validateController(current.getController(), updated.getController());

        // 4. created 검증
        validateCreated(current.getCreated(), updated.getCreated());

        // 5. deactivated 검증
        validateDeactivated(current.getDeactivated(), updated.getDeactivated());

        log.debug("Immutable fields validated");
    }

    private void validateContext(List<String> current, List<String> updated) {
        Set<String> currentSet = new HashSet<>(current);
        Set<String> updatedSet = new HashSet<>(updated);

        if (!currentSet.equals(updatedSet)) {
            log.error("Context mismatch: current={}, updated={}", current, updated);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_CONTEXT,
                "Context must not change");
        }
    }

    private void validateId(String currentId, String updatedId) {
        if (!currentId.equals(updatedId)) {
            log.error("ID mismatch: current={}, updated={}", currentId, updatedId);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_ID,
                "ID must not change");
        }
    }

    private void validateController(String currentController, String updatedController) {
        if (!currentController.equals(updatedController)) {
            log.error("Controller mismatch: current={}, updated={}",
                      currentController, updatedController);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_CONTROLLER,
                "Controller must not change");
        }
    }

    private void validateCreated(String currentCreated, String updatedCreated) {
        if (!currentCreated.equals(updatedCreated)) {
            log.error("Created time mismatch: current={}, updated={}",
                      currentCreated, updatedCreated);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_CREATED,
                "Created time must not change");
        }
    }

    private void validateDeactivated(boolean currentDeactivated, boolean updatedDeactivated) {
        if (currentDeactivated != updatedDeactivated) {
            log.error("Deactivated status mismatch: current={}, updated={}",
                      currentDeactivated, updatedDeactivated);
            throw new TaSdkException(TaSdkErrorCode.INVALID_DID_DOC_DEACTIVATED,
                "Deactivated status must not change");
        }
    }

    /**
     * ownerDidDoc에서 InvokedDidDoc을 생성하고 TAS invoke 키로 서명한다.
     */
    private InvokedDidDoc createInvokedDidDoc(DidDocument ownerDidDoc) throws TaSdkException {
        try {
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            if (tasDidDoc == null) {
                throw new TaSdkException(TaSdkErrorCode.TAS_DID_DOCUMENT_NOT_FOUND);
            }

            String tasCertVcRef = certificateProvider.generateCertificateVcUrl(tasDidDoc.getId());
            ProofType proofType = resolveInvokeKeyProofType(tasDidDoc);

            InvokedDidDoc unsignedDoc = DidDocCryptoHelper.generateInvokedDidDoc(
                tasDidDoc, ownerDidDoc, proofType, tasCertVcRef);

            walletProvider.ensureWalletConnection();
            InvokedDidDoc signedDoc = walletProvider.signInvokedDidDoc(unsignedDoc, "invoke");

            log.debug("InvokedDidDoc created and signed");
            return signedDoc;

        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create InvokedDidDoc: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.INVOKED_DID_DOC_CREATION_FAILED,
                "Failed to create InvokedDidDoc: " + e.getMessage());
        }
    }

    /**
     * TAS DID Document의 invoke 키에서 ProofType을 결정한다.
     */
    private ProofType resolveInvokeKeyProofType(DidDocument tasDidDoc) {
        try {
            List<String> capabilityInvocations = tasDidDoc.getCapabilityInvocation();
            if (capabilityInvocations != null && !capabilityInvocations.isEmpty()) {
                String methodRef = capabilityInvocations.get(0);
                String keyId = methodRef.contains("#")
                    ? methodRef.substring(methodRef.lastIndexOf("#") + 1)
                    : methodRef;
                VerificationMethod vm = BaseCoreDidUtil.getVerificationMethod(tasDidDoc, keyId);
                String keyType = vm.getType();
                if (keyType != null) {
                    if (keyType.contains("Secp256r1")) return ProofType.SECP_256R1_SIGNATURE_2018;
                    if (keyType.contains("Secp256k1")) return ProofType.SECP_256K1_SIGNATURE_2018;
                    if (keyType.contains("Rsa")) return ProofType.RSA_SIGNATURE_2018;
                }
            }
        } catch (Exception e) {
            log.debug("Could not resolve invoke key ProofType, using default: {}", e.getMessage());
        }
        return ProofType.SECP_256R1_SIGNATURE_2018;
    }
}
