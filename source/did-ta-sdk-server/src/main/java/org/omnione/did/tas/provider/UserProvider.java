package org.omnione.did.tas.provider;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.User;

/**
 * Provider interface for User lookup and duplicate validation (read-only).
 *
 * <p>The TA Server implements this interface to check whether a user already
 * exists in the system before completing registration. The SDK uses this
 * to enforce uniqueness constraints on DID and PII (Personally Identifiable Information).</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Read-Only Principle:</b></p>
 * <ul>
 *   <li>SDK reads user existence (existsUserByDid, existsUserByPii)</li>
 *   <li>SDK reads user info (findByDid)</li>
 *   <li>Server writes user records (saveUser, updateUserStatus)</li>
 *   <li>Clear separation of concerns</li>
 * </ul>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P132 User Registration Step 5 - checks for duplicate DID and PII</li>
 *   <li>P141 DID Document Update Step 1 - retrieves user and validates status</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class UserProviderImpl implements UserProvider {
 *
 *     @Override
 *     public User findByDid(String did) {
 *         UserEntity entity = userRepository.findByDid(did)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.USER_NOT_FOUND));
 *         return User.builder()
 *             .did(entity.getDid())
 *             .status(UserStatus.valueOf(entity.getStatus().name()))
 *             .pii(entity.getPii())
 *             .build();
 *     }
 *
 *     @Override
 *     public boolean existsUserByDid(String did) {
 *         return userRepository.existsByDid(did);
 *     }
 *
 *     @Override
 *     public boolean existsUserByPii(String pii) {
 *         return userRepository.existsByPii(pii);
 *     }
 * }
 * }
 * </pre>
 */
public interface UserProvider {

    /**
     * Find user by DID.
     *
     * <p>Used in P141 Step 1 (proposeUpdateDidDoc) to retrieve user information
     * and validate the user status before allowing DID Document update.</p>
     *
     * @param did User DID (e.g., "did:omn:user:12345")
     * @return User domain model
     * @throws TaSdkException if user not found (USER_NOT_FOUND)
     */
    User findByDid(String did) throws TaSdkException;

    /**
     * Check if a user with the given DID already exists.
     *
     * <p>Used in Step 5 (requestRegisterUser) to prevent duplicate DID registration.
     * If a user with this DID already exists, the SDK throws an error before
     * proceeding with blockchain registration.</p>
     *
     * @param did User DID (e.g., "did:omn:user:12345")
     * @return true if a user with this DID already exists
     */
    boolean existsUserByDid(String did);

    /**
     * Check if a user with the given PII (CI) already exists.
     *
     * <p>Used in Step 5 (requestRegisterUser) to prevent duplicate user registration
     * based on identity. PII here refers to the CI (Connecting Information) value
     * derived from KYC verification.</p>
     *
     * @param pii PII/CI value from KYC verification
     * @return true if a user with this PII already exists
     */
    boolean existsUserByPii(String pii);

    // Note: User status validation is NOT part of the SDK Provider pattern.
    // Unlike EntityProvider.validateEntityStatus(), user status validation
    // is the server's responsibility and should be performed BEFORE calling
    // SDK Protocol methods (e.g., proposeUpdateDidDoc, proposeRestoreDidDoc).
    //
    // Server validates:
    // - User status (ACTIVATED, DEACTIVATED, etc.)
    // - Business rules (last login time, restore attempt count, etc.)
    // - Authorization (admin approval, VIP status, etc.)
    //
    // SDK only handles protocol orchestration and cryptographic operations.
}
