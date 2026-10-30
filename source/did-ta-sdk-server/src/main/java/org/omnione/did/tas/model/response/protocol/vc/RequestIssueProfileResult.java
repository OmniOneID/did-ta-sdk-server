package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P210 Step 5: Request Issue Profile Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssueProfileResult {

    private String txId;
    private String authNonce;
    private Object profile;  // IssueProfile
}
