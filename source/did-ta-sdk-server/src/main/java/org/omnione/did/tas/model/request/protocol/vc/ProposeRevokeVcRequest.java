package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P220 Step 1: Propose Revoke VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeRevokeVcRequest {

    /**
     * Request ID (optional, for tracking)
     */
    private String id;

    /**
     * VC ID to revoke
     */
    private String vcId;
}
