package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P220 Step 1: Propose Revoke VC Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeRevokeVcResult {

    /**
     * Transaction ID
     */
    private String txId;

    /**
     * Issuer Nonce (16-byte, multibase-encoded)
     */
    private String issuerNonce;

    /**
     * Authentication type required for revocation
     * (1: PIN, 2: BIO, 3: PIN_OR_BIO, 4: PIN_AND_BIO)
     */
    private Integer authType;
}
