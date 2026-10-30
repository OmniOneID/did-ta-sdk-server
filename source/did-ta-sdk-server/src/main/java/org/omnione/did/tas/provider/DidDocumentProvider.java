package org.omnione.did.tas.provider;

import org.omnione.did.data.model.did.DidDocument;

/**
 * Provider interface for DID Document retrieval.
 *
 * <p>The TA Server implements this interface to supply DID Documents from its
 * cache or blockchain storage. The SDK itself remains stateless and never
 * directly accesses the ledger or database.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>{@code TaEcdhService} - client's keyagree public key lookup, TAS DID doc for AccEcdh signing</li>
 *   <li>Future protocol classes (InvokedDidDoc Handler, VC protocols, etc.)</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TasDidDocumentProvider implements DidDocumentProvider {
 *
 *     @Override
 *     public DidDocument getDidDocument(String did) {
 *         // Fetch from cache or blockchain
 *         return didDocService.getDidDocument(did);
 *     }
 *
 *     @Override
 *     public DidDocument getTasDidDocument() {
 *         Tas tas = tasRepository.findTas();
 *         return didDocService.getDidDocument(tas.getDid());
 *     }
 * }
 * }
 * </pre>
 */
public interface DidDocumentProvider {

    /**
     * Retrieve a DID Document by DID identifier.
     *
     * <p>Implementation should use a cache with periodic refresh for performance.</p>
     *
     * @param did DID identifier (e.g. "did:omn:alice")
     * @return DID Document
     * @throws org.omnione.did.tas.exception.TaSdkException if DID not found or retrieval fails
     */
    DidDocument getDidDocument(String did);

    /**
     * Retrieve the TAS's own DID Document.
     *
     * <p>Used when signing AccEcdh responses to set the server field and
     * construct the verification method ID.</p>
     *
     * @return TAS DID Document
     * @throws org.omnione.did.tas.exception.TaSdkException if TAS is not registered or retrieval fails
     */
    DidDocument getTasDidDocument();
}
