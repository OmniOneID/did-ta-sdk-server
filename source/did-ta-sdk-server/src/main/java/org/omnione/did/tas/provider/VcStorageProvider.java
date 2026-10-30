package org.omnione.did.tas.provider;

import org.omnione.did.data.model.vc.VerifiableCredential;

/**
 * Provider interface for VC persistence and revocation status checking.
 *
 * <p>The TA Server implements this interface to save issued VCs to the database
 * and to check revocation status. The SDK delegates these storage operations to
 * the server, keeping itself stateless.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>{@code TaVcService} - persisting issued VCs and checking revocation on verify</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TasVcStorageProvider implements VcStorageProvider {
 *
 *     @Override
 *     public void saveVc(VerifiableCredential vc) {
 *         vcRepository.save(VcEntity.from(vc));
 *     }
 *
 *     @Override
 *     public void checkRevocationStatus(String vcId) {
 *         VcEntity vc = vcRepository.findByVcId(vcId)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.SDK_VC_META_NOT_FOUND));
 *         if (vc.isRevoked()) {
 *             throw new TaSdkException(TaSdkErrorCode.SDK_VC_REVOKED);
 *         }
 *     }
 * }
 * }
 * </pre>
 */
public interface VcStorageProvider {

    /**
     * Persist a newly issued VC.
     *
     * <p>Called after signing is complete. The server is responsible for storing
     * the VC along with its metadata (vcId, holderDid, issuedAt, etc.).</p>
     *
     * @param vc Signed VerifiableCredential to save
     * @throws org.omnione.did.tas.exception.TaSdkException if persistence fails
     */
    void saveVc(VerifiableCredential vc);

    /**
     * Check whether a VC has been revoked.
     *
     * <p>Called during VC verification. If the VC is revoked, the implementation
     * must throw {@code TaSdkException(TaSdkErrorCode.SDK_VC_REVOKED)}.</p>
     *
     * @param vcId VC identifier to check
     * @throws org.omnione.did.tas.exception.TaSdkException if VC is revoked or not found
     */
    void checkRevocationStatus(String vcId);

    /**
     * Revoke a VC with the given reason.
     *
     * <p>The server updates the VC status in the database and optionally registers
     * the revocation on the blockchain.</p>
     *
     * @param vcId   VC identifier to revoke
     * @param reason Revocation reason (e.g., "VOLUNTARY", "LOST", "COMPROMISED")
     * @throws org.omnione.did.tas.exception.TaSdkException if VC not found or revocation fails
     */
    void revokeVc(String vcId, String reason);
}
