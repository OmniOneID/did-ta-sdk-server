package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P220 Step 5: Confirm Revoke VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRevokeVcRequest {

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
}
