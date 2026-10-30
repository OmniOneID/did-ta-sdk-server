package org.omnione.did.tas.provider;

/**
 * Provider interface for ECDH session persistence and retrieval.
 *
 * <p>The TA Server implements this interface to save and retrieve ECDH session data.
 * The SDK uses this to:</p>
 * <ul>
 *   <li>Save session after Step 2 (ECDH key agreement)</li>
 *   <li>Retrieve session for Step 3+ (encrypting/decrypting messages)</li>
 * </ul>
 *
 * <p><b>What to persist (server responsibility):</b></p>
 * <ul>
 *   <li>{@code sessionKey} - Symmetric session key for encrypting/decrypting subsequent messages</li>
 *   <li>{@code mergedNonce} - Merged nonce (clientNonce + serverNonce) for replay protection</li>
 *   <li>{@code cipher} - Negotiated cipher algorithm (e.g., "AES-256-CBC")</li>
 *   <li>{@code padding} - Negotiated padding scheme (e.g., "PKCS5")</li>
 *   <li>{@code clientDid} - Client DID for session ownership</li>
 *   <li>Association to the current transaction - Server manages this via txId</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TasEcdhSessionProvider implements EcdhSessionProvider {
 *
 *     @Override
 *     public void saveEcdhSession(String clientDid, String sessionKey, String mergedNonce) {
 *         String txId = transactionContext.getCurrentTransactionId();
 *         ecdhRepository.save(Ecdh.builder()
 *             .txId(txId)
 *             .clientDid(clientDid)
 *             .sessionKey(sessionKey)
 *             .nonce(mergedNonce)
 *             .cipher("AES-256-CBC")  // From server config
 *             .padding("PKCS5")       // From server config
 *             .build());
 *     }
 *
 *     @Override
 *     public String getSessionKey(String txId) {
 *         return ecdhRepository.findByTxId(txId)
 *             .map(Ecdh::getSessionKey)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.ECDH_SESSION_NOT_FOUND));
 *     }
 * }
 * }
 * </pre>
 */
public interface EcdhSessionProvider {

    /**
     * Persist ECDH session data after key agreement is complete.
     *
     * <p>Called by SDK after Step 2 (ECDH) completes. The server is responsible for
     * storing cipher and padding from its own configuration, and associating the
     * session with the current transaction.</p>
     *
     * @param clientDid   Client DID (session owner)
     * @param sessionKey  Negotiated symmetric session key (multibase-encoded)
     * @param mergedNonce Merged nonce (clientNonce + serverNonce, multibase-encoded)
     * @throws org.omnione.did.tas.exception.TaSdkException if persistence fails
     */
    void saveEcdhSession(String clientDid, String sessionKey, String mergedNonce);

    /**
     * Retrieve ECDH session key by transaction ID.
     *
     * <p>Used in Step 3+ to encrypt/decrypt messages with the session key
     * negotiated in Step 2.</p>
     *
     * @param txId Transaction ID (UUID string)
     * @return Session key (multibase-encoded)
     * @throws org.omnione.did.tas.exception.TaSdkException if session not found (ECDH_SESSION_NOT_FOUND)
     */
    String getSessionKey(String txId);

    /**
     * Retrieve negotiated cipher algorithm by transaction ID.
     *
     * <p>Returns the cipher algorithm negotiated during ECDH (Step 2).
     * Example values: "AES-256-CBC", "AES-128-CBC".</p>
     *
     * @param txId Transaction ID
     * @return Cipher algorithm display name
     * @throws org.omnione.did.tas.exception.TaSdkException if session not found
     */
    String getCipher(String txId);

    /**
     * Retrieve negotiated padding scheme by transaction ID.
     *
     * <p>Returns the padding scheme negotiated during ECDH (Step 2).
     * Example values: "PKCS5", "PKCS7".</p>
     *
     * @param txId Transaction ID
     * @return Padding scheme display name
     * @throws org.omnione.did.tas.exception.TaSdkException if session not found
     */
    String getPadding(String txId);

    /**
     * Retrieve client DID by transaction ID.
     *
     * <p>Returns the client DID associated with this ECDH session.
     * Useful for validating that the current request comes from the
     * same client that initiated the ECDH handshake.</p>
     *
     * @param txId Transaction ID
     * @return Client DID (e.g., "did:omn:entity:12345")
     * @throws org.omnione.did.tas.exception.TaSdkException if session not found
     */
    String getClientDid(String txId);

    /**
     * Check if ECDH session exists for a transaction.
     *
     * @param txId Transaction ID
     * @return true if session exists
     */
    boolean existsEcdhSession(String txId);
}
