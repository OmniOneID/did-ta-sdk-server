package org.omnione.did.tas.model.response.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.E2e;

/**
 * Issuer API Response: Issue VC.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueVcApiResponse {

    private E2e e2e;
}
