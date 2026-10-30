package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API: Complete Revoke Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteRevokeApiRequest {

    /**
     * Request ID (optional)
     */
    private String id;

    /**
     * Transaction ID
     */
    private String txId;
}
