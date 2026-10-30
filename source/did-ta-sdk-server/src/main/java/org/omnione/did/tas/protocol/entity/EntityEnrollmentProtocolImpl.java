package org.omnione.did.tas.protocol.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.crypto.SymmetricCipherType;
import org.omnione.did.common.datamodel.enums.crypto.SymmetricPaddingType;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.crypto.BaseCryptoUtil;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.tas.core.vc.VcBuilder;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.enums.vc.VcType;
import org.omnione.did.data.model.vc.DocumentVerificationEvidence;
import org.omnione.did.data.model.vc.VcMeta;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.Entity;
import org.omnione.did.tas.model.domain.Transaction;
import org.omnione.did.tas.model.request.protocol.entity.ConfirmEnrollEntityRequest;
import org.omnione.did.tas.model.request.protocol.entity.ProposeEnrollEntityRequest;
import org.omnione.did.tas.model.request.protocol.entity.RequestEnrollEntityRequest;
import org.omnione.did.tas.model.response.protocol.entity.ConfirmEnrollEntityResponse;
import org.omnione.did.tas.model.response.protocol.entity.ProposeEnrollEntityResponse;
import org.omnione.did.tas.model.response.protocol.entity.RequestEnrollEntityResponse;
import org.omnione.did.tas.provider.CertificateProvider;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.EcdhSessionProvider;
import org.omnione.did.tas.provider.EntityProvider;
import org.omnione.did.tas.provider.StorageProvider;
import org.omnione.did.tas.provider.TransactionProvider;
import org.omnione.did.tas.provider.WalletProvider;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * P120: Entity 등록 프로토콜 구현체.
 *
 * <p>Implements the 4-step Entity enrollment protocol. All crypto operations
 * (DID Auth validation, VC generation, signing, encryption) are performed here.
 * Server-side operations (DB access, transaction management) are delegated to
 * Provider interfaces.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TransactionProvider} - transaction state validation</li>
 *   <li>{@link EntityProvider} - entity information lookup</li>
 *   <li>{@link EcdhSessionProvider} - ECDH session key retrieval</li>
 *   <li>{@link StorageProvider} - blockchain DID Document retrieval, VcMeta registration</li>
 *   <li>{@link WalletProvider} - Certificate VC signing with TAS assert key</li>
 *   <li>{@link DidDocumentProvider} - DID Document lookup for DID Auth validation</li>
 *   <li>{@link CertificateProvider} - Certificate VC publishing (optional)</li>
 *   <li>{@link VcBuilder} - fluent unsigned VC construction</li>
 * </ul>
 *
 * <p><b>Server responsibilities (outside this class):</b></p>
 * <ul>
 *   <li>Saving Transaction and SubTransaction records</li>
 *   <li>Updating Entity status</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class EntityEnrollmentProtocolImpl implements EntityEnrollmentProtocol {

    // Provider interfaces
    private final TransactionProvider transactionProvider;
    private final EntityProvider entityProvider;
    private final EcdhSessionProvider ecdhSessionProvider;
    private final StorageProvider storageProvider;
    private final WalletProvider walletProvider;
    private final DidDocumentProvider didDocumentProvider;
    private final CertificateProvider certificateProvider;
    private final VcBuilder vcBuilder;

    /**
     * Step 1: Propose Enroll Entity.
     */
    @Override
    public ProposeEnrollEntityResponse proposeEnrollEntity(ProposeEnrollEntityRequest request)
            throws TaSdkException {

        log.debug("=== P120 Step 1: Propose Enroll Entity ===");
        log.debug("Request ID: {}", request.getId());

        try {
            // 1. Generate transaction ID
            String txId = UUID.randomUUID().toString();
            log.debug("Generated txId: {}", txId);

            // 2. Generate authNonce (16 bytes, multibase-encoded)
            byte[] authNonceBytes = BaseCryptoUtil.generateNonce(16);
            String authNonce = BaseMultibaseUtil.encode(authNonceBytes);
            log.debug("Generated authNonce: {}", authNonce);

            // 3. Build response
            ProposeEnrollEntityResponse response = ProposeEnrollEntityResponse.builder()
                    .txId(txId)
                    .authNonce(authNonce)
                    .build();

            log.info("P120 Step 1 completed successfully - txId: {}", txId);
            return response;

        } catch (TaSdkException e) {
            log.error("P120 Step 1 failed (TaSdkException): {} - {}",
                e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P120 Step 1 failed (unexpected): {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to propose enroll entity: " + e.getMessage());
        }
    }

    /**
     * Step 3: Request Enroll Entity.
     */
    @Override
    public RequestEnrollEntityResponse requestEnrollEntity(RequestEnrollEntityRequest request)
            throws TaSdkException {

        log.debug("=== P120 Step 3: Request Enroll Entity ===");
        log.debug("TxId: {}", request.getTxId());
        log.debug("Entity DID: {}", request.getDidAuth().getDid());

        try {
            // 1. Retrieve transaction (for authNonce)
            log.debug("Step 3.1: Retrieving transaction");
            Transaction transaction = transactionProvider.getTransaction(request.getTxId());
            if (transaction == null) {
                throw new TaSdkException(TaSdkErrorCode.SDK_TRANSACTION_NOT_FOUND);
            }

            // 2. Validate DID Auth
            log.debug("Step 3.2: Validating DID Auth");
            validateDidAuth(request.getDidAuth(), transaction.getAuthNonce());

            // 3. Retrieve entity information
            log.debug("Step 3.3: Retrieving entity information");
            Entity entity = entityProvider.getEntityByDid(request.getDidAuth().getDid());
            if (entity == null) {
                throw new TaSdkException(TaSdkErrorCode.SDK_ENTITY_NOT_FOUND);
            }
            log.debug("Entity found - name: {}, role: {}", entity.getName(), entity.getRole());

            // 4. Generate Certificate VC
            log.debug("Step 3.4: Generating Certificate VC");
            VerifiableCredential certificateVc = generateCertificateVc(entity, request.getDn());
            log.debug("Certificate VC generated - vcId: {}", certificateVc.getId());

            // 5. Sign Certificate VC
            log.debug("Step 3.5: Signing Certificate VC with TAS assert key");
            signCertificateVc(certificateVc);

            // 6. Register VcMeta to blockchain
            log.debug("Step 3.6: Registering VcMeta to blockchain");
            registerVcMetaToBlockchain(certificateVc);

            // 6b. Publish Certificate VC (DB 저장 + entity publishedCertificateUrl 업데이트)
            log.debug("Step 3.6b: Publishing Certificate VC");
            String vcUrl = publishCertificateVc(certificateVc);

            // 7. Generate IV for encryption
            byte[] iv = BaseCryptoUtil.generateNonce(16);
            String encodedIv = BaseMultibaseUtil.encode(iv);
            log.debug("Generated IV for VC encryption");

            // 8. Retrieve ECDH session key
            log.debug("Step 3.7: Retrieving ECDH session key");
            String sessionKey = ecdhSessionProvider.getSessionKey(request.getTxId());
            String cipher = ecdhSessionProvider.getCipher(request.getTxId());
            String padding = ecdhSessionProvider.getPadding(request.getTxId());
            log.debug("ECDH session retrieved - cipher: {}, padding: {}", cipher, padding);

            // 9. Encrypt Certificate VC
            log.debug("Step 3.8: Encrypting Certificate VC");
            byte[] encryptedVc = encryptVc(certificateVc, sessionKey, iv, cipher, padding);
            String encodedEncVc = BaseMultibaseUtil.encode(encryptedVc);

            // 10. Build response
            RequestEnrollEntityResponse response = RequestEnrollEntityResponse.builder()
                    .txId(request.getTxId())
                    .iv(encodedIv)
                    .encVc(encodedEncVc)
                    .vcUrl(vcUrl)
                    .build();

            log.info("P120 Step 3 completed successfully - vcId: {}", certificateVc.getId());
            return response;

        } catch (TaSdkException e) {
            log.error("P120 Step 3 failed (TaSdkException): {} - {}",
                e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P120 Step 3 failed (unexpected): {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to request enroll entity: " + e.getMessage());
        }
    }

    /**
     * Step 4: Confirm Enroll Entity.
     */
    @Override
    public ConfirmEnrollEntityResponse confirmEnrollEntity(ConfirmEnrollEntityRequest request)
            throws TaSdkException {

        log.debug("=== P120 Step 4: Confirm Enroll Entity ===");
        log.debug("TxId: {}", request.getTxId());
        log.debug("VC ID: {}", request.getVcId());

        try {
            // Note: Transaction state validation and VC ID verification
            // are performed by the server before calling this method.
            // SDK focuses on business logic only.

            // 1. Build response
            ConfirmEnrollEntityResponse response = ConfirmEnrollEntityResponse.builder()
                    .txId(request.getTxId())
                    .build();

            log.info("P120 Step 4 completed successfully - txId: {}", request.getTxId());
            return response;

        } catch (TaSdkException e) {
            log.error("P120 Step 4 failed (TaSdkException): {} - {}",
                e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P120 Step 4 failed (unexpected): {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Failed to confirm enroll entity: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Private helper methods
    // -------------------------------------------------------------------------

    /**
     * Validate DID Auth.
     *
     * @param didAuth          DID Auth information
     * @param expectedAuthNonce Expected authNonce (from Propose step)
     * @throws TaSdkException if validation fails
     */
    private void validateDidAuth(
            org.omnione.did.common.datamodel.data.did.DidAuth didAuth,
            String expectedAuthNonce) throws TaSdkException {

        // 1. Validate DID format
        if (!DidValidator.isValidDid(didAuth.getDid())) {
            log.error("Invalid DID format: {}", didAuth.getDid());
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID);
        }

        // 2. Validate verificationMethod format
        String verificationMethod = didAuth.getProof().getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            log.error("Invalid verification method format: {}", verificationMethod);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF);
        }

        // 3. Validate authNonce match
        if (!didAuth.getAuthNonce().equals(expectedAuthNonce)) {
            log.error("AuthNonce mismatch");
            throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_VERIFICATION_FAILED,
                "AuthNonce mismatch");
        }

        // 4. Retrieve client DID Document
        DidDocument clientDidDoc = didDocumentProvider.getDidDocument(didAuth.getDid());
        if (clientDidDoc == null) {
            log.error("Client DID Document not found: {}", didAuth.getDid());
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND);
        }

        // 5. Extract public key
        // verificationMethod is a full DID Key URL (e.g. "did:omn:issuer?versionId=1#auth")
        // getPublicKey expects only the fragment (e.g. "auth")
        String publicKey = BaseCoreDidUtil.getPublicKey(clientDidDoc, DidUtil.extractKeyId(verificationMethod));
        if (publicKey == null) {
            log.error("Public key not found in DID Document");
            throw new TaSdkException(TaSdkErrorCode.SDK_KEY_NOT_FOUND);
        }

        // 6. Generate signature message (remove proof, canonicalize, hash)
        byte[] signatureMessage = org.omnione.did.common.domain.signature.SignatureCryptoHelper
            .extractSignatureMessage(didAuth);

        // 7. Verify signature
        org.omnione.did.common.domain.signature.SignatureCryptoHelper.verifySignature(
            publicKey,
            didAuth.getProof().getProofValue(),
            signatureMessage,
            org.omnione.did.common.datamodel.enums.did.ProofType.fromDisplayName(
                didAuth.getProof().getType())
        );

        log.debug("DID Auth validation passed");
    }

    /**
     * Generate Certificate VC for Entity.
     *
     * <p>Uses the fluent {@link VcBuilder} to construct the VC with:
     * subject DN claim, role claim, and evidence.
     * No validity period is set (가입증명서는 유효기간 없음).</p>
     *
     * @param entity Entity information
     * @param dn     Distinguished Name (optional)
     * @return Unsigned Certificate VC
     * @throws TaSdkException if generation fails
     */
    private VerifiableCredential generateCertificateVc(Entity entity, String dn)
            throws TaSdkException {

        try {
            // 1. Pre-compute certVcRef URL
            String certVcUrl = certificateProvider.generateCertificateVcUrl(entity.getDid());

            // 2. Retrieve TAS information
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            String tasDid = tasDidDoc.getId();
            String tasName = tasDidDoc.getController(); // Or from TAS name field

            // 3. Determine subject DN
            String subjectDn = (dn != null && !dn.isEmpty()) ? dn : entity.getDn();
            if (subjectDn == null || subjectDn.isEmpty()) {
                subjectDn = String.format("cn=%s,o=%s", entity.getName(), entity.getRole());
            }

            // 4. Build VC using VcBuilder
            VerifiableCredential vc = vcBuilder
                    .setIssuer(tasDid, tasName, certVcUrl)
                    .setType(List.of(VcType.VERIFIABLE_CREDENTIAL, VcType.CERTIFICATE_VC))
                    .addClaim("org.opendid.v1.subject", subjectDn.getBytes(StandardCharsets.UTF_8))
                    .addClaim("org.opendid.v1.role", entity.getRole().getRawValue().getBytes(StandardCharsets.UTF_8))
                    .setEvidence(buildEvidence(tasDid))
                    .build(entity.getDid());

            log.debug("Certificate VC generated - vcId: {}", vc.getId());
            return vc;

        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to generate Certificate VC: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.CERTIFICATE_VC_GENERATION_FAILED,
                "VC generation error: " + e.getMessage());
        }
    }

    /**
     * Sign Certificate VC with TAS assert key.
     *
     * @param certificateVc VC to sign (modified in-place)
     * @throws TaSdkException if signing fails
     */
    private void signCertificateVc(VerifiableCredential certificateVc) throws TaSdkException {
        try {
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            walletProvider.ensureWalletConnection();
            walletProvider.signVc(certificateVc, tasDidDoc, "assert");
            log.debug("Certificate VC signed successfully");
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to sign Certificate VC: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.CERTIFICATE_VC_SIGNING_FAILED,
                "VC signing error: " + e.getMessage());
        }
    }

    /**
     * Register VcMeta to blockchain.
     *
     * @param certificateVc Signed Certificate VC
     * @throws TaSdkException if registration fails
     */
    private void registerVcMetaToBlockchain(VerifiableCredential certificateVc)
            throws TaSdkException {

        try {
            String certVcRef = certificateProvider.generateCertificateVcUrl(
                certificateVc.getCredentialSubject().getId());
            VcMeta vcMeta = BaseCoreVcUtil.generateVcMeta(certificateVc, certVcRef);
            storageProvider.registerVcMeta(vcMeta);
            log.debug("VcMeta registered to blockchain - vcId: {}", vcMeta.getId());
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to register VcMeta: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.VC_META_REGISTRATION_FAILED,
                "VcMeta registration error: " + e.getMessage());
        }
    }

    /**
     * Encrypt Certificate VC with session key.
     *
     * @param vc         VC to encrypt
     * @param sessionKey Session key (multibase-encoded)
     * @param iv         Initialization vector
     * @param cipher     Cipher algorithm name
     * @param padding    Padding scheme name
     * @return Encrypted VC bytes
     * @throws TaSdkException if encryption fails
     */
    private byte[] encryptVc(
            VerifiableCredential vc,
            String sessionKey,
            byte[] iv,
            String cipher,
            String padding) throws TaSdkException {

        try {
            byte[] sessionKeyBytes = BaseMultibaseUtil.decode(sessionKey);
            byte[] vcBytes = vc.toJson().getBytes(StandardCharsets.UTF_8);

            return BaseCryptoUtil.encrypt(
                vcBytes,
                sessionKeyBytes,
                iv,
                SymmetricCipherType.fromDisplayName(cipher),
                SymmetricPaddingType.fromDisplayName(padding)
            );
        } catch (Exception e) {
            log.error("Failed to encrypt VC: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_ENCRYPTION_FAILED,
                "VC encryption error: " + e.getMessage());
        }
    }

    /**
     * Publish Certificate VC via CertificateProvider.
     *
     * <p>Saves the VC to the server's certificate store, updates
     * the entity's {@code publishedCertificateUrl} in the DB,
     * and returns the published URL.</p>
     *
     * @param certificateVc Signed Certificate VC to publish
     * @return Published Certificate VC URL
     * @throws TaSdkException if publishing fails
     */
    private String publishCertificateVc(VerifiableCredential certificateVc) throws TaSdkException {
        try {
            String certVcRef = certificateProvider.publishCertificateVc(certificateVc);
            log.debug("Certificate VC published - certVcRef: {}", certVcRef);
            return certVcRef;
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to publish Certificate VC: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.CERTIFICATE_VC_PUBLISHING_FAILED,
                "VC publishing error: " + e.getMessage());
        }
    }

    /**
     * Build DocumentVerificationEvidence for Certificate VC.
     *
     * <p>Matches the TA Server's {@code IssueVcService.setCertificateEvidence} logic.
     * The verifier field is set to TAS DID (the issuer of the certificate),
     * not the entity DID.</p>
     *
     * @param tasDid TAS DID (the verifier/issuer of the certificate)
     * @return Evidence object
     */
    private DocumentVerificationEvidence buildEvidence(String tasDid) {
        DocumentVerificationEvidence evidence = new DocumentVerificationEvidence();
        evidence.setType("DocumentVerification");
        evidence.setVerifier(tasDid);
        evidence.setEvidenceDocument("BusinessLicense");
        evidence.setSubjectPresence("Physical");
        evidence.setDocumentPresence("Physical");

        Map<String, String> attribute = new HashMap<>();
        attribute.put("licenseNumber", "1234567890");
        evidence.setAttribute(attribute);

        return evidence;
    }
}
