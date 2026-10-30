package org.omnione.did.tas.provider;

import org.omnione.did.data.model.vc.VerifiableCredential;

/**
 * Provider interface for Certificate VC storage and publishing.
 *
 * <p>The TA Server implements this interface to persist Certificate VCs to the
 * database and generate publicly accessible URLs. The SDK delegates these
 * storage operations to keep itself stateless.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P110 TAS 등록 - publishes TAS Certificate VC</li>
 *   <li>P120 Entity 등록 - publishes Entity Certificate VC</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class CertificateProviderImpl implements CertificateProvider {
 *     @Override
 *     public String publishCertificateVc(VerifiableCredential vc) {
 *         certificateVcRepository.save(CertificateVc.builder().vc(vc.toJson()).build());
 *         return generateCertificateVcUrl(vc.getCredentialSubject().getId());
 *     }
 *     @Override
 *     public String generateCertificateVcUrl(String did) {
 *         return serverBaseUrl + "/certificate-vc/" + did;
 *     }
 * }
 * }
 * </pre>
 */
public interface CertificateProvider {

    /**
     * Persist the Certificate VC to the database and return its public URL.
     *
     * <p>Saves the signed Certificate VC to the database and returns the URL
     * where it can be publicly retrieved. This URL is stored as {@code certVcRef}
     * in subsequent VC issuances.</p>
     *
     * @param certificateVc Signed Certificate VC to publish
     * @return Public URL of the published Certificate VC
     * @throws org.omnione.did.tas.exception.TaSdkException if publishing fails
     */
    String publishCertificateVc(VerifiableCredential certificateVc);

    /**
     * Generate the Certificate VC URL for a given DID (without saving).
     *
     * <p>Called before VC generation to pre-compute the {@code certVcRef}
     * that will be embedded in the issuer field of the VC itself.</p>
     *
     * @param did Entity DID
     * @return The URL where the Certificate VC will be published
     */
    String generateCertificateVcUrl(String did);

    /**
     * Retrieve a Certificate VC by its VC ID.
     *
     * @param vcId VC identifier
     * @return Certificate VC
     * @throws org.omnione.did.tas.exception.TaSdkException if not found
     */
    VerifiableCredential getCertificateVc(String vcId);

    /**
     * Validate a Certificate VC referenced by URL.
     *
     * <p>Used in P131 to verify that the Wallet Provider holds a valid
     * Certificate VC issued by TAS. Checks:</p>
     * <ol>
     *   <li>Certificate VC exists at the given URL</li>
     *   <li>VC issuer is TAS</li>
     *   <li>VC subject DID matches {@code providerDid}</li>
     *   <li>VC has not expired</li>
     * </ol>
     *
     * @param certVcRef  URL of the Certificate VC to validate
     * @param providerDid DID of the provider that should be the VC subject
     * @throws org.omnione.did.tas.exception.TaSdkException if validation fails:
     *         <ul>
     *           <li>SSDKTRA13003 - Certificate VC not found</li>
     *           <li>SSDKTRA13004 - Provider DID mismatch</li>
     *           <li>SSDKTRA13005 - Invalid Certificate VC issuer or expired</li>
     *         </ul>
     */
    void validateCertificateVc(String certVcRef, String providerDid);
}
