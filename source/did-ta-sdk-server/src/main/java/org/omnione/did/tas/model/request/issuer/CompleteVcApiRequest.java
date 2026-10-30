package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Request: Complete VC.
 *
 * <p>POST /issuer/api/v1/complete-vc</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteVcApiRequest {

    private String id;
    private String txId;
    private String vcId;
}
