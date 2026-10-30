package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P210 Step 5: Request Issue Profile Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssueProfileRequest {

    private String id;
    private String txId;
    private String serverToken;
}
