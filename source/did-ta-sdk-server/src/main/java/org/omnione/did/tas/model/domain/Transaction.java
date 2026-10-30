package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Transaction domain model.
 *
 * <p>Represents a protocol transaction as seen by the SDK.
 * The server maps its database entity to this domain object before
 * passing it to the SDK via {@link org.omnione.did.tas.provider.TransactionProvider}.</p>
 *
 * <p><b>SDK usage:</b> The SDK uses this object solely to retrieve protocol
 * context needed for business logic (e.g., authNonce for DID Auth validation).
 * Transaction state management (status, expiration, step sequencing) is the
 * server's responsibility.</p>
 *
 * <p><b>Server-side mapping example:</b></p>
 * <pre>
 * {@code
 * Transaction.builder()
 *     .authNonce(entity.getAuthNonce())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /**
     * Authentication nonce for DID Auth.
     *
     * <p>Generated in Propose step, validated in subsequent steps (e.g., Step 3).
     * Multibase-encoded 16-byte random value.</p>
     */
    private String authNonce;

    /**
     * DID associated with this transaction.
     *
     * <p>The DID of the user or entity for whom this transaction is being processed.
     * Used in P142 Step 2 to retrieve user information for Offer delivery.</p>
     */
    private String did;
}
