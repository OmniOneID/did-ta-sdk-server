package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Request: Inspect Propose Issue.
 *
 * <p>POST /issuer/api/v1/inspect-propose-issue</p>
 *
 * <p>Used in P210 Step 2 to validate VC issuance proposal with Issuer.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectIssueProposeApiRequest {

    /**
     * Message ID.
     */
    private String id;

    /**
     * VC Plan ID.
     */
    private String vcPlanId;

    /**
     * Issuer DID.
     */
    private String issuer;

    /**
     * Offer ID (optional).
     *
     * <p>Provided if user is responding to an Offer.</p>
     */
    private String offerId;
}
