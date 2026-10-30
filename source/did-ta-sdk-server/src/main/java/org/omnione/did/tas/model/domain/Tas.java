package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.enums.TasStatus;

/**
 * TAS domain model.
 *
 * <p>Represents the TAS (Trust Agent Service) entity as seen by the SDK.
 * The server maps its database entity to this domain object before
 * passing it to the SDK via {@link org.omnione.did.tas.provider.TasProvider}.</p>
 *
 * <p><b>Server-side mapping example:</b></p>
 * <pre>
 * {@code
 * Tas.builder()
 *     .did(entity.getDid())
 *     .name(entity.getName())
 *     .status(TasStatus.valueOf(entity.getStatus().name()))
 *     .subjectDn(String.format("o=%s", entity.getName()))
 *     .version(entity.getVersion())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tas {

    /**
     * TAS DID (e.g., "did:omn:tas").
     */
    private String did;

    /**
     * TAS name (e.g., "TrustAgentService").
     */
    private String name;

    /**
     * TAS registration status.
     */
    private TasStatus status;

    /**
     * Subject DN for Certificate VC.
     *
     * <p>Used as the {@code org.opendid.v1.subject} claim in the Certificate VC.
     * Typically formatted as "o={name}" or a full DN string like
     * "cn=TrustAgent,dc=example,dc=com".</p>
     */
    private String subjectDn;

    /**
     * TAS version string (e.g., "1.0.0").
     */
    private String version;
}
