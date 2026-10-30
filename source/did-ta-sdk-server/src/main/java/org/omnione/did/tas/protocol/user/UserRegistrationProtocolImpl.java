package org.omnione.did.tas.protocol.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.datamodel.enums.did.ProofType;
import org.omnione.did.common.domain.did.DidDocCryptoHelper;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.data.SignedDidDoc;
import org.omnione.did.tas.model.domain.PiiData;
import org.omnione.did.tas.model.request.protocol.user.ConfirmRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.ProposeRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.RequestRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.RetrieveKycRequest;
import org.omnione.did.tas.model.response.protocol.user.ConfirmRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.ProposeRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.RequestRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.RetrieveKycResponse;
import org.omnione.did.tas.provider.CertificateProvider;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.KycProvider;
import org.omnione.did.tas.provider.StorageProvider;
import org.omnione.did.tas.provider.TokenProvider;
import org.omnione.did.tas.provider.UserProvider;
import org.omnione.did.tas.provider.WalletProvider;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * P132: 사용자 등록 프로토콜 구현체.
 *
 * <p>사용자 DID Document를 블록체인에 등록하는 프로토콜을 구현한다.
 * DB 접근과 트랜잭션 관리는 Provider 인터페이스를 통해 서버에 위임한다.</p>
 *
 * <p><b>Note:</b> Step 3 (requestCreateToken)은 서버의 TokenService가 직접 처리하므로
 * 이 클래스에 포함되지 않는다.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TokenProvider} - 서버 토큰 검증 (Step 4, 5, 6)</li>
 *   <li>{@link KycProvider} - KYC PII 조회 (Step 4)</li>
 *   <li>{@link UserProvider} - 사용자 DID 중복 확인 (Step 5)</li>
 *   <li>{@link DidDocumentProvider} - DID Document 조회 (Step 5)</li>
 *   <li>{@link WalletProvider} - TAS invoke 키로 InvokedDidDoc 서명 (Step 5)</li>
 *   <li>{@link StorageProvider} - 블록체인 DID 등록 (Step 5)</li>
 *   <li>{@link CertificateProvider} - TAS certVcRef URL 조회 (Step 5)</li>
 * </ul>
 *
 * <p><b>Server responsibilities (outside this class):</b></p>
 * <ul>
 *   <li>Transaction 상태 검증 및 관리 (각 Step 호출 전)</li>
 *   <li>PII 저장 (Step 4 SDK 반환 후 response.getPiiData() 저장)</li>
 *   <li>User 레코드 저장 (Step 5 SDK 반환 후)</li>
 *   <li>거래 완료 처리 (Step 6 SDK 반환 후)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationProtocolImpl implements UserRegistrationProtocol {

    private final TokenProvider tokenProvider;
    private final KycProvider kycProvider;
    private final UserProvider userProvider;
    private final DidDocumentProvider didDocumentProvider;
    private final WalletProvider walletProvider;
    private final StorageProvider storageProvider;
    private final CertificateProvider certificateProvider;

    // =========================================================================
    // Step 1: Propose Register User
    // =========================================================================

    /**
     * Step 1: 사용자 등록 거래를 시작하고 txId를 생성한다.
     */
    @Override
    public ProposeRegisterUserResponse proposeRegisterUser(ProposeRegisterUserRequest request)
            throws TaSdkException {

        log.debug("=== P132 Step 1: Propose Register User ===");
        log.debug("Request ID: {}", request.getId());

        try {
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            ProposeRegisterUserResponse response = ProposeRegisterUserResponse.builder()
                    .txId(txId)
                    .build();

            log.info("P132 Step 1 completed - txId: {}", txId);
            return response;

        } catch (TaSdkException e) {
            log.error("P132 Step 1 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P132 Step 1 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to propose register user: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 4: Retrieve KYC
    // =========================================================================

    /**
     * Step 4: 서버 토큰 검증 후 KYC 시스템에서 PII를 조회한다.
     */
    @Override
    public RetrieveKycResponse retrieveKyc(RetrieveKycRequest request)
            throws TaSdkException {

        log.debug("=== P132 Step 4: Retrieve KYC ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 4.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Retrieve PII - Option A or Option B (one must be provided)
            PiiData piiData;
            if (request.getKycToken() != null && !request.getKycToken().isEmpty()) {
                // Option A: KYC 시스템이 발급한 토큰에서 PII 추출
                log.debug("Step 4.2: Retrieving PII from kycToken (Option A)");
                piiData = kycProvider.getPiiFromKycToken(request.getKycToken());
            } else if (request.getKycTxId() != null && !request.getKycTxId().isEmpty()) {
                // Option B: 외부 KYC 서버 API로 PII 조회
                log.debug("Step 4.2: Retrieving PII from KYC server by kycTxId (Option B)");
                piiData = kycProvider.retrieveKycData(request.getKycTxId());
            } else {
                throw new TaSdkException(TaSdkErrorCode.KYC_TOKEN_AND_TXID_BOTH_MISSING,
                    "Either kycToken (Option A) or kycTxId (Option B) must be provided");
            }

            if (piiData == null) {
                throw new TaSdkException(TaSdkErrorCode.KYC_DATA_RETRIEVAL_FAILED,
                    "KYC provider returned null PiiData");
            }

            log.debug("Step 4.3: PII retrieved successfully");

            RetrieveKycResponse response = RetrieveKycResponse.builder()
                    .txId(request.getTxId())
                    .piiData(piiData)   // 내부 전용: 서버가 txId와 연결하여 저장
                    .build();

            log.info("P132 Step 4 completed - txId: {}", request.getTxId());
            return response;

        } catch (TaSdkException e) {
            log.error("P132 Step 4 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P132 Step 4 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to retrieve KYC: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 5: Request Register User
    // =========================================================================

    /**
     * Step 5: SignedDidDoc 검증, 블록체인 DID 등록을 수행한다.
     */
    @Override
    public RequestRegisterUserResponse requestRegisterUser(RequestRegisterUserRequest request)
            throws TaSdkException {

        log.debug("=== P132 Step 5: Request Register User ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 5.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Parse and verify ownerDidDoc
            log.debug("Step 5.2: Parsing and verifying ownerDidDoc");
            DidDocument ownerDidDoc = parseAndVerifyOwnerDidDoc(
                request.getSignedDidDoc().getOwnerDidDoc());
            String userDid = ownerDidDoc.getId();
            log.debug("User DID: {}", userDid);

            // 3. Check DID duplicate
            log.debug("Step 5.3: Checking user DID duplicate");
            if (userProvider.existsUserByDid(userDid)) {
                throw new TaSdkException(TaSdkErrorCode.USER_DID_ALREADY_EXISTS,
                    "User DID already registered: " + userDid);
            }

            // 4. Validate SignedDidDoc wallet proof
            log.debug("Step 5.4: Validating SignedDidDoc wallet proof");
            validateSignedDidDocWalletProof(request.getSignedDidDoc(), ownerDidDoc);

            // 5. Create InvokedDidDoc
            log.debug("Step 5.5: Creating InvokedDidDoc");
            InvokedDidDoc invokedDidDoc = createInvokedDidDoc(ownerDidDoc);

            // 6. Register DID Document to blockchain
            log.debug("Step 5.6: Registering DID Document to blockchain");
            try {
                storageProvider.registerDidDocument(invokedDidDoc, RoleType.ETC);
            } catch (TaSdkException e) {
                throw e;
            } catch (Exception e) {
                throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_REGISTRATION_FAILED,
                    "Blockchain DID registration failed: " + e.getMessage());
            }
            log.debug("DID Document registered to blockchain - userDid: {}", userDid);

            RequestRegisterUserResponse response = RequestRegisterUserResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P132 Step 5 completed - userDid: {}", userDid);
            return response;

        } catch (TaSdkException e) {
            log.error("P132 Step 5 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P132 Step 5 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to request register user: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 6: Confirm Register User
    // =========================================================================

    /**
     * Step 6: 서버 토큰을 검증하고 사용자 등록 완료를 확인한다.
     */
    @Override
    public ConfirmRegisterUserResponse confirmRegisterUser(ConfirmRegisterUserRequest request)
            throws TaSdkException {

        log.debug("=== P132 Step 6: Confirm Register User ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            log.debug("Step 6.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            ConfirmRegisterUserResponse response = ConfirmRegisterUserResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P132 Step 6 completed - txId: {}", request.getTxId());
            return response;

        } catch (TaSdkException e) {
            log.error("P132 Step 6 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P132 Step 6 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to confirm register user: " + e.getMessage());
        }
    }

    // =========================================================================
    // Private helper methods
    // =========================================================================

    /**
     * ownerDidDoc (multibase 인코딩)을 파싱하고 각 키의 자기서명을 검증한다.
     */
    private DidDocument parseAndVerifyOwnerDidDoc(String ownerDidDocMultibase) throws TaSdkException {
        byte[] decoded;
        try {
            decoded = BaseMultibaseUtil.decode(ownerDidDocMultibase);
        } catch (Exception e) {
            log.error("Failed to decode ownerDidDoc multibase: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_MULTIBASE,
                "Failed to decode ownerDidDoc: " + e.getMessage());
        }

        String ownerDidDocJson = new String(decoded, StandardCharsets.UTF_8);
        DidDocument ownerDidDoc;
        try {
            ownerDidDoc = BaseCoreDidUtil.parseDidDoc(ownerDidDocJson).getDocument();
        } catch (Exception e) {
            log.error("Failed to parse ownerDidDoc JSON: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
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
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "SignedDidDoc proof is missing");
        }

        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "Invalid SignedDidDoc verificationMethod: " + verificationMethod);
        }

        // Verify proofPurpose is ASSERTION_METHOD
        if (proof.getProofPurpose() != ProofPurpose.ASSERTION_METHOD) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "SignedDidDoc proofPurpose must be assertionMethod, got: " + proof.getProofPurpose());
        }

        String walletDid = signedDidDoc.getWallet().getDid();

        // Verify wallet DID matches DID extracted from verificationMethod
        // DidUtil.extractDid strips both ?query and #fragment from the full DID Key URL
        String didFromVerificationMethod = DidUtil.extractDid(verificationMethod);
        if (!walletDid.equals(didFromVerificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "Wallet DID mismatch: walletDid=" + walletDid + ", verificationMethod DID=" + didFromVerificationMethod);
        }
        DidDocument walletDidDoc;
        try {
            walletDidDoc = didDocumentProvider.getDidDocument(walletDid);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND,
                "Wallet DID Document not found: " + walletDid);
        }

        String publicKey;
        try {
            publicKey = BaseCoreDidUtil.getPublicKey(walletDidDoc, DidUtil.extractKeyId(verificationMethod));
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.SDK_KEY_NOT_FOUND,
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
