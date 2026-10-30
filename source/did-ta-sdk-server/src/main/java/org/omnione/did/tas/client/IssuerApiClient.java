package org.omnione.did.tas.client;

import org.omnione.did.tas.exception.IssuerApiException;
import org.omnione.did.tas.model.request.issuer.CompleteRevokeApiRequest;
import org.omnione.did.tas.model.request.issuer.CompleteVcApiRequest;
import org.omnione.did.tas.model.request.issuer.GenerateIssueProfileApiRequest;
import org.omnione.did.tas.model.request.issuer.InspectProposeRevokeApiRequest;
import org.omnione.did.tas.model.request.issuer.InspectIssueProposeApiRequest;
import org.omnione.did.tas.model.request.issuer.IssueVcApiRequest;
import org.omnione.did.tas.model.request.issuer.OfferIssueVcApiRequest;
import org.omnione.did.tas.model.request.issuer.RevokeVcApiRequest;
import org.omnione.did.tas.model.response.issuer.CompleteRevokeApiResponse;
import org.omnione.did.tas.model.response.issuer.CompleteVcApiResponse;
import org.omnione.did.tas.model.response.issuer.GenerateIssueProfileApiResponse;
import org.omnione.did.tas.model.response.issuer.InspectProposeRevokeApiResponse;
import org.omnione.did.tas.model.response.issuer.InspectIssueProposeApiResponse;
import org.omnione.did.tas.model.response.issuer.IssueVcApiResponse;
import org.omnione.did.tas.model.response.issuer.OfferIssueVcApiResponse;
import org.omnione.did.tas.model.response.issuer.RevokeVcApiResponse;

/**
 * Issuer API Client interface for P210 VC Issuance and P220 VC Revocation Protocols.
 *
 * <p>This interface defines the contract for communicating with an Issuer server.
 * The TA Server implements this interface to provide actual HTTP communication logic
 * (e.g., using RestTemplate, WebClient, or OpenFeign).</p>
 *
 * <p><b>Design Pattern:</b> Dependency Injection - SDK defines the interface,
 * Server provides the implementation.</p>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Component
 * public class IssuerHttpClient implements IssuerApiClient {
 *
 *     private final String issuerServerUrl;
 *     private final RestTemplate restTemplate;
 *
 *     public IssuerHttpClient(String issuerServerUrl) {
 *         this.issuerServerUrl = issuerServerUrl;
 *         this.restTemplate = new RestTemplate();
 *     }
 *
 *     @Override
 *     public InspectIssueProposeApiResponse inspectProposeIssue(
 *         InspectIssueProposeApiRequest request
 *     ) throws IssuerApiException {
 *         String url = issuerServerUrl + "/issuer/api/v1/inspect-propose-issue";
 *         try {
 *             return restTemplate.postForObject(url, request, InspectIssueProposeApiResponse.class);
 *         } catch (Exception e) {
 *             throw new IssuerApiException("Issuer communication failed", e);
 *         }
 *     }
 *
 *     // ... other methods
 * }
 * }
 * </pre>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P210 Step 1a/1b/1c - Offer Issue VC</li>
 *   <li>P210 Step 2 - Propose Issue VC (inspect-propose-issue)</li>
 *   <li>P210 Step 5 - Request Issue Profile (generate-issue-profile)</li>
 *   <li>P210 Step 6 - Request Issue VC (issue-vc)</li>
 *   <li>P210 Step 7 - Confirm Issue VC (complete-vc)</li>
 *   <li>P220 Step 1 - Propose Revoke VC (inspect-propose-revoke)</li>
 *   <li>P220 Step 4 - Request Revoke VC (revoke-vc)</li>
 *   <li>P220 Step 5 - Confirm Revoke VC (complete-revoke)</li>
 * </ul>
 */
public interface IssuerApiClient {

    /**
     * POST /issuer/api/v1/offer-issue-vc
     *
     * <p>Request VC issuance offer from Issuer.</p>
     *
     * <p>Used in Step 1a (QR), Step 1b (Push), Step 1c (Email).</p>
     *
     * @param request Offer request
     * @return Offer response (offerId, validUntil)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    OfferIssueVcApiResponse offerIssueVc(OfferIssueVcApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/inspect-propose-issue
     *
     * <p>Validate VC issuance proposal with Issuer.</p>
     *
     * <p>Used in Step 2 (Propose Issue VC).</p>
     *
     * @param request Inspect propose request
     * @return Inspect propose response (txId, refId)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    InspectIssueProposeApiResponse inspectProposeIssue(InspectIssueProposeApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/generate-issue-profile
     *
     * <p>Request Issue Profile from Issuer.</p>
     *
     * <p>Issue Profile contains:</p>
     * <ul>
     *   <li>VC Schema - structure and required claims</li>
     *   <li>E2E encryption request (reqE2e)</li>
     *   <li>Process information</li>
     * </ul>
     *
     * <p>Used in Step 5 (Request Issue Profile).</p>
     *
     * @param request Generate profile request
     * @return Generate profile response (IssueProfile)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    GenerateIssueProfileApiResponse generateIssueProfile(GenerateIssueProfileApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/issue-vc
     *
     * <p>Request VC issuance from Issuer.</p>
     *
     * <p>Request contains:</p>
     * <ul>
     *   <li>accE2e - E2E encryption acceptance</li>
     *   <li>encReqVc - Encrypted VP (Verifiable Presentation) with holder claims</li>
     * </ul>
     *
     * <p>Response contains:</p>
     * <ul>
     *   <li>e2e.iv - Encryption IV</li>
     *   <li>e2e.encVc - E2E encrypted VC (only Wallet can decrypt)</li>
     * </ul>
     *
     * <p>Used in Step 6 (Request Issue VC).</p>
     *
     * @param request Issue VC request
     * @return Issue VC response (E2E encrypted VC)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    IssueVcApiResponse issueVc(IssueVcApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/complete-vc
     *
     * <p>Notify Issuer that VC issuance is complete.</p>
     *
     * <p>Used in Step 7 (Confirm Issue VC).</p>
     *
     * @param request Complete VC request
     * @return Complete VC response (empty)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    CompleteVcApiResponse completeVc(CompleteVcApiRequest request)
            throws IssuerApiException;

    // ===== P220 VC Revocation Protocol APIs =====

    /**
     * POST /issuer/api/v1/inspect-propose-revoke
     *
     * <p>Validate VC revocation proposal with Issuer.</p>
     *
     * <p>Used in P220 Step 1 (Propose Revoke VC).</p>
     *
     * @param request Inspect propose revoke request
     * @return Inspect propose revoke response (issuerNonce, authType)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    InspectProposeRevokeApiResponse inspectProposeRevoke(InspectProposeRevokeApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/revoke-vc
     *
     * <p>Request VC revocation from Issuer.</p>
     *
     * <p>Used in P220 Step 4 (Request Revoke VC).</p>
     *
     * @param request Revoke VC request
     * @return Revoke VC response (empty)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    RevokeVcApiResponse revokeVc(RevokeVcApiRequest request)
            throws IssuerApiException;

    /**
     * POST /issuer/api/v1/complete-revoke
     *
     * <p>Notify Issuer that VC revocation is complete.</p>
     *
     * <p>Used in P220 Step 5 (Confirm Revoke VC).</p>
     *
     * @param request Complete revoke request
     * @return Complete revoke response (empty)
     * @throws IssuerApiException if communication fails or Issuer returns error
     */
    CompleteRevokeApiResponse completeRevoke(CompleteRevokeApiRequest request)
            throws IssuerApiException;

    /**
     * Get Issuer server base URL.
     *
     * @return Issuer server base URL (e.g., "https://issuer.example.com")
     */
    String getIssuerServerUrl();
}
