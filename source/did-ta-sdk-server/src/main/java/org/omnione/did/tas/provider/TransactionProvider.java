package org.omnione.did.tas.provider;

import org.omnione.did.tas.model.domain.Transaction;

/**
 * Provider interface for Transaction lookup (read-only).
 *
 * <p>The TA Server implements this interface to provide transaction information
 * to the SDK. The SDK uses this to retrieve protocol context (e.g., authNonce)
 * for business logic processing.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK calls, Server implements</p>
 *
 * <p><b>Read-Only Principle:</b></p>
 * <ul>
 *   <li>SDK reads transaction context (getTransaction) for business logic</li>
 *   <li>Server manages transaction lifecycle (creation, validation, state updates)</li>
 *   <li>Clear separation of concerns</li>
 * </ul>
 *
 * <p><b>Important:</b> Transaction state validation (status, expiration, step sequencing)
 * is the server's responsibility, NOT the SDK's. The SDK only uses transaction data
 * for business logic (e.g., authNonce for DID Auth validation).</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P120 Entity Registration - retrieves authNonce for DID Auth validation</li>
 *   <li>P132 User Registration - retrieves protocol context</li>
 *   <li>Other multi-step protocols</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TransactionProviderImpl implements TransactionProvider {
 *
 *     @Override
 *     public Transaction getTransaction(String txId) {
 *         TransactionEntity entity = transactionRepository.findByTxId(txId)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.TRANSACTION_NOT_FOUND));
 *         return entity.toDomain();
 *     }
 *
 *     @Override
 *     public boolean existsTransaction(String txId) {
 *         return transactionRepository.existsByTxId(txId);
 *     }
 * }
 * }
 * </pre>
 */
public interface TransactionProvider {

    /**
     * Retrieve transaction by external transaction ID (txId).
     *
     * <p>This is the primary lookup method used by SDK protocols.
     * The txId is shared with the client and passed in each protocol step.</p>
     *
     * @param txId External transaction ID (UUID string)
     * @return Transaction domain object
     * @throws org.omnione.did.tas.exception.TaSdkException if transaction not found (TRANSACTION_NOT_FOUND)
     */
    Transaction getTransaction(String txId);

    /**
     * Check if a transaction exists by txId.
     *
     * <p>Lightweight query for existence check without loading the full object.</p>
     *
     * @param txId External transaction ID
     * @return true if transaction exists
     */
    boolean existsTransaction(String txId);
}
