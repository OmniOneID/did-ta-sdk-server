package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.datamodel.enums.did.ProofType;
import org.omnione.did.common.domain.did.DidDocCryptoHelper;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.provider.DidDocumentProvider;

/**
 * Handler for InvokedDidDoc creation and signing.
 *
 * <p>InvokedDidDoc is the data structure submitted to the blockchain when
 * registering or updating a DID Document. It contains:</p>
 * <ul>
 *   <li>Encoded owner DID Document (base-multibase encoded)</li>
 *   <li>TAS as the invoker (Provider with TAS DID and certificate VC URL)</li>
 *   <li>A nonce for replay protection</li>
 *   <li>A proof signed by TAS's capability invocation key</li>
 * </ul>
 *
 * <p><b>Reused from did-common-sdk (no re-implementation):</b></p>
 * <ul>
 *   <li>{@link DidDocCryptoHelper#generateInvokedDidDoc} - Builds the unsigned structure</li>
 *   <li>{@link DidDocCryptoHelper#signInvokedDidDoc} - Signs with wallet key</li>
 *   <li>{@link SignatureCryptoHelper#removeProofValue} - Removes proof before encoding</li>
 *   <li>{@link BaseCoreDidUtil#getVerificationMethod} - Finds the invoke key ID</li>
 * </ul>
 *
 * <p><b>Used by protocols:</b></p>
 * <ul>
 *   <li>P110 TAS 등록 - TAS signs its own DID Document</li>
 *   <li>P120 Entity 등록 - TAS signs entity's DID Document</li>
 *   <li>P132 사용자 등록 - TAS signs user's DID Document</li>
 *   <li>P141 DID Document 갱신 - TAS signs updated DID Document</li>
 *   <li>P142 DID Document 복구 - TAS signs restored DID Document</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * InvokedDidDocHandler handler = new InvokedDidDocHandler(fileWalletAdapter, didDocumentProvider);
 *
 * // Sign a user's DID Document for blockchain registration (P132)
 * InvokedDidDoc invokedDidDoc = handler.sign(userDidDocument, "https://tas.example.com/cert-vc");
 *
 * // Register to blockchain
 * blockchainAdapter.registerDidDocument(invokedDidDoc, RoleType.HOLDERS);
 * }
 * </pre>
 */
@Slf4j
public class InvokedDidDocHandler {

    private final FileWalletAdapter fileWalletAdapter;
    private final DidDocumentProvider didDocumentProvider;

    /**
     * Create InvokedDidDocHandler.
     *
     * @param fileWalletAdapter   Initialized wallet for signing with the invoke key
     * @param didDocumentProvider Server-provided DID Document lookup for TAS DID Document
     */
    public InvokedDidDocHandler(FileWalletAdapter fileWalletAdapter,
                                DidDocumentProvider didDocumentProvider) {
        this.fileWalletAdapter = fileWalletAdapter;
        this.didDocumentProvider = didDocumentProvider;
    }

    /**
     * Create a signed InvokedDidDoc for blockchain registration.
     *
     * <p>TAS acts as the invoker and signs the owner's DID Document using
     * its capability invocation key. The owner can be TAS itself, an entity,
     * or a user.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Fetch TAS DID Document via {@link DidDocumentProvider}</li>
     *   <li>Remove proof from owner DID Document (protocol requirement before encoding)</li>
     *   <li>Generate InvokedDidDoc structure with TAS as invoker</li>
     *   <li>Sign with TAS's invoke key (CAPABILITY_INVOCATION)</li>
     * </ol>
     *
     * @param ownerDidDoc    DID Document to be registered (proof will be stripped internally)
     * @param certificateUrl TAS certificate VC reference URL (from TAS registration record)
     * @return Signed InvokedDidDoc ready for blockchain submission
     * @throws TaSdkException if DID Document retrieval, generation, or signing fails
     */
    public InvokedDidDoc sign(DidDocument ownerDidDoc, String certificateUrl) {
        try {
            log.debug("Creating InvokedDidDoc for: {}", ownerDidDoc.getId());

            // Step 1: Fetch TAS DID Document (TAS is always the invoker)
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();

            // Step 2: Remove proof from owner DID Document before encoding
            // Protocol requires a clean DID Document (no existing proof) when generating InvokedDidDoc
            DidDocument cleanOwnerDidDoc = SignatureCryptoHelper.removeProofValue(ownerDidDoc);

            // Step 3: Generate unsigned InvokedDidDoc
            // TAS = invoker (provider.did, provider.certVcRef), cleanOwnerDidDoc = encoded target
            InvokedDidDoc unsigned = DidDocCryptoHelper.generateInvokedDidDoc(
                tasDidDoc,
                cleanOwnerDidDoc,
                ProofType.SECP_256R1_SIGNATURE_2018,
                certificateUrl
            );

            // Step 4: Find TAS's capability invocation key ID
            // ProofPurpose.CAPABILITY_INVOCATION.toKeyId() = "invoke"
            VerificationMethod invokeVm = BaseCoreDidUtil.getVerificationMethod(
                tasDidDoc,
                ProofPurpose.CAPABILITY_INVOCATION.toKeyId()
            );

            // Step 5: Sign with TAS wallet's invoke key
            InvokedDidDoc signed = DidDocCryptoHelper.signInvokedDidDoc(
                unsigned,
                fileWalletAdapter.getWalletManager(),
                invokeVm.getId()
            );

            log.debug("InvokedDidDoc signed for: {}", ownerDidDoc.getId());
            return signed;

        } catch (TaSdkException e) {
            throw e;
        } catch (CommonSdkException e) {
            log.error("Failed to create InvokedDidDoc for: {}", ownerDidDoc.getId(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_GENERATION_FAILED, e);
        } catch (Exception e) {
            log.error("Unexpected error creating InvokedDidDoc for: {}", ownerDidDoc.getId(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_GENERATION_FAILED, e);
        }
    }
}
