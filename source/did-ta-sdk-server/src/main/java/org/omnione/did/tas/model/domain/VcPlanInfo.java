package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * VC Plan information domain model.
 *
 * <p>Represents VC Plan configuration as seen by the SDK.
 * The server maps its database entity to this domain object before
 * passing it to the SDK via {@link org.omnione.did.tas.provider.VcIssuanceDataProvider}.</p>
 *
 * <p><b>SDK usage:</b> The SDK uses this object to retrieve VC Plan metadata
 * and the associated Issuer DID for validation.</p>
 *
 * <p><b>Server-side mapping example:</b></p>
 * <pre>
 * {@code
 * VcPlanInfo.builder()
 *     .vcPlanId(entity.getVcPlanId())
 *     .name(entity.getName())
 *     .issuerDid(entity.getIssuerDid())
 *     .vcSchemaId(entity.getVcSchemaId())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VcPlanInfo {

    /**
     * VC Plan ID.
     *
     * <p>Unique identifier for the VC Plan.</p>
     */
    private String vcPlanId;

    /**
     * VC Plan name.
     *
     * <p>Human-readable name (e.g., "National ID Card", "Driver License").</p>
     */
    private String name;

    /**
     * Issuer DID.
     *
     * <p>The DID of the Issuer who provides this VC type.</p>
     */
    private String issuerDid;

    /**
     * VC Schema ID (optional).
     *
     * <p>Reference to the VC Schema that defines the structure of the VC.</p>
     */
    private String vcSchemaId;
}
