package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.data.vc.CredentialSubject;
import org.omnione.did.common.datamodel.data.vc.IssueProfile;
import org.omnione.did.common.datamodel.data.vc.Issuer;
import org.omnione.did.common.datamodel.data.vc.VcProfile;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.exception.ErrorCode;
import org.omnione.did.common.service.impl.vc.BaseVcServiceImpl;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.core.data.rest.IssueVcParam;
import org.omnione.did.core.exception.CoreException;
import org.omnione.did.core.manager.VcManager;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.provider.DidDocumentProvider;
import org.omnione.did.tas.provider.VcStorageProvider;

/**
 * TA SDK VC Service (VC Builder).
 *
 * <p>Extends {@link BaseVcServiceImpl} to provide TA-specific VC issuance and
 * verification. TAS always acts as the issuer and signs VCs with its {@code assert} key.</p>
 *
 * <p><b>Inherited from BaseVcServiceImpl (no re-implementation):</b></p>
 * <ul>
 *   <li>{@code issueCredential()} - Full issuance flow: create → sign → save</li>
 *   <li>{@code verifyCredential()} - Verify structure, proof, expiration, revocation</li>
 *   <li>{@code verifyProof()} - Reflection-based proof verification</li>
 *   <li>{@code validateExpiration()} - ISO 8601 expiration check</li>
 * </ul>
 *
 * <p><b>TA-specific implementations:</b></p>
 * <ul>
 *   <li>{@code createVcManager()} - Builds IssueVcParam from IssueProfile/CredentialSubject, generates unsigned VC</li>
 *   <li>{@code signVerifiableCredential()} - Signs with TAS assert key via {@link BaseCoreVcUtil#signVerifiableCredential}</li>
 *   <li>{@code saveVerifiableCredential()} - Delegates to {@link VcStorageProvider}</li>
 *   <li>{@code checkRevocationStatus()} - Delegates to {@link VcStorageProvider}</li>
 * </ul>
 *
 * <p><b>Protocol entry points:</b></p>
 * <ul>
 *   <li>{@code issueCredential(IssueProfile, CredentialSubject)} - Template pattern (inherited)</li>
 *   <li>{@code issueVc(IssueVcParam, String holderDid)} - Direct issuance from pre-configured params</li>
 * </ul>
 *
 * <p><b>Used by protocols:</b></p>
 * <ul>
 *   <li>P110 TAS 등록 - TAS issues its own certificate VC</li>
 *   <li>P120 Entity 등록 - TAS issues certificate VC for entity</li>
 *   <li>P210 VC 발급 - TAS issues VC on behalf of issuer</li>
 * </ul>
 *
 * <p><b>Usage Example (P120 Entity Registration):</b></p>
 * <pre>
 * {@code
 * TaVcService vcService = new TaVcService(
 *     fileWalletAdapter, didDocumentProvider, vcStorageProvider
 * );
 *
 * // Option 1: Direct issuance (protocol classes building IssueVcParam)
 * IssueVcParam param = new IssueVcParam();
 * BaseCoreVcUtil.setVcSchema(param, schemaJson);
 * BaseCoreVcUtil.setIssuer(param, tasDid, tasName, certVcRef);
 * ClaimManager.configure(param, ClaimManager.build("org.opendid.v1.role", "Issuer"));
 * VerifiableCredential vc = vcService.issueVc(param, entityDid);
 *
 * // Option 2: Template pattern (via IssueProfile + CredentialSubject)
 * CredentialSubject subject = new CredentialSubject(entityDid, claims);
 * vcService.issueCredential(issueProfile, subject);
 * }
 * </pre>
 */
@Slf4j
public class TaVcService extends BaseVcServiceImpl {

    private final FileWalletAdapter fileWalletAdapter;
    private final DidDocumentProvider didDocumentProvider;
    private final VcStorageProvider vcStorageProvider;

    /**
     * ThreadLocal to pass the unsigned VC from {@link #createVcManager} to
     * {@link #signVerifiableCredential}, bridging the template method gap.
     *
     * <p>The VcManager's {@code getOriginDataForSign()} requires the VC to be
     * passed explicitly. Since {@code BaseVcServiceImpl} only passes the VcManager
     * between these steps, we store the VC here and clean up after signing.</p>
     */
    private final ThreadLocal<VerifiableCredential> pendingVc = new ThreadLocal<>();

    /**
     * Create TaVcService.
     *
     * @param fileWalletAdapter   Initialized wallet for signing with the assert key
     * @param didDocumentProvider Server-provided TAS DID Document lookup
     * @param vcStorageProvider   Server-provided VC persistence and revocation checking
     */
    public TaVcService(
            FileWalletAdapter fileWalletAdapter,
            DidDocumentProvider didDocumentProvider,
            VcStorageProvider vcStorageProvider) {
        this.fileWalletAdapter = fileWalletAdapter;
        this.didDocumentProvider = didDocumentProvider;
        this.vcStorageProvider = vcStorageProvider;
    }

    // -------------------------------------------------------------------------
    // BaseVcServiceImpl abstract method implementations
    // -------------------------------------------------------------------------

    /**
     * Build IssueVcParam from IssueProfile and CredentialSubject, generate unsigned VC.
     *
     * <p>Extracts schema JSON from {@code IssueProfile.profile.credentialSchema.value},
     * issuer info from {@code IssueProfile.profile.issuer}, and claims from
     * {@code CredentialSubject.claims}. The generated unsigned VC is stored in
     * {@link #pendingVc} ThreadLocal for the subsequent sign step.</p>
     */
    @Override
    protected VcManager createVcManager(IssueProfile issueProfile, CredentialSubject credentialSubject)
            throws CommonSdkException {
        try {
            log.debug("Creating VcManager for holder: {}", credentialSubject.getHolderDid());

            VcProfile vcProfile = issueProfile.getProfile();

            // Configure IssueVcParam from IssueProfile
            IssueVcParam issueVcParam = new IssueVcParam();

            // Schema: use value field (JSON string) from credentialSchema
            String schemaJson = vcProfile.getCredentialSchema().getValue();
            BaseCoreVcUtil.setVcSchema(issueVcParam, schemaJson);

            // Issuer: DID, name, certVcRef from profile.issuer
            Issuer issuer = vcProfile.getIssuer();
            BaseCoreVcUtil.setIssuer(issueVcParam, issuer.getId(), issuer.getName(), issuer.getCertVcRef());

            // Claims from credentialSubject
            if (credentialSubject.getClaims() != null) {
                BaseCoreVcUtil.setClaimInfo(issueVcParam, credentialSubject.getClaims());
            }

            // Generate unsigned VC using OmniOne VcManager
            VcManager vcManager = new VcManager();
            VerifiableCredential unsignedVc = vcManager.issueCredential(issueVcParam, credentialSubject.getHolderDid());

            // Store for signing step (ThreadLocal — cleaned up in signVerifiableCredential)
            pendingVc.set(unsignedVc);

            log.debug("VcManager created, unsigned VC generated for: {}", credentialSubject.getHolderDid());
            return vcManager;

        } catch (CommonSdkException e) {
            throw e;
        } catch (CoreException e) {
            throw new CommonSdkException(ErrorCode.VC_GENERATION_FAILED,
                "Failed to generate VC: " + e.getMessage());
        } catch (Exception e) {
            throw new CommonSdkException(ErrorCode.VC_GENERATION_FAILED,
                "Unexpected error creating VcManager: " + e.getMessage());
        }
    }

    /**
     * Sign the unsigned VC with TAS's assert key.
     *
     * <p>Retrieves the unsigned VC from {@link #pendingVc} ThreadLocal (set during
     * {@link #createVcManager}), then signs it using the TAS wallet's {@code assert}
     * key via {@link BaseCoreVcUtil#signVerifiableCredential}.</p>
     */
    @Override
    protected VerifiableCredential signVerifiableCredential(VcManager vcManager) throws CommonSdkException {
        VerifiableCredential vc = pendingVc.get();
        pendingVc.remove(); // Always clean up ThreadLocal

        try {
            log.debug("Signing VC with TAS assert key");

            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            VerifiableCredential signedVc = BaseCoreVcUtil.signVerifiableCredential(
                vcManager,
                fileWalletAdapter.getWalletManager(),
                tasDidDoc,
                "assert",
                vc
            );

            log.debug("VC signed: {}", signedVc.getId());
            return signedVc;

        } catch (TaSdkException e) {
            throw new CommonSdkException(ErrorCode.VC_SIGNING_FAILED,
                "Failed to retrieve TAS DID Document for VC signing: " + e.getMessage());
        }
    }

    /**
     * Persist the signed VC via {@link VcStorageProvider}.
     */
    @Override
    protected void saveVerifiableCredential(VerifiableCredential vc) throws CommonSdkException {
        try {
            vcStorageProvider.saveVc(vc);
        } catch (TaSdkException e) {
            throw new CommonSdkException(ErrorCode.VC_GENERATION_FAILED,
                "Failed to save VC: " + e.getMessage());
        }
    }

    /**
     * Check revocation status via {@link VcStorageProvider}.
     *
     * <p>If the VC has been revoked, the provider throws
     * {@code TaSdkException(SDK_VC_REVOKED)}.</p>
     */
    @Override
    protected void checkRevocationStatus(String vcId) throws CommonSdkException {
        try {
            vcStorageProvider.checkRevocationStatus(vcId);
        } catch (TaSdkException e) {
            throw new CommonSdkException(ErrorCode.VC_VERIFICATION_FAILED,
                "VC revocation check failed for: " + vcId);
        }
    }

    /**
     * Revoke a VC by ID with the given reason.
     *
     * <p>Delegates to {@link VcStorageProvider#revokeVc} which updates the
     * VC status in the database (and optionally on the blockchain).</p>
     *
     * <p><b>Used by:</b></p>
     * <ul>
     *   <li>P220 VC 폐기 - TAS revokes a user's VC on request</li>
     * </ul>
     *
     * @param vcId   VC identifier to revoke
     * @param reason Revocation reason
     * @throws TaSdkException if revocation fails
     */
    @Override
    public void revokeCredential(String vcId, String reason) throws CommonSdkException {
        try {
            log.debug("Revoking VC: {} reason: {}", vcId, reason);
            vcStorageProvider.revokeVc(vcId, reason);
            log.debug("VC revoked: {}", vcId);
        } catch (TaSdkException e) {
            throw new CommonSdkException(ErrorCode.VC_GENERATION_FAILED,
                "Failed to revoke VC: " + vcId);
        }
    }

    // -------------------------------------------------------------------------
    // Direct issuance API for protocol classes
    // -------------------------------------------------------------------------

    /**
     * Issue and sign a VC directly from a pre-configured {@link IssueVcParam}.
     *
     * <p>Protocol classes (P110, P120, P210) typically use this method when they
     * need fine-grained control over VC parameters (VC types, evidence, validity
     * period, etc.) that cannot be expressed through {@link IssueProfile}
     * and {@link CredentialSubject} alone.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Generate unsigned VC using OmniOne VcManager</li>
     *   <li>Sign with TAS wallet's {@code assert} key</li>
     *   <li>Persist via {@link VcStorageProvider}</li>
     * </ol>
     *
     * @param issueVcParam Pre-configured VC issuance parameters
     *                     (schema, issuer, claims, types, evidence, validity set by caller)
     * @param holderDid    DID of the credential subject (holder)
     * @return Signed VerifiableCredential
     * @throws TaSdkException if VC generation, signing, or storage fails
     */
    public VerifiableCredential issueVc(IssueVcParam issueVcParam, String holderDid) {
        try {
            log.debug("Issuing VC directly for holder: {}", holderDid);

            // Step 1: Generate unsigned VC
            VcManager vcManager = new VcManager();
            VerifiableCredential unsignedVc = vcManager.issueCredential(issueVcParam, holderDid);

            // Step 2: Sign with TAS assert key
            DidDocument tasDidDoc = didDocumentProvider.getTasDidDocument();
            VerifiableCredential signedVc = BaseCoreVcUtil.signVerifiableCredential(
                vcManager,
                fileWalletAdapter.getWalletManager(),
                tasDidDoc,
                "assert",
                unsignedVc
            );

            // Step 3: Save
            vcStorageProvider.saveVc(signedVc);

            log.debug("VC issued and saved: {}", signedVc.getId());
            return signedVc;

        } catch (TaSdkException e) {
            throw e;
        } catch (CommonSdkException e) {
            log.error("VC issuance failed for holder: {}", holderDid, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_VC_ISSUANCE_FAILED, e);
        } catch (Exception e) {
            log.error("Unexpected error during VC issuance for holder: {}", holderDid, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_VC_ISSUANCE_FAILED, e);
        }
    }
}
