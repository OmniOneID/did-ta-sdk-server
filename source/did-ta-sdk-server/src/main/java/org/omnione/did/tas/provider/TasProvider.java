package org.omnione.did.tas.provider;

import org.omnione.did.tas.model.domain.Tas;
import org.omnione.did.tas.model.enums.TasStatus;

/**
 * Provider interface for TAS information lookup (read-only).
 *
 * <p>The TA Server implements this interface to provide TAS entity information
 * to the SDK. The SDK uses this to validate TAS state and retrieve registration
 * credentials.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P110 TAS 등록 - validates password, checks TAS status, retrieves TAS info</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TasProviderImpl implements TasProvider {
 *     @Override
 *     public Tas getTasInfo() {
 *         return tasRepository.findFirst()
 *             .map(TasEntity::toDomain)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.SDK_NOT_INITIALIZED));
 *     }
 *     @Override
 *     public String getTasPassword() {
 *         return taAuthProperty.getAuth().getRegistrationPassword();
 *     }
 * }
 * }
 * </pre>
 */
public interface TasProvider {

    /**
     * Retrieve TAS information.
     *
     * @return TAS domain object (did, name, status, subjectDn, version)
     * @throws org.omnione.did.tas.exception.TaSdkException if TAS info not found
     */
    Tas getTasInfo();

    /**
     * Retrieve the TAS registration password.
     *
     * <p>Returns the pre-distributed registration password stored in server
     * configuration (environment variable or Vault). The password is used
     * to authorize P110 TAS self-enrollment.</p>
     *
     * @return TAS registration password, or null if not configured
     */
    String getTasPassword();

    /**
     * Retrieve current TAS status.
     *
     * @return TAS status (PENDING or COMPLETED)
     */
    TasStatus getTasStatus();

    /**
     * Retrieve TAS DID.
     *
     * @return TAS DID string (e.g., "did:omn:tas")
     */
    String getTasDid();
}
