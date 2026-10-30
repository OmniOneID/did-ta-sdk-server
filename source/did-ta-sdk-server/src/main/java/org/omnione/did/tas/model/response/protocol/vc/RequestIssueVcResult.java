package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.E2e;

/**
 * P210 Step 6: Request Issue VC Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestIssueVcResult {

    private String txId;
    private E2e e2e;  // E2E encrypted VC
}
