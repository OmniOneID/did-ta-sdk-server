package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P220 Step 4: Request Revoke VC Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRevokeVcResult {

    /**
     * Transaction ID
     */
    private String txId;
}
