package org.omnione.did.tas.model.response.issuer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Issuer API Response: Generate Issue Profile.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateIssueProfileApiResponse {

    private Object profile;  // IssueProfile - 복잡한 구조이므로 일단 Object로
}
