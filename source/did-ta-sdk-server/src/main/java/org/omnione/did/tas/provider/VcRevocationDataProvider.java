package org.omnione.did.tas.provider;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.VcMeta;

/**
 * Provider interface for VC Revocation Protocol data access.
 *
 * <p>The TA Server implements this interface to supply data needed by the SDK
 * during P220 VC Revocation Protocol execution.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK reads, Server implements</p>
 */
public interface VcRevocationDataProvider {

    // ===== VC Metadata =====

    /**
     * Retrieve VC metadata from blockchain.
     *
     * <p>Used in Step 1 (proposeRevokeVc) to fetch VC information
     * and extract Issuer DID.</p>
     *
     * @param vcId VC ID
     * @return VC metadata (issuer, subject, status, dates)
     * @throws TaSdkException if VC not found or blockchain query fails
     */
    VcMeta getVcMeta(String vcId) throws TaSdkException;

    // ===== Transaction Information =====

    /**
     * Retrieve Issuer DID from Transaction.
     *
     * <p>Used in Step 4, 5 to call Issuer APIs.</p>
     *
     * @param txId Transaction ID
     * @return Issuer DID
     * @throws TaSdkException if Transaction not found
     */
    String getIssuerDid(String txId) throws TaSdkException;

    /**
     * Retrieve issuerNonce from Transaction.
     *
     * <p>issuerNonce is received from Issuer in Step 1 and validated in Step 4.</p>
     *
     * @param txId Transaction ID
     * @return issuerNonce (multibase-encoded 16-byte nonce)
     * @throws TaSdkException if Transaction not found
     */
    String getIssuerNonce(String txId) throws TaSdkException;

    /**
     * Retrieve VC ID from Transaction.
     *
     * <p>Used in Step 4 to validate reqRevokeVc.vcId matches the original request.</p>
     *
     * @param txId Transaction ID
     * @return VC ID
     * @throws TaSdkException if Transaction not found
     */
    String getVcId(String txId) throws TaSdkException;
}
