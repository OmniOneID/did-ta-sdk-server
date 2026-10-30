package org.omnione.did.tas.provider;

import org.omnione.did.tas.model.domain.PiiData;

/**
 * Provider interface for KYC (Know Your Customer) PII retrieval.
 *
 * <p>The TA Server implements this interface to retrieve PII (Personally Identifiable
 * Information) from the KYC system. The SDK supports two retrieval methods, and the
 * server chooses which one to implement based on the KYC integration model.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Two KYC Integration Options (choose one):</b></p>
 * <ul>
 *   <li><b>Option A - kycToken:</b> The client passes a token issued by the KYC system.
 *       The server decodes/validates the kycToken to extract embedded PII.</li>
 *   <li><b>Option B - kycTxId:</b> The client passes a KYC transaction ID.
 *       The server calls an external KYC server API to fetch PII using the ID.</li>
 * </ul>
 *
 * <p><b>Note:</b> This provider handles KYC system tokens (kycToken), which are
 * distinct from TAS-issued server tokens (serverToken). Server token validation
 * is handled by {@link TokenProvider}.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P132 User Registration Step 4 - retrieveKyc</li>
 * </ul>
 *
 * <p><b>Server-side implementation example (Option A - kycToken):</b></p>
 * <pre>
 * {@code
 * @Override
 * public PiiData getPiiFromKycToken(String kycToken) {
 *     KycTokenPayload payload = kycTokenVerifier.verify(kycToken);
 *     return PiiData.builder()
 *         .pii(payload.getPii())
 *         .name(payload.getName())
 *         .birthDate(payload.getBirthDate())
 *         .build();
 * }
 * }
 * </pre>
 *
 * <p><b>Server-side implementation example (Option B - kycTxId):</b></p>
 * <pre>
 * {@code
 * @Override
 * public PiiData retrieveKycData(String kycTxId) {
 *     KycResponse response = kycApiClient.get("/kyc/" + kycTxId);
 *     return PiiData.builder()
 *         .pii(response.getCi())
 *         .name(response.getName())
 *         .birthDate(response.getBirthDate())
 *         .build();
 * }
 * }
 * </pre>
 */
public interface KycProvider {

    /**
     * Extract PII from a KYC system token (Option A).
     *
     * <p>The kycToken is issued by the KYC system (not by TAS) and contains
     * embedded PII. The server decodes and verifies this token to extract PII
     * without making an external API call.</p>
     *
     * @param kycToken Token issued by the KYC system, containing embedded PII
     * @return PiiData containing name, birthDate, pii (CI), and other identity fields
     * @throws org.omnione.did.tas.exception.TaSdkException if token is invalid or PII extraction fails
     */
    PiiData getPiiFromKycToken(String kycToken);

    /**
     * Retrieve PII by calling the external KYC server API (Option B).
     *
     * <p>The kycTxId identifies a completed KYC transaction on the external KYC server.
     * The server makes an API call (e.g., GET /kyc/{kycTxId}) to fetch the PII
     * associated with that transaction.</p>
     *
     * @param kycTxId KYC transaction ID issued by the external KYC system
     * @return PiiData containing name, birthDate, pii (CI), and other identity fields
     * @throws org.omnione.did.tas.exception.TaSdkException if KYC lookup fails or returns no data
     */
    PiiData retrieveKycData(String kycTxId);
}
