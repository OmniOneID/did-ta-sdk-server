package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Request: Offer Issue VC.
 *
 * <p>POST /issuer/api/v1/offer-issue-vc</p>
 *
 * <p>Used in P210 Step 1a/1b/1c.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferIssueVcApiRequest {

    private String id;
    private String vcPlanId;
    private String issuer;
    private String holder;  // For Push/Email
    private String email;   // For Email
}
