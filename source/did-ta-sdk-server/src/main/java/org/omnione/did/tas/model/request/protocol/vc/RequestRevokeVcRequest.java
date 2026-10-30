package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.ReqRevokeVc;

/**
 * P220 Step 4: Request Revoke VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRevokeVcRequest {

    /**
     * Request ID (optional, for tracking)
     */
    private String id;

    /**
     * Transaction ID
     */
    private String txId;

    /**
     * Server Token (multibase-encoded)
     */
    private String serverToken;

    /**
     * VC Revocation Request Information
     */
    private ReqRevokeVc request;
}
