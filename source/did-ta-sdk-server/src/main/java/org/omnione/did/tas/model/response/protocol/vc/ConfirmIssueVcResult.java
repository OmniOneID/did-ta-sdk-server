package org.omnione.did.tas.model.response.protocol.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P210 Step 7: Confirm Issue VC Result.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmIssueVcResult {

    private String txId;
}
