package org.omnione.did.tas.provider;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.HolderInfo;
import org.omnione.did.tas.model.domain.VcPlanInfo;

/**
 * Provider interface for VC Issuance Protocol data access.
 *
 * <p>The TA Server implements this interface to supply data needed by the SDK
 * during P210 VC Issuance Protocol execution. All methods are read-only from
 * the SDK's perspective.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK reads, Server implements</p>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class VcIssuanceDataProviderImpl implements VcIssuanceDataProvider {
 *
 *     @Override
 *     public String getAuthNonce(String txId) throws TaSdkException {
 *         Transaction tx = transactionRepository.findByTxId(txId)
 *             .orElseThrow(() -> new TaSdkException(TRANSACTION_NOT_FOUND));
 *         return tx.getAuthNonce();
 *     }
 *
 *     @Override
 *     public String getExternalTxId(String txId) throws TaSdkException {
 *         Transaction tx = transactionRepository.findByTxId(txId)
 *             .orElseThrow(() -> new TaSdkException(TRANSACTION_NOT_FOUND));
 *         return tx.getExternalTxId();
 *     }
 *
 *     // ... other methods
 * }
 * }
 * </pre>
 */
public interface VcIssuanceDataProvider {

    // ===== Transaction Information =====

    /**
     * Retrieve authNonce from Transaction.
     *
     * <p>Used in Step 6 (requestIssueVc) for DID Auth validation.</p>
     *
     * @param txId Transaction ID
     * @return authNonce (multibase-encoded 16-byte nonce)
     * @throws TaSdkException if Transaction not found
     */
    String getAuthNonce(String txId) throws TaSdkException;

    /**
     * Retrieve externalTxId (Issuer's txId) from Transaction.
     *
     * <p>Used in Step 5, 6, 7 to call Issuer APIs with the correct Issuer txId.</p>
     *
     * @param txId TAS Transaction ID
     * @return Issuer Transaction ID
     * @throws TaSdkException if Transaction not found
     */
    String getExternalTxId(String txId) throws TaSdkException;

    /**
     * Retrieve refId from Transaction.
     *
     * <p>refId is an Issuer-provided reference number received in Step 2.</p>
     *
     * @param txId Transaction ID
     * @return Issuer reference number
     * @throws TaSdkException if Transaction not found
     */
    String getRefId(String txId) throws TaSdkException;

    // ===== Issuer Information =====

    /**
     * Retrieve Issuer DID from Transaction.
     *
     * <p>Used when the Issuer DID is not provided in the request.</p>
     *
     * @param txId Transaction ID
     * @return Issuer DID
     * @throws TaSdkException if Transaction not found
     */
    String getIssuerDid(String txId) throws TaSdkException;

    /**
     * Retrieve Issuer server URL.
     *
     * <p>Note: This method is deprecated. Use IssuerApiClient.getIssuerServerUrl() instead.</p>
     *
     * @param issuerDid Issuer DID
     * @return Issuer server base URL
     * @throws TaSdkException if Issuer not found
     * @deprecated Use IssuerApiClient directly instead
     */
    @Deprecated
    String getIssuerServerUrl(String issuerDid) throws TaSdkException;

    // ===== Holder Information =====

    /**
     * Retrieve Holder information for Issue Profile generation.
     *
     * <p>Used in Step 5 (requestIssueProfile) to provide Holder DID and PII
     * to the Issuer for generating the Issue Profile.</p>
     *
     * @param txId Transaction ID
     * @return Holder information (DID, PII)
     * @throws TaSdkException if Transaction or Holder not found
     */
    HolderInfo getHolderInfo(String txId) throws TaSdkException;

    /**
     * Retrieve Holder's FCM Push Token.
     *
     * <p>Used in Step 1b (offerIssueVcPush) to send push notification.</p>
     *
     * @param holderDid Holder DID
     * @return FCM Push Token
     * @throws TaSdkException if Holder not found or Push Token not registered
     */
    String getPushToken(String holderDid) throws TaSdkException;

    // ===== VC Plan Information =====

    /**
     * Retrieve VC Plan information.
     *
     * <p>Used in Step 2 (proposeIssueVc) to retrieve VC Plan details.</p>
     *
     * @param vcPlanId VC Plan ID
     * @return VC Plan information
     * @throws TaSdkException if VC Plan not found
     */
    VcPlanInfo getVcPlan(String vcPlanId) throws TaSdkException;

    /**
     * Retrieve Issuer DID from VC Plan.
     *
     * <p>Used in Step 2 (proposeIssueVc) when Issuer DID is not provided in the request.</p>
     *
     * @param vcPlanId VC Plan ID
     * @return Issuer DID
     * @throws TaSdkException if VC Plan not found
     */
    String getVcPlanIssuerDid(String vcPlanId) throws TaSdkException;
}
