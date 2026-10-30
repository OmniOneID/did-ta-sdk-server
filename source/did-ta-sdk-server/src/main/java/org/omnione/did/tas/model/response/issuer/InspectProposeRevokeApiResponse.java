package org.omnione.did.tas.model.response.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API: Inspect Propose Revoke Response.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectProposeRevokeApiResponse {

    /**
     * Issuer Nonce (16-byte, multibase-encoded)
     */
    private String issuerNonce;

    /**
     * Authentication type required
     * (1: PIN, 2: BIO, 3: PIN_OR_BIO, 4: PIN_AND_BIO)
     */
    private Integer authType;
}
