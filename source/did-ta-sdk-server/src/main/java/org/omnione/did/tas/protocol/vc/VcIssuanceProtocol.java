package org.omnione.did.tas.protocol.vc;

import org.omnione.did.tas.client.IssuerApiClient;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.vc.ProposeIssueVcRequest;
import org.omnione.did.tas.model.response.protocol.vc.ProposeIssueVcResult;
import org.omnione.did.tas.provider.VcIssuanceDataProvider;

/**
 * P210 VC Issuance Protocol interface.
 *
 * <p>This interface defines the contract for P210 VC Issuance Protocol.
 * The TAS SDK provides the implementation, and the TA Server calls these methods
 * to execute the protocol steps.</p>
 *
 * <p><b>Protocol Steps:</b></p>
 * <ul>
 *   <li>Step 1a/1b/1c: Offer Issue VC (Optional) - QR/Push/Email</li>
 *   <li>Step 2: Propose Issue VC</li>
 *   <li>Step 3: Request ECDH (Common component)</li>
 *   <li>Step 4: Request Create Token (Common component)</li>
 *   <li>Step 5: Request Issue Profile</li>
 *   <li>Step 6: Request Issue VC</li>
 *   <li>Step 7: Confirm Issue VC</li>
 * </ul>
 *
 * <p><b>Design Notes:</b></p>
 * <ul>
 *   <li>SDK handles business logic (when to call Issuer, validation, crypto)</li>
 *   <li>Server provides IssuerApiClient implementation (HTTP communication)</li>
 *   <li>Server provides DataProvider implementation (DB access)</li>
 *   <li>Server manages Transaction state (status, expiration, step sequencing)</li>
 * </ul>
 *
 * @see IssuerApiClient
 * @see VcIssuanceDataProvider
 */
public interface VcIssuanceProtocol {

    // ===== Core Protocol APIs =====

    /**
     * Step 2: Propose Issue VC.
     *
     * <p>Start VC issuance process and validate proposal with Issuer.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Validate VC Plan (via DataProvider)</li>
     *   <li>Check Issuer status (COMPLETED)</li>
     *   <li>Generate TAS txId</li>
     *   <li>Call Issuer API: inspect-propose-issue</li>
     *   <li>Return txId, externalTxId (Issuer txId), refId</li>
     * </ol>
     *
     * <p><b>Server responsibilities:</b></p>
     * <ul>
     *   <li>Save Transaction (txId, externalTxId, refId)</li>
     *   <li>Save SubTransaction (Step 2 completed)</li>
     * </ul>
     *
     * @param request Propose request (vcPlanId, issuer, offerId)
     * @param issuerClient Issuer API client (Server provides implementation)
     * @param dataProvider Data provider (Server provides implementation)
     * @return Propose result (txId, externalTxId, refId)
     * @throws TaSdkException if validation fails or Issuer communication fails
     */
    ProposeIssueVcResult proposeIssueVc(
        ProposeIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException;

    /**
     * Step 5: Request Issue Profile.
     *
     * <p>Request Issue Profile from Issuer and generate authNonce.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Retrieve Issuer txId and Holder info</li>
     *   <li>Call Issuer API: generate-issue-profile</li>
     *   <li>Generate authNonce (16-byte)</li>
     *   <li>Return authNonce and IssueProfile</li>
     * </ol>
     *
     * @param request Profile request
     * @param issuerClient Issuer API client
     * @param dataProvider Data provider
     * @return Profile result (authNonce, profile)
     * @throws TaSdkException if Issuer communication fails
     */
    org.omnione.did.tas.model.response.protocol.vc.RequestIssueProfileResult requestIssueProfile(
        org.omnione.did.tas.model.request.protocol.vc.RequestIssueProfileRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException;

    /**
     * Step 6: Request Issue VC.
     *
     * <p>Validate DID Auth and request VC issuance from Issuer.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Validate DID Auth (authNonce, signature)</li>
     *   <li>Call Issuer API: issue-vc</li>
     *   <li>Return E2E encrypted VC</li>
     * </ol>
     *
     * @param request Issue VC request (didAuth, accE2e, encReqVc)
     * @param issuerClient Issuer API client
     * @param dataProvider Data provider
     * @return Issue VC result (E2E encrypted VC)
     * @throws TaSdkException if DID Auth fails or Issuer communication fails
     */
    org.omnione.did.tas.model.response.protocol.vc.RequestIssueVcResult requestIssueVc(
        org.omnione.did.tas.model.request.protocol.vc.RequestIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException;

    /**
     * Step 7: Confirm Issue VC.
     *
     * <p>Notify Issuer that VC issuance is complete.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Validate VC ID</li>
     *   <li>Call Issuer API: complete-vc</li>
     * </ol>
     *
     * @param request Confirm request (vcId)
     * @param issuerClient Issuer API client
     * @param dataProvider Data provider
     * @return Confirm result
     * @throws TaSdkException if VC ID invalid or Issuer communication fails
     */
    org.omnione.did.tas.model.response.protocol.vc.ConfirmIssueVcResult confirmIssueVc(
        org.omnione.did.tas.model.request.protocol.vc.ConfirmIssueVcRequest request,
        IssuerApiClient issuerClient,
        VcIssuanceDataProvider dataProvider
    ) throws TaSdkException;

    // TODO: Add Offer steps (Step 1a, 1b, 1c) - Optional
}
