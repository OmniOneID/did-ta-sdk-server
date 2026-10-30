package org.omnione.did.tas.provider;

import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.tas.model.domain.Entity;

/**
 * Provider interface for Entity lookup (read-only).
 *
 * <p>The TA Server implements this interface to provide Entity information
 * to the SDK. The SDK uses this to validate entity state and retrieve
 * entity details during enrollment.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Read-Only Principle:</b></p>
 * <ul>
 *   <li>SDK reads entity info (getEntityByDid, existsEntityByDid)</li>
 *   <li>Server writes entity state (saveEntity, updateEntityStatus)</li>
 *   <li>Clear separation of concerns</li>
 * </ul>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P120 Entity Registration - retrieves entity info for Certificate VC generation</li>
 *   <li>P131 Wallet Registration - validates Wallet Provider eligibility</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class EntityProviderImpl implements EntityProvider {
 *
 *     @Override
 *     public Entity getEntityByDid(String did) {
 *         EntityEntity entity = entityRepository.findByDid(did)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.ENTITY_NOT_FOUND));
 *         return entity.toDomain();
 *     }
 *
 *     @Override
 *     public boolean existsEntityByDid(String did) {
 *         return entityRepository.existsByDid(did);
 *     }
 * }
 * }
 * </pre>
 */
public interface EntityProvider {

    /**
     * Retrieve entity by DID.
     *
     * <p>This is the primary lookup method used in P120 Step 3.
     * The SDK needs the entity's name, role, and DN to generate the Certificate VC.</p>
     *
     * @param did Entity DID (e.g., "did:omn:issuer:12345")
     * @return Entity domain object
     * @throws org.omnione.did.tas.exception.TaSdkException if entity not found (ENTITY_NOT_FOUND)
     */
    Entity getEntityByDid(String did);

    /**
     * Retrieve entity by role.
     *
     * <p>Useful for finding a specific Issuer or Verifier when DID is unknown.
     * If multiple entities have the same role, returns the first one found.</p>
     *
     * @param roleType Entity role (ISSUER or VERIFIER)
     * @return Entity domain object, or null if not found
     */
    Entity getEntityByRole(RoleType roleType);

    /**
     * Check if an entity exists by DID.
     *
     * <p>Lightweight query that only checks existence without loading the full entity.</p>
     *
     * @param did Entity DID
     * @return true if entity exists
     */
    boolean existsEntityByDid(String did);

    /**
     * Validate that the entity is in an eligible state for protocol operations.
     *
     * <p>The server implements this method with its own business logic to determine
     * whether the entity's current state allows participation in a protocol.
     * The SDK does not assume a specific status value (e.g., "COMPLETED") —
     * the server decides what constitutes a valid state.</p>
     *
     * <p><b>Used by:</b></p>
     * <ul>
     *   <li>P131 Wallet Registration - checks Wallet Provider is in a valid operational state</li>
     * </ul>
     *
     * <p><b>Server-side implementation example:</b></p>
     * <pre>
     * {@code
     * @Override
     * public void validateEntityStatus(String did) {
     *     EntityEntity entity = entityRepository.findByDid(did)
     *         .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.SDK_ENTITY_NOT_FOUND));
     *     if (!entity.isEligible()) {
     *         throw new TaSdkException(TaSdkErrorCode.ENTITY_NOT_COMPLETED);
     *     }
     * }
     * }
     * </pre>
     *
     * @param did Entity DID to validate
     * @throws org.omnione.did.tas.exception.TaSdkException if the entity is not in a valid state
     */
    void validateEntityStatus(String did);
}
