package org.omnione.did.tas.protocol.wallet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.did.AttestedDidDoc;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.datamodel.enums.did.ProofType;
import org.omnione.did.common.domain.did.DidDocCryptoHelper;
import org.omnione.did.common.domain.signature.SignatureCryptoHelper;
import org.omnione.did.common.util.DidUtil;
import org.omnione.did.common.util.DidValidator;
import org.omnione.did.common.util.did.BaseCoreDidUtil;
import org.omnione.did.common.util.multibase.BaseMultibaseUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.Entity;
import org.omnione.did.tas.model.request.protocol.wallet.RegisterWalletRequest;
import org.omnione.did.tas.model.response.protocol.wallet.RegisterWalletResponse;
import org.omnione.did.tas.provider.CertificateProvider;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.EntityProvider;
import org.omnione.did.tas.provider.StorageProvider;
import org.omnione.did.tas.provider.WalletProvider;
import org.omnione.did.tas.provider.WalletRegistrationProvider;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * P131: 월렛 등록 프로토콜 구현체.
 *
 * <p>Implements the single-step Wallet Registration protocol.
 * Validates the Wallet Provider's attestation, verifies the DID Document key proofs,
 * creates an InvokedDidDoc (TAS-signed), and registers it to the blockchain.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link EntityProvider} - Wallet Provider entity lookup (role/status check)</li>
 *   <li>{@link WalletRegistrationProvider} - walletId/DID duplicate check</li>
 *   <li>{@link StorageProvider} - blockchain DID Document registration</li>
 *   <li>{@link DidDocumentProvider} - Provider DID Document lookup, TAS DID Document</li>
 *   <li>{@link CertificateProvider} - Certificate VC validation, TAS certVcRef generation</li>
 *   <li>{@link WalletProvider} - InvokedDidDoc signing with TAS invoke key</li>
 * </ul>
 *
 * <p><b>Server responsibilities (after SDK returns):</b></p>
 * <ul>
 *   <li>Save Wallet record (walletId, walletDid, entityId, status=CREATED)</li>
 *   <li>Create Transaction (type=WALLET_REGISTRATION, status=COMPLETED)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class WalletRegistrationProtocolImpl implements WalletRegistrationProtocol {

    private final EntityProvider entityProvider;
    private final WalletRegistrationProvider walletRegistrationProvider;
    private final StorageProvider storageProvider;
    private final DidDocumentProvider didDocumentProvider;
    private final CertificateProvider certificateProvider;
    private final WalletProvider walletProvider;

    /**
     * Register Wallet DID Document to blockchain.
     */
    @Override
    public RegisterWalletResponse registerWallet(RegisterWalletRequest request) throws TaSdkException {
        log.debug("=== P131: Register Wallet ===");

        try {
            // Input validation
            if (request == null || request.getAttestedDidDoc() == null) {
                throw new TaSdkException(TaSdkErrorCode.SDK_FIELD_REQUIRED,
                    "request and attestedDidDoc are required");
            }

            AttestedDidDoc attestedDidDoc = request.getAttestedDidDoc();
            String providerDid = attestedDidDoc.getProvider().getDid();

            // Step 1: Validate Wallet Provider (role, status)
            log.debug("Step 1: Validating Wallet Provider - did: {}", providerDid);
            validateWalletProvider(providerDid);

            // Step 2: (Optional) Validate Certificate VC
            String certVcRef = attestedDidDoc.getProvider().getCertVcRef();
            if (certVcRef != null && !certVcRef.isEmpty()) {
                log.debug("Step 2: Validating Certificate VC - ref: {}", certVcRef);
                certificateProvider.validateCertificateVc(certVcRef, providerDid);
            } else {
                log.debug("Step 2: Certificate VC validation skipped (no certVcRef)");
            }

            // Step 3: Validate Attestation Proof
            log.debug("Step 3: Validating Attestation Proof");
            validateAttestationProof(attestedDidDoc, providerDid);

            // Step 4: Check WalletId duplicate
            String walletId = attestedDidDoc.getWalletId();
            log.debug("Step 4: Checking WalletId duplicate - walletId: {}", walletId);
            if (walletRegistrationProvider.checkWalletIdExists(walletId)) {
                throw new TaSdkException(TaSdkErrorCode.WALLET_ID_ALREADY_EXISTS,
                    "WalletId already exists: " + walletId);
            }

            // Step 5: Parse and verify OwnerDidDoc key proofs
            log.debug("Step 5: Parsing and verifying OwnerDidDoc");
            DidDocument ownerDidDoc = parseAndVerifyOwnerDidDoc(attestedDidDoc.getOwnerDidDoc());

            // Step 6: Create InvokedDidDoc (TAS signs the clean DID Document)
            log.debug("Step 6: Creating InvokedDidDoc");
            InvokedDidDoc invokedDidDoc = createInvokedDidDoc(ownerDidDoc);

            // Step 7: Register DID Document to blockchain
            log.debug("Step 7: Registering DID Document to blockchain");
            try {
                storageProvider.registerDidDocument(invokedDidDoc, RoleType.WALLET);
            } catch (TaSdkException e) {
                throw e;
            } catch (Exception e) {
                throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_REGISTRATION_FAILED,
                    "Blockchain registration failed: " + e.getMessage());
            }

            String walletDid = ownerDidDoc.getId();
            log.info("P131 completed successfully - walletDid: {}", walletDid);

            return RegisterWalletResponse.builder()
                    .walletDid(walletDid)
                    .build();

        } catch (TaSdkException e) {
            log.error("P131 failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("P131 unexpected error: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_UNKNOWN_ERROR,
                "Wallet registration failed: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Private helper methods
    // -------------------------------------------------------------------------

    /**
     * Validate Wallet Provider entity.
     *
     * <p>Checks:</p>
     * <ol>
     *   <li>DID format is valid</li>
     *   <li>Entity exists</li>
     *   <li>Entity role == WALLET_PROVIDER</li>
     *   <li>Entity status == COMPLETED</li>
     * </ol>
     *
     * @param providerDid Wallet Provider DID
     * @throws TaSdkException if validation fails
     */
    private void validateWalletProvider(String providerDid) throws TaSdkException {
        // 1. DID format check
        if (!DidValidator.isValidDid(providerDid)) {
            log.error("Invalid provider DID format: {}", providerDid);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
                "Invalid provider DID: " + providerDid);
        }

        // 2. Get entity
        Entity entity;
        try {
            entity = entityProvider.getEntityByDid(providerDid);
        } catch (TaSdkException e) {
            throw new TaSdkException(TaSdkErrorCode.WALLET_PROVIDER_NOT_REGISTERED,
                "Wallet Provider not found: " + providerDid);
        }

        if (entity == null) {
            throw new TaSdkException(TaSdkErrorCode.WALLET_PROVIDER_NOT_REGISTERED,
                "Wallet Provider not found: " + providerDid);
        }

        // 3. Role check: must be WALLET_PROVIDER (spec requirement)
        if (entity.getRole() != RoleType.WALLET_PROVIDER) {
            log.error("Entity role is not WALLET_PROVIDER: {} (actual: {})",
                providerDid, entity.getRole());
            throw new TaSdkException(TaSdkErrorCode.WALLET_PROVIDER_NOT_REGISTERED,
                "Entity role is not WALLET_PROVIDER: " + entity.getRole());
        }

        // 4. Status check: delegated to server (server decides what constitutes a valid state)
        entityProvider.validateEntityStatus(providerDid);

        log.debug("Wallet Provider validated - did: {}, role: {}", providerDid, entity.getRole());
    }

    /**
     * Validate Attestation Proof of the AttestedDidDoc.
     *
     * <p>Checks:</p>
     * <ol>
     *   <li>verificationMethod has valid DID Key URL format</li>
     *   <li>proofPurpose == ASSERTION_METHOD</li>
     *   <li>Provider DID Document exists on blockchain</li>
     *   <li>Public key for verificationMethod exists in Provider DID Document</li>
     *   <li>Signature is valid (extractSignatureMessage + verifySignature)</li>
     * </ol>
     *
     * @param attestedDidDoc AttestedDidDoc to validate
     * @param providerDid    Provider DID (already validated for format)
     * @throws TaSdkException if attestation proof validation fails
     */
    private void validateAttestationProof(AttestedDidDoc attestedDidDoc, String providerDid)
            throws TaSdkException {

        Proof proof = attestedDidDoc.getProof();
        if (proof == null) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF, "Proof is missing");
        }

        // 1. verificationMethod format check
        String verificationMethod = proof.getVerificationMethod();
        if (!DidValidator.isValidDidKeyUrl(verificationMethod)) {
            log.error("Invalid verificationMethod format: {}", verificationMethod);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF,
                "Invalid verificationMethod format: " + verificationMethod);
        }

        // 2. proofPurpose check (must be assertionMethod)
        if (proof.getProofPurpose() != ProofPurpose.ASSERTION_METHOD) {
            log.error("Expected proofPurpose ASSERTION_METHOD, got: {}", proof.getProofPurpose());
            throw new TaSdkException(TaSdkErrorCode.ATTESTATION_VERIFICATION_FAILED,
                "Expected proofPurpose assertionMethod, got: " + proof.getProofPurpose());
        }

        // 3. Get Provider DID Document from blockchain
        DidDocument providerDidDoc;
        try {
            providerDidDoc = didDocumentProvider.getDidDocument(providerDid);
        } catch (Exception e) {
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND,
                "Provider DID Document not found: " + providerDid);
        }

        if (providerDidDoc == null) {
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND,
                "Provider DID Document not found: " + providerDid);
        }

        // 4. Extract public key corresponding to verificationMethod
        String publicKey;
        try {
            publicKey = BaseCoreDidUtil.getPublicKey(providerDidDoc, DidUtil.extractKeyId(verificationMethod));
        } catch (Exception e) {
            log.error("Public key not found for verificationMethod: {}", verificationMethod);
            throw new TaSdkException(TaSdkErrorCode.SDK_KEY_NOT_FOUND,
                "Public key not found: " + verificationMethod);
        }

        // 5. Extract signature message (remove proof.proofValue, canonicalize, hash)
        byte[] signatureMessage;
        try {
            signatureMessage = SignatureCryptoHelper.extractSignatureMessage(attestedDidDoc);
        } catch (Exception e) {
            log.error("Failed to extract signature message: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.ATTESTATION_VERIFICATION_FAILED,
                "Failed to extract signature message: " + e.getMessage());
        }

        // 6. Verify signature
        try {
            ProofType proofType = proof.getType();
            if (proofType == null) {
                throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PROOF, "Proof type is missing");
            }
            SignatureCryptoHelper.verifySignature(
                publicKey, proof.getProofValue(), signatureMessage, proofType);
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Attestation signature verification failed: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.ATTESTATION_VERIFICATION_FAILED,
                "Attestation signature verification failed: " + e.getMessage());
        }

        log.debug("Attestation proof verified successfully");
    }

    /**
     * Parse and verify OwnerDidDoc key proofs.
     *
     * <p>Decodes the multibase-encoded OwnerDidDoc, parses it into a
     * {@link DidDocument}, and verifies all self-signed key proofs.</p>
     *
     * @param ownerDidDocMultibase Multibase-encoded DID Document JSON string
     * @return Parsed and verified DidDocument
     * @throws TaSdkException if decoding, parsing, or proof verification fails
     */
    private DidDocument parseAndVerifyOwnerDidDoc(String ownerDidDocMultibase) throws TaSdkException {
        // 1. Multibase decode
        byte[] decoded;
        try {
            decoded = BaseMultibaseUtil.decode(ownerDidDocMultibase);
        } catch (Exception e) {
            log.error("Failed to decode ownerDidDoc multibase: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_MULTIBASE,
                "Failed to decode ownerDidDoc: " + e.getMessage());
        }

        // 2. Parse JSON into DidDocument
        String ownerDidDocJson = new String(decoded, StandardCharsets.UTF_8);
        DidDocument ownerDidDoc;
        try {
            ownerDidDoc = BaseCoreDidUtil.parseDidDoc(ownerDidDocJson).getDocument();
        } catch (Exception e) {
            log.error("Failed to parse ownerDidDoc JSON: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID,
                "Failed to parse ownerDidDoc: " + e.getMessage());
        }

        // 3. Verify key proofs (each key's self-signature)
        try {
            BaseCoreDidUtil.verifyDidDocKeyProofs(ownerDidDoc);
        } catch (Exception e) {
            log.error("DID Document key proofs verification failed: {}", e.getMessage());
            throw new TaSdkException(TaSdkErrorCode.DID_DOCUMENT_KEY_VERIFICATION_FAILED,
                "DID Document key proofs verification failed: " + e.getMessage());
        }

        log.debug("OwnerDidDoc verified - did: {}", ownerDidDoc.getId());
        return ownerDidDoc;
    }

    /**
     * Create InvokedDidDoc with TAS signature.
     *
     * <p>Steps:</p>
     * <ol>
     *   <li>Retrieve TAS DID Document</li>
     *   <li>Generate TAS certVcRef URL</li>
     *   <li>Determine ProofType from TAS invoke key</li>
     *   <li>Generate unsigned InvokedDidDoc</li>
     *   <li>Sign with TAS invoke key via WalletProvider</li>
     * </ol>
     *
     * @param ownerDidDoc Verified wallet DID Document
     * @return Signed InvokedDidDoc ready for blockchain registration
     * @throws TaSdkException if creation or signing fails
     */
    private InvokedDidDoc createInvokedDidDoc(DidDocument ownerDidDoc) throws TaSdkException {
        try {
            // 1. Get TAS DID Document
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            if (tasDidDoc == null) {
                throw new TaSdkException(TaSdkErrorCode.TAS_DID_DOCUMENT_NOT_FOUND);
            }

            // 2. Generate TAS certVcRef URL
            String tasCertVcRef = certificateProvider.generateCertificateVcUrl(tasDidDoc.getId());

            // 3. Determine ProofType from TAS invoke key
            ProofType proofType = resolveInvokeKeyProofType(tasDidDoc);

            // 4. Generate unsigned InvokedDidDoc
            InvokedDidDoc unsignedDoc = DidDocCryptoHelper.generateInvokedDidDoc(
                tasDidDoc, ownerDidDoc, proofType, tasCertVcRef);

            // 5. Sign with TAS invoke key via WalletProvider
            walletProvider.ensureWalletConnection();
            InvokedDidDoc signedDoc = walletProvider.signInvokedDidDoc(unsignedDoc, "invoke");

            log.debug("InvokedDidDoc created and signed successfully");
            return signedDoc;

        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create InvokedDidDoc: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.INVOKED_DID_DOC_CREATION_FAILED,
                "Failed to create InvokedDidDoc: " + e.getMessage());
        }
    }

    /**
     * Resolve ProofType from the TAS DID Document's capability invocation key.
     *
     * <p>Maps the key type string (e.g., "Secp256r1VerificationKey2018")
     * to the corresponding {@link ProofType} enum.
     * Defaults to {@link ProofType#SECP_256R1_SIGNATURE_2018} if resolution fails.</p>
     *
     * @param tasDidDoc TAS DID Document
     * @return ProofType corresponding to the TAS invoke key algorithm
     */
    private ProofType resolveInvokeKeyProofType(DidDocument tasDidDoc) {
        try {
            List<String> capabilityInvocations = tasDidDoc.getCapabilityInvocation();
            if (capabilityInvocations != null && !capabilityInvocations.isEmpty()) {
                String methodRef = capabilityInvocations.get(0);
                String keyId = methodRef.contains("#")
                    ? methodRef.substring(methodRef.lastIndexOf("#") + 1)
                    : methodRef;

                VerificationMethod vm = BaseCoreDidUtil.getVerificationMethod(tasDidDoc, keyId);
                String keyType = vm.getType();

                if (keyType != null) {
                    if (keyType.contains("Secp256r1")) return ProofType.SECP_256R1_SIGNATURE_2018;
                    if (keyType.contains("Secp256k1")) return ProofType.SECP_256K1_SIGNATURE_2018;
                    if (keyType.contains("Rsa")) return ProofType.RSA_SIGNATURE_2018;
                }
            }
        } catch (Exception e) {
            log.debug("Could not resolve ProofType from TAS DID Document, using default: {}", e.getMessage());
        }
        return ProofType.SECP_256R1_SIGNATURE_2018;
    }
}
