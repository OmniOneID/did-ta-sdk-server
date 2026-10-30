package org.omnione.did.tas.model.request.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Request: Generate Issue Profile.
 *
 * <p>POST /issuer/api/v1/generate-issue-profile</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateIssueProfileApiRequest {

    private String id;
    private String txId;
    private ApiHolder holder;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiHolder {
        private String did;
        private String pii;
    }
}
