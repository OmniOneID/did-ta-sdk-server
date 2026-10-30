package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.AccE2e;

/**
 * Issuer API Request: Issue VC.
 *
 * <p>POST /issuer/api/v1/issue-vc</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueVcApiRequest {

    private String id;
    private String txId;
    private AccE2e accE2e;
    private String encReqVc;
}
