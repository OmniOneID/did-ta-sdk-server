package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * E2E (End-to-End) encryption data model.
 *
 * <p>Contains encrypted VC and IV for E2E encryption between Wallet and Issuer.
 * The TAS acts as a mediator but cannot decrypt the VC content.</p>
 *
 * <p><b>Used in:</b></p>
 * <ul>
 *   <li>P210 Step 6 - Issuer returns E2E encrypted VC</li>
 * </ul>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class E2e {

    /**
     * Initialization Vector (IV) for AES encryption.
     *
     * <p>Multibase-encoded.</p>
     */
    private String iv;

    /**
     * E2E encrypted VC.
     *
     * <p>Encrypted with Wallet's public key, only Wallet can decrypt.
     * Multibase-encoded.</p>
     */
    private String encVc;
}
