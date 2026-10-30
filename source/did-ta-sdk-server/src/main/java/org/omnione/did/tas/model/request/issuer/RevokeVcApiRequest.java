package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.ReqRevokeVc;

/**
 * Issuer API: Revoke VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevokeVcApiRequest {

    /**
     * Request ID (optional)
     */
    private String id;

    /**
     * VC Revocation Request Information
     */
    private ReqRevokeVc reqRevokeVc;
}
