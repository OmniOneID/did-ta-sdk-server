package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Holder information domain model.
 *
 * <p>Represents Holder (user) information needed for VC issuance.
 * Used in P210 Step 5 (Request Issue Profile) to provide Holder details
 * to the Issuer for generating the Issue Profile.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 * {@code
 * HolderInfo holderInfo = HolderInfo.builder()
 *     .did("did:omn:user:12345")
 *     .pii("z...base64EncodedCI...")
 *     .build();
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolderInfo {

    /**
     * Holder DID.
     *
     * <p>Example: "did:omn:user:12345"</p>
     */
    private String did;

    /**
     * PII (Personally Identifiable Information).
     *
     * <p>CI (Connecting Information) value from KYC verification,
     * multibase-encoded.</p>
     */
    private String pii;
}
