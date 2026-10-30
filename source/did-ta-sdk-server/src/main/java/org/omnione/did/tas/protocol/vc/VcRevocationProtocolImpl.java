package org.omnione.did.tas.protocol.vc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.tas.client.IssuerApiClient;
import org.omnione.did.tas.exception.IssuerApiException;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.data.ReqRevokeVc;
import org.omnione.did.tas.model.domain.VcMeta;
import org.omnione.did.tas.model.request.issuer.CompleteRevokeApiRequest;
import org.omnione.did.tas.model.request.issuer.InspectProposeRevokeApiRequest;
import org.omnione.did.tas.model.request.issuer.RevokeVcApiRequest;
import org.omnione.did.tas.model.request.protocol.vc.ConfirmRevokeVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.ProposeRevokeVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.RequestRevokeVcRequest;
import org.omnione.did.tas.model.response.issuer.InspectProposeRevokeApiResponse;
import org.omnione.did.tas.model.response.protocol.vc.ConfirmRevokeVcResult;
import org.omnione.did.tas.model.response.protocol.vc.ProposeRevokeVcResult;
import org.omnione.did.tas.model.response.protocol.vc.RequestRevokeVcResult;
import org.omnione.did.tas.provider.VcRevocationDataProvider;

import java.util.UUID;

/**
 * P220 VC Revocation Protocol implementation.
 *
 * <p>Implements the VC Revocation Protocol for P220.
 * This class handles the business logic for VC revocation, delegating
 * Issuer API communication to {@link IssuerApiClient} and data access
 * to {@link VcRevocationDataProvider}.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class VcRevocationProtocolImpl implements VcRevocationProtocol {

    // =========================================================================
    // Step 1: Propose Revoke VC
    // =========================================================================

    /**
     * Step 1: Propose Revoke VC.
     *
     * <p>Validates VC metadata from blockchain, checks VC status,
     * and calls Issuer API to validate the revocation proposal.</p>
     */
    @Override
    public ProposeRevokeVcResult proposeRevokeVc(
        ProposeRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P220 Step 1: Propose Revoke VC ===");
        log.debug("VC ID: {}", request.getVcId());

        try {
            // 1. Generate TAS txId
            log.debug("Step 1.1: Generating TAS txId");
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            // 2. Retrieve VC metadata from blockchain
            log.debug("Step 1.2: Retrieving VC metadata from blockchain");
            VcMeta vcMeta = dataProvider.getVcMeta(request.getVcId());
            if (vcMeta == null) {
                throw new TaSdkException(
                    TaSdkErrorCode.VC_META_FETCH_FAILED,
                    "VC metadata not found: " + request.getVcId()
                );
            }
            log.debug("VC metadata retrieved - Issuer: {}, Status: {}", vcMeta.getIssuer(), vcMeta.getStatus());

            // 3. Validate VC status
            log.debug("Step 1.3: Validating VC status");
            validateVcStatus(vcMeta);

            // 4. Extract Issuer DID
            String issuerDid = vcMeta.getIssuer();
            if (issuerDid == null || issuerDid.isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.ISSUER_NOT_FOUND,
                    "Issuer DID not found in VC metadata"
                );
            }

            // 5. Call Issuer API: inspect-propose-revoke
            log.debug("Step 1.4: Calling Issuer API - inspect-propose-revoke");
            InspectProposeRevokeApiRequest apiRequest = InspectProposeRevokeApiRequest.builder()
                .id(request.getId())
                .vcId(request.getVcId())
                .build();

            InspectProposeRevokeApiResponse apiResponse;
            try {
                apiResponse = issuerClient.inspectProposeRevoke(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.PROPOSE_REVOKE_VC_FAILED,
                    "Failed to inspect propose revoke from Issuer: " + e.getMessage(),
                    e
                );
            }

            // 6. Validate response
            if (apiResponse.getIssuerNonce() == null || apiResponse.getIssuerNonce().isEmpty()) {
                throw new TaSdkException(
                    TaSdkErrorCode.PROPOSE_REVOKE_VC_FAILED,
                    "Issuer returned null or empty issuerNonce"
                );
            }

            // 7. Build result
            ProposeRevokeVcResult result = ProposeRevokeVcResult.builder()
                .txId(txId)
                .issuerNonce(apiResponse.getIssuerNonce())
                .authType(apiResponse.getAuthType())
                .build();

            log.info("P220 Step 1 completed - txId: {}", txId);
            return result;

        } catch (TaSdkException e) {
            log.error("P220 Step 1 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P220 Step 1 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to propose revoke VC: " + e.getMessage()
            );
        }
    }

    /**
     * Validate VC status (must be ACTIVE).
     */
    private void validateVcStatus(VcMeta vcMeta) throws TaSdkException {
        String status = vcMeta.getStatus();

        if ("REVOKED".equals(status)) {
            throw new TaSdkException(
                TaSdkErrorCode.VC_ALREADY_REVOKED,
                "VC is already revoked: " + vcMeta.getVcId()
            );
        }

        if ("EXPIRED".equals(status)) {
            throw new TaSdkException(
                TaSdkErrorCode.VC_EXPIRED,
                "VC is expired: " + vcMeta.getVcId()
            );
        }

        if (!"ACTIVE".equals(status)) {
            throw new TaSdkException(
                TaSdkErrorCode.VC_STATUS_INVALID,
                "VC status is not ACTIVE: " + status
            );
        }
    }

    // =========================================================================
    // Step 4: Request Revoke VC
    // =========================================================================

    /**
     * Step 4: Request Revoke VC.
     */
    @Override
    public RequestRevokeVcResult requestRevokeVc(
        RequestRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P220 Step 4: Request Revoke VC ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Validate ReqRevokeVc
            log.debug("Step 4.1: Validating ReqRevokeVc");
            validateReqRevokeVc(request.getRequest(), request.getTxId(), dataProvider);

            // 2. Validate PIN signature
            log.debug("Step 4.2: Validating PIN signature");
            validatePinSignature(request.getRequest());

            // 3. Call Issuer API: revoke-vc
            log.debug("Step 4.3: Calling Issuer API - revoke-vc");
            RevokeVcApiRequest apiRequest = RevokeVcApiRequest.builder()
                .id(request.getId())
                .reqRevokeVc(request.getRequest())
                .build();

            try {
                issuerClient.revokeVc(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.REQUEST_REVOKE_VC_FAILED,
                    "Failed to revoke VC from Issuer: " + e.getMessage(),
                    e
                );
            }

            // 4. Build result
            RequestRevokeVcResult result = RequestRevokeVcResult.builder()
                .txId(request.getTxId())
                .build();

            log.info("P220 Step 4 completed - txId: {}", request.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P220 Step 4 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P220 Step 4 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to request revoke VC: " + e.getMessage()
            );
        }
    }

    /**
     * Validate ReqRevokeVc (vcId and issuerNonce match).
     */
    private void validateReqRevokeVc(
        ReqRevokeVc reqRevokeVc,
        String txId,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException {

        // Validate vcId match
        String expectedVcId = dataProvider.getVcId(txId);
        if (!reqRevokeVc.getVcId().equals(expectedVcId)) {
            log.error("VC ID mismatch: expected={}, actual={}", expectedVcId, reqRevokeVc.getVcId());
            throw new TaSdkException(
                TaSdkErrorCode.VC_ID_MISMATCH,
                "VC ID mismatch: expected=" + expectedVcId + ", actual=" + reqRevokeVc.getVcId()
            );
        }

        // Validate issuerNonce match
        String expectedIssuerNonce = dataProvider.getIssuerNonce(txId);
        if (!reqRevokeVc.getIssuerNonce().equals(expectedIssuerNonce)) {
            log.error("Issuer nonce mismatch: expected={}, actual={}",
                expectedIssuerNonce, reqRevokeVc.getIssuerNonce());
            throw new TaSdkException(
                TaSdkErrorCode.ISSUER_NONCE_MISMATCH,
                "Issuer nonce mismatch"
            );
        }
    }

    /**
     * Validate PIN signature.
     *
     * <p>TODO: Implement full PIN signature verification.
     * Currently validates structure only.</p>
     */
    private void validatePinSignature(ReqRevokeVc reqRevokeVc) throws TaSdkException {
        if (reqRevokeVc.getProof() == null) {
            throw new TaSdkException(
                TaSdkErrorCode.SIGNATURE_INVALID,
                "PIN signature (proof) is required"
            );
        }

        // Validate proof structure
        if (reqRevokeVc.getProof().getType() == null ||
            reqRevokeVc.getProof().getVerificationMethod() == null ||
            reqRevokeVc.getProof().getProofPurpose() == null ||
            reqRevokeVc.getProof().getProofValue() == null) {
            throw new TaSdkException(
                TaSdkErrorCode.SIGNATURE_INVALID,
                "Invalid proof structure"
            );
        }

        // Validate proof purpose is assertionMethod
        if (reqRevokeVc.getProof().getProofPurpose() != ProofPurpose.ASSERTION_METHOD) {
            throw new TaSdkException(
                TaSdkErrorCode.SIGNATURE_INVALID,
                "Proof purpose must be 'assertionMethod' for PIN signature, but was: " +
                reqRevokeVc.getProof().getProofPurpose()
            );
        }

        // TODO: Full signature verification with DID Document resolution
        log.debug("PIN signature validated successfully (structure check only)");
    }

    // =========================================================================
    // Step 5: Confirm Revoke VC
    // =========================================================================

    /**
     * Step 5: Confirm Revoke VC.
     */
    @Override
    public ConfirmRevokeVcResult confirmRevokeVc(
        ConfirmRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException {

        log.debug("=== P220 Step 5: Confirm Revoke VC ===");
        log.debug("TxId: {}", request.getTxId());

        try {
            // 1. Call Issuer API: complete-revoke
            log.debug("Step 5.1: Calling Issuer API - complete-revoke");
            CompleteRevokeApiRequest apiRequest = CompleteRevokeApiRequest.builder()
                .id(request.getId())
                .txId(request.getTxId())
                .build();

            try {
                issuerClient.completeRevoke(apiRequest);
            } catch (IssuerApiException e) {
                log.error("Issuer API call failed: {}", e.getMessage());
                throw new TaSdkException(
                    TaSdkErrorCode.CONFIRM_REVOKE_VC_FAILED,
                    "Failed to complete revoke from Issuer: " + e.getMessage(),
                    e
                );
            }

            // 2. Build result
            ConfirmRevokeVcResult result = ConfirmRevokeVcResult.builder()
                .txId(request.getTxId())
                .build();

            log.info("P220 Step 5 completed - txId: {}", request.getTxId());
            return result;

        } catch (TaSdkException e) {
            log.error("P220 Step 5 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P220 Step 5 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(
                TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to confirm revoke VC: " + e.getMessage()
            );
        }
    }
}
