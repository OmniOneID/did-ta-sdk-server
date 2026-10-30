package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.AccE2e;
import org.omnione.did.tas.model.data.DidAuth;

/**
 * P210 Step 6: Request Issue VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssueVcRequest {

    private String id;
    private String txId;
    private String serverToken;
    private DidAuth didAuth;
    private AccE2e accE2e;
    private String encReqVc;  // Encrypted VP
}
