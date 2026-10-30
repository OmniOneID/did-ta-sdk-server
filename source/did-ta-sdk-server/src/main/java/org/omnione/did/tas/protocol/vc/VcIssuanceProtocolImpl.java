package org.omnione.did.tas.protocol.vc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.tas.client.IssuerApiClient;
import org.omnione.did.tas.exception.IssuerApiException;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.data.DidAuth;
import org.omnione.did.tas.model.domain.VcPlanInfo;
import org.omnione.did.tas.model.request.issuer.InspectIssueProposeApiRequest;
import org.omnione.did.tas.model.request.protocol.vc.ConfirmIssueVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.ProposeIssueVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.RequestIssueProfileRequest;
import org.omnione.did.tas.model.request.protocol.vc.RequestIssueVcRequest;
import org.omnione.did.tas.model.response.issuer.InspectIssueProposeApiResponse;
import org.omnione.did.tas.model.response.protocol.vc.ConfirmIssueVcResult;
import org.omnione.did.tas.model.response.protocol.vc.ProposeIssueVcResult;
import org.omnione.did.tas.model.response.protocol.vc.RequestIssueProfileResult;
import org.omnione.did.tas.model.response.protocol.vc.RequestIssueVcResult;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.TokenProvider;
import org.omnione.did.tas.provider.VcIssuanceDataProvider;

import java.util.UUID;

/**
 * P210 VC Issuance Protocol implementation.
 *
 * <p>Implements the VC Issuance Protocol for P210.
 * This class handles the business logic for VC issuance, delegating
 * Issuer API communication to {@link IssuerApiClient} and data access
 * to {@link VcIssuanceDataProvider}.</p>
 *
 * <p><b>Design Pattern:</b></p>
 * <ul>
 *   <li>SDK handles: Protocol flow, validation, Issuer API orchestration, serverToken/DidAuth crypto verification</li>
 *   <li>Server handles: HTTP communication (IssuerApiClient), DB access (DataProvider), Transaction management</li>
 * </ul>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TokenProvider} - serverToken 검증 (Step 5, 6, 7)</li>
 *   <li>{@link DidDocumentProvider} - 사용자 DID Document 조회 (Step 6 DidAuth 서명 검증)</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
public class VcIssuanceProtocolImpl implements VcIssuanceProtocol {

    private final TokenProvider tokenProvider;
    private final DidDocumentProvider didDocumentProvider;

    // =========================================================================
    // Step 2: Propose Issue VC
    // =========================================================================

    /**
     * Step 2: Propose Issue VC.
     *
     * <p>Validates VC Plan, checks Issuer status, generates txId,
     * and calls Issuer API to validate the issuance proposal.</p>
     */
    @Override
    public ProposeIssueVcResult proposeIssueVc(
        ProposeIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P210 Step 2: Propose Issue VC ===");
        log.debug("VC Plan ID: {}", request.getVcPlanId());

        try {
            // 1. Validate VC Plan
            log.debug("Step 2.1: Validating VC Plan");
            VcPlanInfo vcPlan = dataProvider.getVcPlan(request.getVcPlanId());
            if (vcPlan == null) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_PLAN_NOT_FOUND,
                    "VC Plan not found: " + request.getVcPlanId()
                );
            }

            // 2. Determine Issuer DID
            String issuerDid = (request.getIssuer() != null && !request.getIssuer().isEmpty())
                ? request.getIssuer()
                : vcPlan.getIssuerDid();

            if (issuerDid == null || issuerDid.isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_NOT_FOUND,
                    "Issuer DID not found in request or VC Plan"
                );
            }
            log.debug("Issuer DID: {}", issuerDid);

            // 3. Generate TAS txId
            log.debug("Step 2.2: Generating TAS txId");
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            // 4. Call Issuer API: inspect-propose-issue
            log.debug("Step 2.3: Calling Issuer API - inspect-propose-issue");
            InspectIssueProposeApiRequest apiRequest = InspectIssueProposeApiRequest.builder()
                .id(request.getId())
                .vcPlanId(request.getVcPlanId())
                .issuer(issuerDid)
                .offerId(request.getOfferId())
                .build();

            InspectIssueProposeApiResponse apiResponse;
            try {
                apiResponse = issuerClient.inspectProposeIssue(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Failed to inspect propose with Issuer: " + e.getMessage(),
                    e
                );
            }

            // 5. Validate Issuer response
            if (apiResponse.getTxId() == null || apiResponse.getTxId().isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Issuer returned null or empty txId"
                );
            }

            if (apiResponse.getRefId() == null || apiResponse.getRefId().isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Issuer returned null or empty refId"
                );
            }

            log.debug("Issuer txId: {}", apiResponse.getTxId());
            log.debug("Issuer refId: {}", apiResponse.getRefId());

            // 6. Build result
            ProposeIssueVcResult result = ProposeIssueVcResult.builder()
                .txId(txId)
                .externalTxId(apiResponse.getTxId())
                .refId(apiResponse.getRefId())
                .build();

            log.info("P210 Step 2 completed - txId: {}, externalTxId: {}", txId, apiResponse.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P210 Step 2 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P210 Step 2 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to propose issue VC: " + e.getMessage()
            );
        }
    }

    // =========================================================================
    // Step 5: Request Issue Profile
    // =========================================================================

    /**
     * Step 5: Request Issue Profile.
     *
     * <p>서버 토큰 검증 후 Issuer로부터 Issue Profile을 요청하고 authNonce를 생성한다.</p>
     */
    @Override
    public RequestIssueProfileResult requestIssueProfile(
        RequestIssueProfileRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P210 Step 5: Request Issue Profile ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 5.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Retrieve Issuer txId
            log.debug("Step 5.2: Retrieving Issuer txId");
            String externalTxId = dataProvider.getExternalTxId(request.getTxId());
            log.debug("External txId: {}", externalTxId);

            // 3. Retrieve Holder info
            log.debug("Step 5.3: Retrieving Holder info");
            org.omnione.did.tas.model.domain.HolderInfo holderInfo =
                dataProvider.getHolderInfo(request.getTxId());

            if (holderInfo == null || holderInfo.getDid() == null) {
                throw new TaSdkException(
                    TaSdkErrorCode.HOLDER_NOT_FOUND,
                    "Holder information not found for txId: " + request.getTxId()
                );
            }
            log.debug("Holder DID: {}", holderInfo.getDid());

            // 4. Call Issuer API: generate-issue-profile
            log.debug("Step 5.4: Calling Issuer API - generate-issue-profile");
            org.omnione.did.tas.model.request.issuer.GenerateIssueProfileApiRequest apiRequest =
                org.omnione.did.tas.model.request.issuer.GenerateIssueProfileApiRequest.builder()
                    .id(request.getId())
                    .txId(externalTxId)
                    .holder(org.omnione.did.tas.model.request.issuer.GenerateIssueProfileApiRequest.ApiHolder.builder()
                        .did(holderInfo.getDid())
                        .pii(holderInfo.getPii())
                        .build())
                    .build();

            org.omnione.did.tas.model.response.issuer.GenerateIssueProfileApiResponse apiResponse;
            try {
                apiResponse = issuerClient.generateIssueProfile(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Failed to generate issue profile from Issuer: " + e.getMessage(),
                    e
                );
            }

            // 5. Validate IssueProfile
            if (apiResponse.getProfile() == null) {
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUE_PROFILE_RETRIEVAL_FAILED,
                    "Issuer returned null IssueProfile"
                );
            }

            // 6. Generate authNonce (16-byte)
            log.debug("Step 5.5: Generating authNonce");
            byte[] authNonceBytes = org.omnione.did.common.util.NonceGenerator.generate16ByteNonce();
            String authNonce = org.omnione.did.common.util.multibase.BaseMultibaseUtil.encode(authNonceBytes);
            log.debug("Generated authNonce: {}", authNonce);

            // 7. Build result
            RequestIssueProfileResult result = RequestIssueProfileResult.builder()
                .txId(request.getTxId())
                .authNonce(authNonce)
                .profile(apiResponse.getProfile())
                .build();

            log.info("P210 Step 5 completed - txId: {}", request.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P210 Step 5 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P210 Step 5 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to request issue profile: " + e.getMessage()
            );
        }
    }

    // =========================================================================
    // Step 6: Request Issue VC
    // =========================================================================

    /**
     * Step 6: Request Issue VC.
     *
     * <p>서버 토큰 검증, DID Auth 완전 검증 후 Issuer로부터 VC를 발급받는다.</p>
     */
    @Override
    public RequestIssueVcResult requestIssueVc(
        RequestIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P210 Step 6: Request Issue VC ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 6.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Validate DID Auth
            log.debug("Step 6.2: Validating DID Auth");
            String authNonce = dataProvider.getAuthNonce(request.getTxId());
            validateDidAuth(request.getDidAuth(), authNonce);
            log.debug("DID Auth validated successfully");

            // 3. Retrieve Issuer txId
            log.debug("Step 6.3: Retrieving Issuer txId");
            String externalTxId = dataProvider.getExternalTxId(request.getTxId());

            // 4. Call Issuer API: issue-vc
            log.debug("Step 6.4: Calling Issuer API - issue-vc");
            org.omnione.did.tas.model.request.issuer.IssueVcApiRequest apiRequest =
                org.omnione.did.tas.model.request.issuer.IssueVcApiRequest.builder()
                    .id(request.getId())
                    .txId(externalTxId)
                    .accE2e(request.getAccE2e())
                    .encReqVc(request.getEncReqVc())
                    .build();

            org.omnione.did.tas.model.response.issuer.IssueVcApiResponse apiResponse;
            try {
                apiResponse = issuerClient.issueVc(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Failed to issue VC from Issuer: " + e.getMessage(),
                    e
                );
            }

            // 5. Validate E2E response
            if (apiResponse.getE2e() == null) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_ISSUANCE_FAILED,
                    "Issuer returned null E2E encrypted VC"
                );
            }

            if (apiResponse.getE2e().getEncVc() == null || apiResponse.getE2e().getEncVc().isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_ENCRYPTION_FAILED,
                    "Encrypted VC is missing in Issuer response"
                );
            }

            // 6. Build result
            RequestIssueVcResult result = RequestIssueVcResult.builder()
                .txId(request.getTxId())
                .e2e(apiResponse.getE2e())
                .build();

            log.info("P210 Step 6 completed - txId: {}", request.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P210 Step 6 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P210 Step 6 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to request issue VC: " + e.getMessage()
            );
        }
    }

    // =========================================================================
    // Step 7: Confirm Issue VC
    // =========================================================================

    /**
     * Step 7: Confirm Issue VC.
     *
     * <p>서버 토큰 검증, VC ID 검증 후 Issuer에게 완료를 통보한다.</p>
     */
    @Override
    public ConfirmIssueVcResult confirmIssueVc(
        ConfirmIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P210 Step 7: Confirm Issue VC ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate serverToken
            log.debug("Step 7.1: Validating serverToken");
            tokenProvider.validateServerToken(request.getServerToken(), request.getTxId());

            // 2. Validate VC ID
            log.debug("Step 7.2: Validating VC ID");
            if (request.getVcId() == null || request.getVcId().isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_ID_MISMATCH,
                    "VC ID is required"
                );
            }

            try {
                UUID.fromString(request.getVcId());
            } catch (IllegalArgumentException e) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_ID_MISMATCH,
                    "Invalid VC ID format: " + request.getVcId()
                );
            }

            // 3. Retrieve Issuer txId
            log.debug("Step 7.3: Retrieving Issuer txId");
            String externalTxId = dataProvider.getExternalTxId(request.getTxId());

            // 4. Call Issuer API: complete-vc
            log.debug("Step 7.4: Calling Issuer API - complete-vc");
            org.omnione.did.tas.model.request.issuer.CompleteVcApiRequest apiRequest =
                org.omnione.did.tas.model.request.issuer.CompleteVcApiRequest.builder()
                    .id(request.getId())
                    .txId(externalTxId)
                    .vcId(request.getVcId())
                    .build();

            try {
                issuerClient.completeVc(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_COMMUNICATION_FAILED,
                    "Failed to complete VC with Issuer: " + e.getMessage(),
                    e
                );
            }

            // 5. Build result
            ConfirmIssueVcResult result = ConfirmIssueVcResult.builder()
                .txId(request.getTxId())
                .build();

            log.info("P210 Step 7 completed - txId: {}", request.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P210 Step 7 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P210 Step 7 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to confirm issue VC: " + e.getMessage()
            );
        }
    }

    // =========================================================================
    // Private helper methods
    // =========================================================================

    /**
     * DID Auth를 완전히 검증한다.
     *
     * <p>검증 항목:</p>
     * <ol>
     *   <li>authNonce 일치 (transaction에 저장된 값과 비교)</li>
     *   <li>DID 형식 유효성</li>
     *   <li>proof null 검사</li>
     *   <li>verificationMethod 형식 유효성</li>
     *   <li>verificationMethod DID == didAuth.did</li>
     *   <li>proofPurpose == AUTHENTICATION</li>
     *   <li>사용자 DID Document 조회 → 공개키 추출</li>
     *   <li>서명 메시지 추출 → 서명 검증</li>
     * </ol>
     *
     * @param didAuth           검증할 DidAuth
     * @param expectedAuthNonce 거래에 저장된 authNonce
     */
    private void validateDidAuth(DidAuth didAuth, String expectedAuthNonce) {

        // 1. authNonce 비교
        if (didAuth.getAuthNonce() == null || !expectedAuthNonce.equals(didAuth.getAuthNonce())) {
            log.error("AuthNonce mismatch");
            throw new TaSdkException(TaSdkErrorCode.AUTH_NONCE_MISMATCH, "AuthNonce mismatch");
        }

        String clientDid = didAuth.getDid();

        // 2. DID 형식 유효성
        if (!DidValidator.isValidDid(clientDid)) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
                "Invalid DID format: " + clientDid);
        }

        // 3. proof null 검사
        Proof proof = didAuth.getProof();
        if (proof == null) {
            throw new TaSdkException(TaSdkErrorCode.PROOF_MISSING, "DidAuth proof is missing");
        }

        // 4. verificationMethod 형식 유효성
        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_VERIFICATION_METHOD,
                "Invalid DidAuth verificationMethod: " + verificationMethod);
        }

        // 5. verificationMethod DID == didAuth.did
        String didFromVm = DidUtil.extractDid(verificationMethod);
        if (!clientDid.equals(didFromVm)) {
            throw new TaSdkException(TaSdkErrorCode.DID_MISMATCH,
                "DidAuth DID does not match verificationMethod DID");
        }

        // 6. proofPurpose == AUTHENTICATION
        if (proof.getProofPurpose() != ProofPurpose.AUTHENTICATION) {
            throw new TaSdkException(TaSdkErrorCode.INVALID_PROOF_PURPOSE,
                "DidAuth proofPurpose must be authentication, got: " + proof.getProofPurpose());
        }

        // 7. 사용자 DID Document 조회 → 공개키 추출
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

        // 8. 서명 메시지 추출 → 서명 검증
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
}
