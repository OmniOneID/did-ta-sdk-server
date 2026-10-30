package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P210 Step 2: Propose Issue VC Request.
 *
 * <p>Request to start VC issuance process.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeIssueVcRequest {

    /**
     * Message ID.
     */
    private String id;

    /**
     * VC Plan ID (required).
     */
    private String vcPlanId;

    /**
     * Issuer DID (optional).
     *
     * <p>If not provided, SDK will retrieve from VC Plan.</p>
     */
    private String issuer;

    /**
     * Offer ID (optional).
     *
     * <p>Provided if user is responding to an Offer.</p>
     */
    private String offerId;
}
