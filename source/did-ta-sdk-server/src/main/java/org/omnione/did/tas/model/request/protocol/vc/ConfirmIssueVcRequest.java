package org.omnione.did.tas.model.request.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P210 Step 7: Confirm Issue VC Request.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmIssueVcRequest {

    private String id;
    private String txId;
    private String serverToken;
    private String vcId;
}
