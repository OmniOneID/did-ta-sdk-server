package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P220 Step 5: Confirm Revoke VC Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRevokeVcResult {

    /**
     * Transaction ID
     */
    private String txId;
}
