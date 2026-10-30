package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API: Inspect Propose Revoke Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectProposeRevokeApiRequest {

    /**
     * Request ID (optional)
     */
    private String id;

    /**
     * VC ID to revoke
     */
    private String vcId;
}
