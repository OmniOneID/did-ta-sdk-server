package org.omnione.did.tas.provider;

/**
 * Provider interface for server token validation (read-only).
 *
 * <p>The TA Server implements this interface to validate server tokens
 * issued by TAS during protocol sessions. The SDK uses this to verify
 * that a client is authorized to proceed with subsequent steps.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Token Lifecycle:</b></p>
 * <ul>
 *   <li>Server Token is created in Step 3 (requestCreateToken)</li>
 *   <li>Server Token is validated in Steps 4, 5, 6</li>
 *   <li>Server Token binds a session to a specific txId and purpose</li>
 * </ul>
 *
 * <p><b>Note:</b> This provider handles only TAS-issued server tokens.
 * KYC system tokens (kycToken) are handled separately by {@link KycProvider}.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P132 User Registration - validates serverToken in Steps 4, 5, 6</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TokenProviderImpl implements TokenProvider {
 *
 *     @Override
 *     public void validateServerToken(String serverToken, String txId) {
 *         ServerTokenEntity token = serverTokenRepository.findByTxId(txId)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.SERVER_TOKEN_NOT_FOUND));
 *         if (!token.getTokenValue().equals(serverToken)) {
 *             throw new TaSdkException(TaSdkErrorCode.SERVER_TOKEN_INVALID);
 *         }
 *         if (token.isExpired()) {
 *             throw new TaSdkException(TaSdkErrorCode.SERVER_TOKEN_EXPIRED);
 *         }
 *     }
 * }
 * }
 * </pre>
 */
public interface TokenProvider {

    /**
     * Validate a server token against the given transaction ID.
     *
     * <p>The server checks that:</p>
     * <ul>
     *   <li>A server token exists for the given txId</li>
     *   <li>The provided serverToken value matches the stored value</li>
     *   <li>The token has not expired</li>
     * </ul>
     *
     * @param serverToken Server token value provided by the client
     * @param txId        Transaction ID that the token is bound to
     * @throws org.omnione.did.tas.exception.TaSdkException if token is missing, invalid, or expired
     */
    void validateServerToken(String serverToken, String txId);
}
