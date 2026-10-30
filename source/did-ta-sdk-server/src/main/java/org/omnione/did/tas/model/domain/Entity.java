package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.data.model.enums.vc.RoleType;

/**
 * Entity domain model.
 *
 * <p>Represents an Entity (Issuer/Verifier) as seen by the SDK.
 * The server maps its database entity to this domain object before
 * passing it to the SDK via {@link org.omnione.did.tas.provider.EntityProvider}.</p>
 *
 * <p><b>Supported roles:</b></p>
 * <ul>
 *   <li>{@link RoleType#ISSUER} - Issues Verifiable Credentials</li>
 *   <li>{@link RoleType#VERIFIER} - Verifies Verifiable Presentations</li>
 * </ul>
 *
 * <p><b>Server-side mapping example:</b></p>
 * <pre>
 * {@code
 * Entity.builder()
 *     .did(entityEntity.getDid())
 *     .name(entityEntity.getName())
 *     .role(RoleType.valueOf(entityEntity.getRole().name()))
 *     .dn(entityEntity.getDn())
 *     .certVcRef(entityEntity.getCertVcRef())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    /**
     * Entity DID (e.g., "did:omn:issuer:12345").
     */
    private String did;

    /**
     * Entity name (e.g., "National ID Issuer").
     */
    private String name;

    /**
     * Entity role (Issuer or Verifier).
     */
    private RoleType role;

    /**
     * Distinguished Name (DN) for Certificate VC.
     *
     * <p>Optional field set during P120 Step 3. Used as the
     * {@code org.opendid.v1.subject} claim in the Entity's Certificate VC.</p>
     *
     * <p>Example: "cn=National ID Issuer,o=Government,c=KR"</p>
     */
    private String dn;

    /**
     * Certificate VC reference URL.
     *
     * <p>Public URL where the Entity's Certificate VC can be retrieved.
     * Set after P120 Step 3 (Certificate VC issuance).</p>
     */
    private String certVcRef;
}
