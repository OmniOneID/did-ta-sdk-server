package org.omnione.did.tas.protocol.diddoc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.data.DidAuth;
import org.omnione.did.tas.model.request.protocol.user.*;
import org.omnione.did.tas.model.response.protocol.user.*;
import org.omnione.did.tas.provider.*;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * P142: DID Document 복구 프로토콜 구현체.
 *
 * <p>비활성화된 사용자의 DID Document를 안전하게 복구하는 프로토콜을 구현한다.
 * Offer 생성/검증은 서버가 별도 API로 처리하며, SDK는 핵심 3단계만 담당한다.
 * DB 접근과 트랜잭션 관리는 Provider 인터페이스를 통해 서버에 위임한다.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TokenProvider} - 서버 토큰 검증 (Step 4, 5)</li>
 *   <li>{@link TransactionProvider} - Transaction 조회 (Step 4)</li>
 *   <li>{@link DidDocumentProvider} - 사용자 DID Document 조회 (Step 4 didAuth 서명 검증)</li>
 *   <li>{@link StorageProvider} - 블록체인 DID Document 상태 변경 (Step 4)</li>
 * </ul>
 *
 * <p><b>Server responsibilities (outside this class):</b></p>
 * <ul>
 *   <li>Offer 생성 및 Push/Email 발송 (/offer-restore-did/push|email)</li>
 *   <li>User 상태 검증 (DEACTIVATED 필수, Step 1 호출 전)</li>
 *   <li>Offer 검증 (Step 1 호출 전)</li>
 *   <li>Transaction 상태 검증 및 관리 (각 Step 호출 전)</li>
 *   <li>Transaction 저장 (Step 1 SDK 반환 후, did + authNonce 저장)</li>
 *   <li>SubTransaction 저장 (각 Step SDK 반환 후)</li>
 *   <li>User 상태 갱신 (Step 5 SDK 반환 후, DEACTIVATED → ACTIVATED)</li>
 *   <li>거래 완료 처리 (Step 5 SDK 반환 후)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class DidDocRestoreProtocolImpl implements DidDocRestoreProtocol {

    private final TokenProvider tokenProvider;
    private final TransactionProvider transactionProvider;
    private final DidDocumentProvider didDocumentProvider;
    private final StorageProvider storageProvider;

    // =========================================================================
    // Step 1: Propose Restore DidDoc
    // =========================================================================

    /**
     * Step 1: DID Document 복구 거래를 시작하고 txId 및 authNonce를 생성한다.
     */
    @Override
    public ProposeRestoreDidDocResponse proposeRestoreDidDoc(ProposeRestoreDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P142 Step 1: Propose Restore DID Document ===");
        log.debug("Request DID: {}", request.getDid());

        try {
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            String authNonce = generateAuthNonce();

            ProposeRestoreDidDocResponse response = ProposeRestoreDidDocResponse.builder()
                    .txId(txId)
                    .authNonce(authNonce)
                    .build();

            log.info("P142 Step 1 completed - txId: {}", txId);
            return response;

        } catch (TaSdkException e) {
            log.error("P142 Step 1 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P142 Step 1 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.PROPOSE_RESTORE_DIDDOC_FAILED,
                "Failed to propose restore DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 4: Request Restore DidDoc
    // =========================================================================

    /**
     * Step 4: DID Auth를 검증하고 블록체인의 DID Document 상태를 ACTIVATED로 변경한다.
     */
    @Override
    public RequestRestoreDidDocResponse requestRestoreDidDoc(RequestRestoreDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P142 Step 4: Request Restore DID Document ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 4.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Validate DID Auth (returns userDid from transaction)
            log.debug("Step 4.2: Validating DID Auth");
            String userDid = validateDidAuth(request.getDidAuth(), request.getTxId());

            // 3. Update DID Document status to ACTIVATED on blockchain
            log.debug("Step 4.3: Updating DID Document status to ACTIVATED - did: {}", userDid);
            try {
                storageProvider.updateDidDocumentStatus(userDid, "ACTIVATED");
            } catch (TaSdkException e) {
                throw e;
            } catch (Exception e) {
                throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_REGISTRATION_FAILED,
                    "Failed to update DID Document status: " + e.getMessage());
            }

            RequestRestoreDidDocResponse response = RequestRestoreDidDocResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P142 Step 4 completed - userDid: {}", userDid);
            return response;

        } catch (TaSdkException e) {
            log.error("P142 Step 4 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P142 Step 4 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.REQUEST_RESTORE_DIDDOC_FAILED,
                "Failed to request restore DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Step 5: Confirm Restore DidDoc
    // =========================================================================

    /**
     * Step 5: 서버 토큰을 검증하고 DID Document 복구 완료를 확인한다.
     */
    @Override
    public ConfirmRestoreDidDocResponse confirmRestoreDidDoc(ConfirmRestoreDidDocRequest request)
            throws TaSdkException {

        log.debug("=== P142 Step 5: Confirm Restore DID Document ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            log.debug("Step 5.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            ConfirmRestoreDidDocResponse response = ConfirmRestoreDidDocResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P142 Step 5 completed - txId: {}", request.getTxId());
            return response;

        } catch (TaSdkException e) {
            log.error("P142 Step 5 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P142 Step 5 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.CONFIRM_RESTORE_DIDDOC_FAILED,
                "Failed to confirm restore DID Document: " + e.getMessage());
        }
    }

    // =========================================================================
    // Private helper methods
    // =========================================================================

    /**
     * authNonce 생성 (16-byte, multibase).
     */
    private String generateAuthNonce() {
        try {
            byte[] nonceBytes = new byte[16];
            new SecureRandom().nextBytes(nonceBytes);
            return BaseMultibaseUtil.encode(nonceBytes);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.NONCE_GENERATION_FAILED,
                "Failed to generate authNonce: " + e.getMessage());
        }
    }

    /**
     * DID Auth를 완전히 검증하고 사용자 DID를 반환한다.
     *
     * <p>검증 항목:</p>
     * <ol>
     *   <li>authNonce 일치 (transaction에 저장된 값과 비교)</li>
     *   <li>didAuth.did == transaction.did 교차 검증</li>
     *   <li>DID 형식 유효성</li>
     *   <li>proof null 검사</li>
     *   <li>verificationMethod 형식 유효성</li>
     *   <li>verificationMethod DID == didAuth.did</li>
     *   <li>proofPurpose == AUTHENTICATION</li>
     *   <li>사용자 DID Document 조회 → 공개키 추출</li>
     *   <li>서명 메시지 추출 → 서명 검증</li>
     * </ol>
     *
     * @param didAuth    검증할 DidAuth
     * @param txId       거래 ID
     * @return 검증된 사용자 DID
     */
    private String validateDidAuth(DidAuth didAuth, String txId) {
        var transaction = transactionProvider.getTransaction(txId);

        // 1. authNonce 비교
        String storedAuthNonce = transaction.getAuthNonce();
        if (storedAuthNonce == null || !storedAuthNonce.equals(didAuth.getAuthNonce())) {
            log.error("AuthNonce mismatch");
            throw new TaSdkException(TaSdkErrorCode.AUTH_NONCE_MISMATCH, "AuthNonce mismatch");
        }

        String clientDid = didAuth.getDid();

        // 2. transaction.did 교차 검증
        if (transaction.getDid() != null && !transaction.getDid().equals(clientDid)) {
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DidAuth DID does not match transaction DID");
        }

        // 3. DID 형식 유효성
        if (!DidValidator.isValidDid(clientDid)) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
                "Invalid DID format: " + clientDid);
        }

        // 4. proof null 검사
        Proof proof = didAuth.getProof();
        if (proof == null) {
            throw new TaSdkException(TaSdkErrorCode.PROOF_MISSING, "DidAuth proof is missing");
        }

        // 5. verificationMethod 형식 유효성
        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_VERIFICATION_METHOD,
                "Invalid DidAuth verificationMethod: " + verificationMethod);
        }

        // 6. verificationMethod DID == didAuth.did
        String didFromVm = DidUtil.extractDid(verificationMethod);
        if (!clientDid.equals(didFromVm)) {
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DidAuth DID does not match verificationMethod DID");
        }

        // 7. proofPurpose == AUTHENTICATION
        if (proof.getProofPurpose() != ProofPurpose.AUTHENTICATION) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_PROOF_PURPOSE,
                "DidAuth proofPurpose must be authentication, got: " + proof.getProofPurpose());
        }

        // 8. 사용자 DID Document 조회 → 공개키 추출
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

        // 9. 서명 메시지 추출 → 서명 검증
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
        return clientDid;
    }
}
