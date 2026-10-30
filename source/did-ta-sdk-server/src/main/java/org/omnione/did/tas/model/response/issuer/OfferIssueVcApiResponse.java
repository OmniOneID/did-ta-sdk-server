package org.omnione.did.tas.model.response.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Response: Offer Issue VC.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferIssueVcApiResponse {

    private String offerId;
    private String validUntil;
}
