package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.data.model.did.Proof;

/**
 * AccE2e (Accept E2E) encryption acceptance model.
 *
 * <p>Wallet's acceptance of E2E encryption request from Issuer.
 * Contains Wallet's public key, IV, and proof for E2E encryption verification.</p>
 *
 * <p><b>Used in:</b></p>
 * <ul>
 *   <li>P210 Step 6 - Wallet sends AccE2e to accept E2E encryption</li>
 * </ul>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccE2e {

    /**
     * Wallet's public key for E2E encryption.
     *
     * <p>Multibase-encoded public key.</p>
     */
    private String publicKey;

    /**
     * Initialization Vector for E2E encryption.
     *
     * <p>Multibase-encoded IV.</p>
     */
    private String iv;

    /**
     * Proof of E2E encryption acceptance.
     *
     * <p>Cryptographic proof verifying the AccE2e message.</p>
     */
    private Proof proof;
}
