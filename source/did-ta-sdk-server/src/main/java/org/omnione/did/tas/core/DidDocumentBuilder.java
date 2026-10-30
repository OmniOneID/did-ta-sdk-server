package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.core.data.rest.DidKeyInfo;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.wallet.key.WalletManagerInterface;

import java.util.List;
import java.util.Map;

/**
 * DID Document Builder for TA SDK.
 *
 * <p>Wraps {@link BaseCoreDidUtil} static methods to provide a cohesive API for
 * DID Document creation, parsing, and verification in TA protocol contexts.</p>
 *
 * <p><b>Reused from did-common-sdk (no re-implementation):</b></p>
 * <ul>
 *   <li>{@code BaseCoreDidUtil.createDefaultProofPurposes()} - Standard TAS key→purpose mapping</li>
 *   <li>{@code BaseCoreDidUtil.getDidKeyInfosFromWallet()} - Extract key info from wallet</li>
 *   <li>{@code BaseCoreDidUtil.createDidDocument()} - Build unsigned DID Document</li>
 *   <li>{@code BaseCoreDidUtil.signAndAddProof()} - Sign document with wallet keys</li>
 *   <li>{@code BaseCoreDidUtil.parseDidDoc()} - Parse JSON to DidDocument</li>
 *   <li>{@code BaseCoreDidUtil.verifyDidDocKeyProofs()} - Verify all key proofs</li>
 *   <li>{@code BaseCoreDidUtil.getVerificationMethod()} - Look up verification method</li>
 *   <li>{@code BaseCoreDidUtil.getPublicKey()} - Extract public key string</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * DidDocumentBuilder builder = new DidDocumentBuilder(fileWalletAdapter);
 *
 * // Create and sign TAS DID Document (P110)
 * DidDocument tasDidDoc = builder.createAndSign("did:omn:tas", "did:omn:tas");
 *
 * // Parse and verify a received DID Document (P120, P132)
 * DidDocument userDidDoc = builder.parse(didDocJson);
 * builder.verifyKeyProofs(userDidDoc);
 *
 * // Get verification method ID for proof generation
 * String verificationMethodId = builder.getVerificationMethodId(tasDidDoc, ProofPurpose.ASSERTION_METHOD);
 * // → "did:omn:tas?versionId=1#assert"
 * }
 * </pre>
 */
@Slf4j
public class DidDocumentBuilder {

    private final FileWalletAdapter fileWalletAdapter;

    /**
     * Create DidDocumentBuilder with the TA's file wallet adapter.
     *
     * @param fileWalletAdapter Initialized file wallet adapter for key access and signing
     */
    public DidDocumentBuilder(FileWalletAdapter fileWalletAdapter) {
        this.fileWalletAdapter = fileWalletAdapter;
    }

    /**
     * Create and sign a DID Document using keys from the wallet.
     *
     * <p>Full creation flow:</p>
     * <ol>
     *   <li>Create default proof purposes map (assert/auth/keyagree/invoke)</li>
     *   <li>Extract key info from wallet</li>
     *   <li>Build unsigned DID Document</li>
     *   <li>Sign with all non-keyAgreement keys and add proofs</li>
     * </ol>
     *
     * @param did        DID string (e.g. "did:omn:tas")
     * @param controller Controller DID (usually same as DID for self-controlled)
     * @return Signed DidDocument
     * @throws TaSdkException if creation or signing fails
     */
    public DidDocument createAndSign(String did, String controller) {
        try {
            log.info("Creating DID Document for: {}", did);

            WalletManagerInterface walletManager = fileWalletAdapter.getWalletManager();

            // Step 1: Standard TAS proof purposes (assert/auth/keyagree/invoke)
            Map<String, List<org.omnione.did.data.model.enums.did.ProofPurpose>> purposes =
                BaseCoreDidUtil.createDefaultProofPurposes();

            // Step 2: Extract key info from wallet
            List<DidKeyInfo> keyInfos =
                BaseCoreDidUtil.getDidKeyInfosFromWallet(walletManager, did, purposes);

            // Step 3: Build unsigned DID Document
            DidManager didManager = new DidManager();
            BaseCoreDidUtil.createDidDocument(didManager, did, controller, keyInfos);

            // Step 4: Sign with assert/auth/invoke keys (keyagree is excluded)
            List<String> signingKeyIds = BaseCoreDidUtil.getSigningKeyIds(purposes);
            DidDocument signedDoc = BaseCoreDidUtil.signAndAddProof(didManager, walletManager, signingKeyIds);

            log.info("DID Document created successfully: {}", did);
            return signedDoc;

        } catch (TaSdkException e) {
            throw e;
        } catch (CommonSdkException e) {
            log.error("Failed to create DID Document: {}", did, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR, e);
        } catch (Exception e) {
            log.error("Unexpected error creating DID Document: {}", did, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR, e);
        }
    }

    /**
     * Parse a DID Document from a JSON string.
     *
     * @param didDocJson DID Document JSON string
     * @return Parsed DidDocument
     * @throws TaSdkException if parsing fails
     */
    public DidDocument parse(String didDocJson) {
        try {
            DidManager didManager = BaseCoreDidUtil.parseDidDoc(didDocJson);
            return didManager.getDocument();
        } catch (CommonSdkException e) {
            log.error("Failed to parse DID Document JSON", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PARAMETER, e);
        }
    }

    /**
     * Verify all key proofs in a DID Document.
     *
     * <p>Used when receiving a DID Document from an external party (P120, P132)
     * to confirm the document was signed by the corresponding private keys.</p>
     *
     * @param didDocument DID Document to verify
     * @throws TaSdkException if any key proof is invalid
     */
    public void verifyKeyProofs(DidDocument didDocument) {
        try {
            BaseCoreDidUtil.verifyDidDocKeyProofs(didDocument);
        } catch (CommonSdkException e) {
            log.error("DID Document key proof verification failed", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF, e);
        }
    }

    /**
     * Get the verification method ID for a given proof purpose.
     *
     * <p>Format: {@code {did}?versionId={version}#{keyId}}</p>
     * <ul>
     *   <li>ASSERTION_METHOD      → {@code did:omn:tas?versionId=1#assert}</li>
     *   <li>AUTHENTICATION        → {@code did:omn:tas?versionId=1#auth}</li>
     *   <li>KEY_AGREEMENT         → {@code did:omn:tas?versionId=1#keyagree}</li>
     *   <li>CAPABILITY_INVOCATION → {@code did:omn:tas?versionId=1#invoke}</li>
     * </ul>
     *
     * @param didDocument  DID Document
     * @param proofPurpose Proof purpose to get the ID for
     * @return Fully qualified verification method ID
     */
    public String getVerificationMethodId(DidDocument didDocument, ProofPurpose proofPurpose) {
        String did = didDocument.getId();
        String version = didDocument.getVersionId();
        if (version == null || version.isEmpty()) {
            version = "1";
        }
        // ProofPurpose.toKeyId() → "assert" / "auth" / "keyagree" / "invoke"
        return did + "?versionId=" + version + "#" + proofPurpose.toKeyId();
    }

    /**
     * Get a specific VerificationMethod from a DID Document by key ID.
     *
     * @param didDocument DID Document
     * @param keyId       Key ID (e.g. "assert", "did:omn:tas#assert")
     * @return VerificationMethod
     * @throws TaSdkException if the key ID is not found
     */
    public VerificationMethod getVerificationMethod(DidDocument didDocument, String keyId) {
        try {
            return BaseCoreDidUtil.getVerificationMethod(didDocument, keyId);
        } catch (CommonSdkException e) {
            log.error("Verification method not found: {}", keyId, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "Verification method not found: " + keyId);
        }
    }

    /**
     * Get the public key multibase string from a DID Document by key ID.
     *
     * @param didDocument DID Document
     * @param keyId       Key ID (e.g. "assert")
     * @return Public key multibase string
     * @throws TaSdkException if the key is not found
     */
    public String getPublicKey(DidDocument didDocument, String keyId) {
        try {
            return BaseCoreDidUtil.getPublicKey(didDocument, keyId);
        } catch (CommonSdkException e) {
            log.error("Public key not found: {}", keyId, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "Public key not found for key ID: " + keyId);
        }
    }
}
