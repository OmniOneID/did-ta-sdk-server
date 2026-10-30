package org.omnione.did.tas.protocol.tas;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.enums.vc.VcType;
import org.omnione.did.data.model.vc.DocumentVerificationEvidence;
import org.omnione.did.data.model.vc.VcMeta;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.tas.core.vc.VcBuilder;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.domain.Tas;
import org.omnione.did.tas.model.request.protocol.tas.EnrollTasRequest;
import org.omnione.did.tas.model.response.protocol.tas.EnrollTasResponse;
import org.omnione.did.tas.provider.CertificateProvider;
import org.omnione.did.tas.provider.StorageProvider;
import org.omnione.did.tas.provider.TasProvider;
import org.omnione.did.tas.provider.WalletProvider;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * P110: TAS 등록 프로토콜 구현체
 *
 * <p>Implements TAS self-enrollment. All crypto operations (VC generation,
 * signing, blockchain registration) are performed here. Server-side
 * operations (password lookup, DB access, transaction creation) are
 * delegated to Provider interfaces.</p>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link TasProvider} — reads TAS info and password from server config/DB</li>
 *   <li>{@link StorageProvider} — reads TAS DID Document, registers VcMeta</li>
 *   <li>{@link WalletProvider} — signs the Certificate VC with TAS assert key</li>
 *   <li>{@link CertificateProvider} — publishes Certificate VC to DB/URL</li>
 *   <li>{@link VcBuilder} — fluent unsigned VC construction</li>
 * </ul>
 *
 * <p><b>Server responsibilities (outside this class):</b></p>
 * <ul>
 *   <li>Creating the audit Transaction after this call returns</li>
 *   <li>Updating TAS status to COMPLETED</li>
 *   <li>Storing the {@code certVcRef} URL on the TAS entity</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
public class TasEnrollmentProtocolImpl implements TasEnrollmentProtocol {

    private final TasProvider tasProvider;
    private final StorageProvider storageProvider;
    private final WalletProvider walletProvider;
    private final CertificateProvider certificateProvider;
    private final VcBuilder vcBuilder;

    @Override
    public EnrollTasResponse enrollTas(EnrollTasRequest request) throws TaSdkException {
        log.debug("=== Starting P110: TAS Enrollment ===");

        try {
            // Step 1: Validate password
            log.debug("Step 1: Validating password");
            validatePassword(request.getRequest().getPassword());

            // Step 2: Retrieve TAS information
            log.debug("Step 2: Retrieving TAS information");
            Tas tas = tasProvider.getTasInfo();

            // Step 3: Retrieve TAS DID Document from blockchain
            log.debug("Step 3: Retrieving TAS DID Document from blockchain");
            DidDocument tasDidDoc = storageProvider.findDidDoc(tas.getDid());
            if (tasDidDoc == null) {
                throw new TaSdkException(TaSdkErrorCode.TAS_DID_DOCUMENT_NOT_FOUND);
            }

            // Step 4: Generate Certificate VC (unsigned)
            log.debug("Step 4: Generating TAS Certificate VC");
            VerifiableCredential certificateVc = generateCertificateVc(tas);

            // Step 5: Sign Certificate VC with TAS assert key
            log.debug("Step 5: Signing Certificate VC with TAS assert key");
            signCertificateVc(certificateVc, tasDidDoc);

            // Step 6: Publish Certificate VC (DB + URL)
            log.debug("Step 6: Publishing Certificate VC");
            String certVcRef = publishCertificateVc(certificateVc);

            // Step 7: Register VcMeta to blockchain
            log.debug("Step 7: Registering VcMeta to blockchain");
            registerVcMetaToBlockchain(certificateVc, certVcRef);

            log.debug("=== P110: TAS Enrollment Completed Successfully ===");
            log.debug("Certificate VC URL: {}", certVcRef);

            return EnrollTasResponse.builder()
                    .certVcRef(certVcRef)
                    .build();

        } catch (TaSdkException e) {
            log.error("TAS enrollment failed: {} - {}", e.getErrorCode().getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during TAS enrollment: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.TAS_ENROLLMENT_FAILED,
                "Unexpected error: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Validate the request password against the server-stored password.
     */
    private void validatePassword(String requestPassword) {
        String storedPassword = tasProvider.getTasPassword();

        if (storedPassword == null || storedPassword.isEmpty()) {
            throw new TaSdkException(TaSdkErrorCode.TAS_PASSWORD_NOT_CONFIGURED);
        }

        if (!storedPassword.equals(requestPassword)) {
            log.warn("Password mismatch — TAS enrollment rejected");
            throw new TaSdkException(TaSdkErrorCode.PASSWORD_MISMATCH);
        }

        log.debug("Password validation successful");
    }

    /**
     * Generate unsigned Certificate VC for TAS.
     *
     * <p>Uses the fluent {@link VcBuilder} to construct the VC with:
     * subject DN claim, role="Tas" claim, and evidence.
     * No validity period is set (가입증명서는 유효기간 없음).</p>
     */
    private VerifiableCredential generateCertificateVc(Tas tas) {
        try {
            // Pre-compute certVcRef (embedded in VC issuer field)
            String certVcUrl = certificateProvider.generateCertificateVcUrl(tas.getDid());

            VerifiableCredential vc = vcBuilder
                    .setIssuer(tas.getDid(), tas.getName(), certVcUrl)
                    .setType(List.of(VcType.VERIFIABLE_CREDENTIAL, VcType.CERTIFICATE_VC))
                    .addClaim("org.opendid.v1.subject",
                            tas.getSubjectDn().getBytes(StandardCharsets.UTF_8))
                    .addClaim("org.opendid.v1.role",
                            "Tas".getBytes(StandardCharsets.UTF_8))
                    .setEvidence(buildEvidence(tas))
                    .build(tas.getDid());

            log.debug("Certificate VC generated — vcId: {}", vc.getId());
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
     * Sign the Certificate VC with TAS's assert key via WalletProvider.
     */
    private void signCertificateVc(VerifiableCredential certificateVc, DidDocument tasDidDoc) {
        try {
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
     * Publish Certificate VC via CertificateProvider and return the URL.
     */
    private String publishCertificateVc(VerifiableCredential certificateVc) {
        try {
            String certVcRef = certificateProvider.publishCertificateVc(certificateVc);
            log.debug("Certificate VC published: {}", certVcRef);
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
     * Register VcMeta to blockchain via StorageProvider.
     *
     * <p>Uses {@link BaseCoreVcUtil#generateVcMeta} to build the VcMeta from
     * the signed VC and the published URL.</p>
     */
    private void registerVcMetaToBlockchain(VerifiableCredential certificateVc, String certVcRef) {
        try {
            VcMeta vcMeta = BaseCoreVcUtil.generateVcMeta(certificateVc, certVcRef);
            storageProvider.registerVcMeta(vcMeta);
            log.debug("VcMeta registered to blockchain — vcId: {}", vcMeta.getId());
        } catch (TaSdkException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to register VcMeta to blockchain: {}", e.getMessage(), e);
            throw new TaSdkException(TaSdkErrorCode.VC_META_REGISTRATION_FAILED,
                "VcMeta registration error: " + e.getMessage());
        }
    }

    /**
     * Build DocumentVerificationEvidence for the TAS Certificate VC.
     */
    private DocumentVerificationEvidence buildEvidence(Tas tas) {
        DocumentVerificationEvidence evidence = new DocumentVerificationEvidence();
        evidence.setType("DocumentVerification");
        evidence.setVerifier(tas.getDid());
        evidence.setEvidenceDocument("BusinessLicense");
        evidence.setSubjectPresence("Physical");
        evidence.setDocumentPresence("Physical");

        Map<String, String> attribute = new HashMap<>();
        attribute.put("licenseNumber", "1234567890");
        evidence.setAttribute(attribute);

        return evidence;
    }
}
