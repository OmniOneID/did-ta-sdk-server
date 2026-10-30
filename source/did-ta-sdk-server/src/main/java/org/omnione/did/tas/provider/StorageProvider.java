package org.omnione.did.tas.provider;

import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.data.model.vc.VcMeta;

/**
 * Provider interface for blockchain/LSS storage operations.
 *
 * <p>This interface defines the contract for storage operations (DID Document registration,
 * retrieval, and status updates). TA Server implements this interface to provide actual
 * blockchain or LSS integration.</p>
 *
 * <p><b>Design Pattern:</b> Provider Pattern - SDK delegates storage operations to the server.</p>
 *
 * <p><b>Implementation Note:</b></p>
 * <ul>
 *   <li>TA Server implements this interface using blockchain (Fabric/Besu) or LSS client</li>
 *   <li>SDK receives provider instance via constructor injection</li>
 *   <li>All methods are read-only from SDK perspective (SDK calls, Server implements)</li>
 * </ul>
 *
 * <p><b>Usage Example (Server-side implementation):</b></p>
 * <pre>
 * {@code
 * @Service
 * public class BlockchainStorageProvider implements StorageProvider {
 *     @Override
 *     public void registerDidDocument(InvokedDidDoc invokedDidDoc, RoleType roleType) {
 *         blockchainAdapter.registerDidDocument(invokedDidDoc, roleType);
 *     }
 * }
 * }
 * </pre>
 */
public interface StorageProvider {

    /**
     * Register DID Document to blockchain/LSS.
     *
     * <p>Submits an {@link InvokedDidDoc} (TAS-signed DID Document) to the
     * blockchain. The {@code roleType} determines which contract method is
     * invoked (e.g., TAS, WALLET, USER, ISSUER).</p>
     *
     * @param invokedDidDoc TAS-signed DID Document ready for blockchain submission
     * @param roleType      Role of the DID owner (e.g., {@link RoleType#WALLET})
     * @throws org.omnione.did.tas.exception.TaSdkException if registration fails
     */
    void registerDidDocument(InvokedDidDoc invokedDidDoc, RoleType roleType);

    /**
     * Retrieve DID Document from blockchain/LSS.
     *
     * @param did DID identifier
     * @return DID Document
     * @throws org.omnione.did.tas.exception.TaSdkException if DID not found or retrieval fails
     */
    DidDocument getDidDocument(String did);

    /**
     * Update DID Document status (e.g., ACTIVATED, DEACTIVATED, REVOKED).
     *
     * @param did DID identifier
     * @param status New status value
     * @throws org.omnione.did.tas.exception.TaSdkException if update fails
     */
    void updateDidDocumentStatus(String did, String status);

    /**
     * Check if DID Document exists in blockchain/LSS.
     *
     * @param did DID identifier
     * @return true if DID Document exists
     */
    boolean existsDidDocument(String did);

    /**
     * Retrieve DID Document version information.
     *
     * @param did DID identifier
     * @return Version ID string
     * @throws org.omnione.did.tas.exception.TaSdkException if DID not found
     */
    String getDidDocumentVersionId(String did);

    /**
     * Retrieve DID Document from blockchain/LSS (null-safe variant).
     *
     * <p>Unlike {@link #getDidDocument(String)}, returns null if not found
     * instead of throwing an exception.</p>
     *
     * @param did DID identifier
     * @return DID Document, or null if not found
     */
    DidDocument findDidDoc(String did);

    /**
     * Register VC metadata to blockchain/LSS.
     *
     * <p>Called after Certificate VC issuance to record VC metadata on the
     * blockchain for verifiability.</p>
     *
     * @param vcMeta VC metadata to register
     * @throws org.omnione.did.tas.exception.TaSdkException if registration fails
     */
    void registerVcMeta(VcMeta vcMeta);

    /**
     * Retrieve VC metadata from blockchain/LSS.
     *
     * @param vcId VC identifier
     * @return VC metadata
     * @throws org.omnione.did.tas.exception.TaSdkException if not found
     */
    VcMeta findVcMeta(String vcId);
}
