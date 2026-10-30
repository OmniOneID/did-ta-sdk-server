package org.omnione.did.tas.model.response.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Response: Inspect Propose Issue.
 *
 * <p>Response from POST /issuer/api/v1/inspect-propose-issue</p>
 *
 * <p>Used in P210 Step 2.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectIssueProposeApiResponse {

    /**
     * Issuer Transaction ID.
     */
    private String txId;

    /**
     * Reference ID.
     *
     * <p>Issuer-provided reference number for this issuance.</p>
     */
    private String refId;
}
