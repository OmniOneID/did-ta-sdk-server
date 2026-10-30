package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.ecdh.AccEcdh;
import org.omnione.did.common.datamodel.data.ecdh.EcdhReqData;
import org.omnione.did.common.datamodel.enums.crypto.SymmetricCipherType;
import org.omnione.did.common.datamodel.enums.crypto.SymmetricPaddingType;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.datamodel.enums.did.ProofType;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.exception.ErrorCode;
import org.omnione.did.common.service.impl.ecdh.BaseEcdhServiceImpl;
import org.omnione.did.common.util.crypto.BaseCryptoUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.Proof;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.EcdhSessionProvider;

import java.time.Instant;

/**
 * TA SDK ECDH Service.
 *
 * <p>Extends {@link BaseEcdhServiceImpl} to provide TA-specific ECDH key agreement.
 * Handles client request verification, session key generation, and AccEcdh signing.</p>
 *
 * <p><b>Inherited from BaseEcdhServiceImpl (no re-implementation):</b></p>
 * <ul>
 *   <li>{@code processEcdhRequest()} - Full ECDH flow: verify → key pair → session key → AccEcdh → save</li>
 *   <li>{@code verifyEcdhRequest()} - Validates client DID and proof signature</li>
 *   <li>{@code generateSessionKey()} - ECDH + nonce → symmetric key derivation</li>
 * </ul>
 *
 * <p><b>TA-specific implementations:</b></p>
 * <ul>
 *   <li>{@code getClientPublicKey()} - Fetches client DID Document via {@link DidDocumentProvider}</li>
 *   <li>{@code generateServerNonce()} - Generates 16-byte random nonce</li>
 *   <li>{@code saveEcdhSession()} - Delegates to {@link EcdhSessionProvider}</li>
 * </ul>
 *
 * <p><b>AccEcdh signing:</b></p>
 * <p>After key agreement, the server signs the AccEcdh response with the TAS keyagree key
 * (ProofPurpose: KEY_AGREEMENT) to prove server participation.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * TaEcdhService ecdhService = new TaEcdhService(
 *     fileWalletAdapter, didDocumentBuilder,
 *     didDocumentProvider, ecdhSessionProvider
 * );
 *
 * AccEcdh accEcdh = ecdhService.processAndSign(
 *     ecdhReqData,
 *     SymmetricCipherType.AES_256_CBC,
 *     SymmetricPaddingType.PKCS5
 * );
 * }
 * </pre>
 */
@Slf4j
public class TaEcdhService extends BaseEcdhServiceImpl {

    private final FileWalletAdapter fileWalletAdapter;
    private final DidDocumentBuilder didDocumentBuilder;
    private final DidDocumentProvider didDocumentProvider;
    private final EcdhSessionProvider ecdhSessionProvider;

    /**
     * Create TaEcdhService.
     *
     * @param fileWalletAdapter    Initialized wallet for signing AccEcdh
     * @param didDocumentBuilder   For public key extraction from DID Documents
     * @param didDocumentProvider  Server-provided DID Document lookup
     * @param ecdhSessionProvider  Server-provided ECDH session persistence
     */
    public TaEcdhService(
            FileWalletAdapter fileWalletAdapter,
            DidDocumentBuilder didDocumentBuilder,
            DidDocumentProvider didDocumentProvider,
            EcdhSessionProvider ecdhSessionProvider) {
        this.fileWalletAdapter = fileWalletAdapter;
        this.didDocumentBuilder = didDocumentBuilder;
        this.didDocumentProvider = didDocumentProvider;
        this.ecdhSessionProvider = ecdhSessionProvider;
    }

    /**
     * Process ECDH request and return a signed AccEcdh response.
     *
     * <p>This is the main entry point for protocol classes. Combines
     * {@link BaseEcdhServiceImpl#processEcdhRequest} with AccEcdh signing.</p>
     *
     * <p>The server determines {@code serverCipher} and {@code serverPadding} from
     * its configuration before calling this method.</p>
     *
     * @param ecdhReqData   ECDH request data from client
     * @param serverCipher  Server's selected cipher type
     * @param serverPadding Server's selected padding type
     * @return Signed AccEcdh containing server public key, nonce, cipher info, and proof
     * @throws TaSdkException if ECDH processing or signing fails
     */
    public AccEcdh processAndSign(
            EcdhReqData ecdhReqData,
            SymmetricCipherType serverCipher,
            SymmetricPaddingType serverPadding) {
        try {
            log.debug("Processing ECDH request from: {}", ecdhReqData.getClient());

            AccEcdh accEcdh = processEcdhRequest(ecdhReqData, serverCipher, serverPadding);
            signAccEcdh(accEcdh);

            log.debug("ECDH request processed and signed for: {}", ecdhReqData.getClient());
            return accEcdh;

        } catch (TaSdkException e) {
            throw e;
        } catch (CommonSdkException e) {
            log.error("ECDH processing failed", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_ECDH_KEY_AGREEMENT_FAILED, e);
        } catch (Exception e) {
            log.error("Unexpected error during ECDH processing", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_ECDH_KEY_AGREEMENT_FAILED, e);
        }
    }

    // -------------------------------------------------------------------------
    // BaseEcdhServiceImpl abstract method implementations
    // -------------------------------------------------------------------------

    /**
     * Retrieve the client's keyagree public key for ECDH request verification.
     *
     * <p>Fetches the client DID Document via {@link DidDocumentProvider}
     * and extracts the "keyagree" verification method public key.</p>
     */
    @Override
    protected String getClientPublicKey(String clientDid) throws CommonSdkException {
        try {
            DidDocument clientDidDoc = didDocumentProvider.getDidDocument(clientDid);
            return didDocumentBuilder.getPublicKey(clientDidDoc, "keyagree");
        } catch (TaSdkException e) {
            throw new CommonSdkException(ErrorCode.INVALID_DID_DOCUMENT,
                "Failed to get client keyagree public key for: " + clientDid);
        }
    }

    /**
     * Generate a 16-byte random server nonce.
     */
    @Override
    protected byte[] generateServerNonce() throws CommonSdkException {
        return BaseCryptoUtil.generateNonce(16);
    }

    /**
     * Save ECDH session data via {@link EcdhSessionProvider}.
     *
     * <p>Cipher and padding are NOT passed to the provider because the server
     * already knows its own configuration and retrieves them from its config
     * when decrypting subsequent messages.</p>
     */
    @Override
    protected void saveEcdhSession(String clientDid, String sessionKey, String mergedNonce)
            throws CommonSdkException {
        ecdhSessionProvider.saveEcdhSession(clientDid, sessionKey, mergedNonce);
    }

    // -------------------------------------------------------------------------
    // AccEcdh signing
    // -------------------------------------------------------------------------

    /**
     * Sign the AccEcdh response with the TAS keyagree key.
     *
     * <p>Sets the server DID and constructs a KEY_AGREEMENT proof:</p>
     * <ol>
     *   <li>Fetch TAS DID Document via {@link DidDocumentProvider}</li>
     *   <li>Set {@code accEcdh.server} to TAS DID</li>
     *   <li>Build Proof with verificationMethod = {@code {did}?versionId={v}#keyagree}</li>
     *   <li>Generate signature message (canonical JSON hash of AccEcdh with null proofValue)</li>
     *   <li>Sign with "keyagree" wallet key and set proofValue</li>
     * </ol>
     */
    private void signAccEcdh(AccEcdh accEcdh) throws CommonSdkException {
        DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
        String tasDid = tasDidDoc.getId();

        accEcdh.setServer(tasDid);

        // Build verificationMethod ID: {did}?versionId={version}#keyagree
        String version = tasDidDoc.getVersionId();
        if (version == null || version.isEmpty()) {
            version = "1";
        }
        String verificationMethodId = tasDid + "?versionId=" + version + "#keyagree";

        // Build proof with null proofValue (required before signature message generation)
        Proof proof = new Proof();
        proof.setType(ProofType.SECP_256R1_SIGNATURE_2018.toString());
        proof.setCreated(Instant.now().toString());
        proof.setVerificationMethod(verificationMethodId);
        proof.setProofPurpose(ProofPurpose.KEY_AGREEMENT.toString());
        proof.setProofValue(null);
        accEcdh.setProof(proof);

        // Sign: canonical JSON hash → wallet sign → multibase encode
        byte[] signatureMessage = SignatureCryptoHelper.generateSignatureMessage(accEcdh);
        byte[] signature = fileWalletAdapter.sign("keyagree", signatureMessage);
        accEcdh.getProof().setProofValue(BaseMultibaseUtil.encode(signature));

        log.debug("AccEcdh signed with TAS keyagree key, verificationMethod: {}", verificationMethodId);
    }
}
