package org.omnione.did.tas.provider;

/**
 * Provider interface for registered wallet lookup (read-only).
 *
 * <p>The TA Server implements this interface to allow the SDK to check
 * whether a wallet has already been registered. Used during the P131
 * Wallet Registration Protocol to prevent duplicate registrations.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Read-Only Principle:</b></p>
 * <ul>
 *   <li>SDK reads wallet registration state (duplicate checks)</li>
 *   <li>Server writes wallet records (save after SDK returns)</li>
 * </ul>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P131 Wallet Registration - duplicate walletId/DID check before blockchain registration</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class WalletRegistrationProviderImpl implements WalletRegistrationProvider {
 *
 *     @Override
 *     public boolean checkWalletIdExists(String walletId) {
 *         return walletRepository.existsByWalletId(walletId);
 *     }
 *
 *     @Override
 *     public boolean checkWalletDidExists(String did) {
 *         return walletRepository.existsByDid(did);
 *     }
 * }
 * }
 * </pre>
 */
public interface WalletRegistrationProvider {

    /**
     * Check if a wallet with the given walletId already exists.
     *
     * <p>Called before blockchain registration to prevent duplicate wallet IDs.</p>
     *
     * @param walletId Wallet identifier assigned by the Wallet Provider
     * @return true if a wallet with this ID already exists
     * @throws org.omnione.did.tas.exception.TaSdkException if the check fails
     */
    boolean checkWalletIdExists(String walletId);

    /**
     * Check if a wallet with the given DID already exists.
     *
     * <p>Called before blockchain registration to prevent duplicate wallet DIDs.</p>
     *
     * @param did Wallet DID (e.g., "did:omn:wallet:12345")
     * @return true if a wallet with this DID already exists
     * @throws org.omnione.did.tas.exception.TaSdkException if the check fails
     */
    boolean checkWalletDidExists(String did);
}
