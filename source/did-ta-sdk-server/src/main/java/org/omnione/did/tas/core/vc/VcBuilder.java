package org.omnione.did.tas.core.vc;

import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.core.data.rest.ClaimInfo;
import org.omnione.did.core.data.rest.IssueVcParam;
import org.omnione.did.data.model.enums.vc.VcType;
import org.omnione.did.data.model.vc.DocumentVerificationEvidence;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.provider.VcSchemaProvider;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent VC builder for generating unsigned Certificate VCs.
 *
 * <p>This is a stateless, thread-safe factory. Each call to {@link #setIssuer}
 * creates a fresh {@link BuildContext} — one per VC build cycle — so the
 * {@code VcBuilder} instance can be safely shared as a singleton dependency.</p>
 *
 * <p><b>Typical usage (P110):</b></p>
 * <pre>
 * {@code
 * VerifiableCredential vc = vcBuilder
 *     .setIssuer(tas.getDid(), tas.getName(), certVcUrl)
 *     .setType(List.of(VcType.VERIFIABLE_CREDENTIAL, VcType.CERTIFICATE_VC))
 *     .addClaim("org.opendid.v1.subject", subjectDn.getBytes(UTF_8))
 *     .addClaim("org.opendid.v1.role", "Tas".getBytes(UTF_8))
 *     .setValidityPeriod(issuanceDate, expirationDate)
 *     .setEvidence(evidence)
 *     .build(holderDid);
 * }
 * </pre>
 *
 * <p><b>Construction:</b> The server creates a {@code VcBuilder} pre-configured
 * with the {@link VcSchemaProvider} and the schema ID for certificate VCs.</p>
 */
public class VcBuilder {

    private final VcSchemaProvider vcSchemaProvider;
    private final String schemaId;

    /**
     * Create a VcBuilder for the specified VC schema.
     *
     * @param vcSchemaProvider Provider that loads VC schema JSON from the server DB
     * @param schemaId         Schema identifier (e.g., "certificate") passed to the provider
     */
    public VcBuilder(VcSchemaProvider vcSchemaProvider, String schemaId) {
        this.vcSchemaProvider = vcSchemaProvider;
        this.schemaId = schemaId;
    }

    /**
     * Start a new build context with issuer information.
     *
     * <p>Loads the VC schema via {@link VcSchemaProvider} and initialises a
     * fresh {@link BuildContext}. This is the entry point for the fluent chain.</p>
     *
     * @param did        Issuer DID
     * @param name       Issuer name
     * @param certVcRef  Issuer's Certificate VC URL (may be null for TAS first issuance)
     * @return New {@link BuildContext} for further configuration
     * @throws TaSdkException if schema loading fails
     */
    public BuildContext setIssuer(String did, String name, String certVcRef) {
        String schemaJson = vcSchemaProvider.getVcSchemaJson(schemaId);
        try {
            IssueVcParam param = new IssueVcParam();
            BaseCoreVcUtil.setVcSchema(param, schemaJson);
            BaseCoreVcUtil.setIssuer(param, did, name, certVcRef);
            return new BuildContext(param);
        } catch (CommonSdkException e) {
            throw new TaSdkException(TaSdkErrorCode.CERTIFICATE_VC_GENERATION_FAILED,
                "Failed to initialize VcBuilder: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Fluent build context (one instance per build cycle)
    // -------------------------------------------------------------------------

    /**
     * Fluent build context for a single VC issuance.
     *
     * <p>Not thread-safe; do not share across threads. A new instance is created
     * by {@link VcBuilder#setIssuer} on every call.</p>
     */
    public static class BuildContext {

        private final IssueVcParam param;
        private final HashMap<String, ClaimInfo> claims = new HashMap<>();

        private BuildContext(IssueVcParam param) {
            this.param = param;
        }

        /**
         * Set the VC types.
         *
         * @param types List of {@link VcType} values
         * @return this context (for chaining)
         */
        public BuildContext setType(List<VcType> types) {
            BaseCoreVcUtil.setVcTypes(param, types);
            return this;
        }

        /**
         * Add a claim with a raw byte-array value.
         *
         * <p>The value should be UTF-8 encoded string bytes in most cases.</p>
         *
         * @param code  Claim code (e.g., "org.opendid.v1.subject")
         * @param value Claim value as UTF-8 bytes
         * @return this context (for chaining)
         */
        public BuildContext addClaim(String code, byte[] value) {
            ClaimInfo claim = new ClaimInfo();
            claim.setCode(code);
            claim.setValue(value);
            claims.put(code, claim);
            return this;
        }

        /**
         * Set validity period from ISO 8601 UTC strings.
         *
         * @param validFrom  ISO 8601 UTC string (e.g., "2026-02-19T00:00:00Z")
         * @param validUntil ISO 8601 UTC string
         * @return this context (for chaining)
         */
        public BuildContext setValidityPeriod(String validFrom, String validUntil) {
            param.setValidFrom(ZonedDateTime.parse(validFrom));
            param.setValidUntil(ZonedDateTime.parse(validUntil));
            return this;
        }

        /**
         * Set validity period from ZonedDateTime values.
         *
         * @param validFrom  Start of validity period
         * @param validUntil End of validity period
         * @return this context (for chaining)
         */
        public BuildContext setValidityPeriod(ZonedDateTime validFrom, ZonedDateTime validUntil) {
            param.setValidFrom(validFrom);
            param.setValidUntil(validUntil);
            return this;
        }

        /**
         * Set document verification evidence.
         *
         * @param evidence {@link DocumentVerificationEvidence} for the VC
         * @return this context (for chaining)
         */
        public BuildContext setEvidence(DocumentVerificationEvidence evidence) {
            BaseCoreVcUtil.setEvidence(param, evidence);
            return this;
        }

        /**
         * Build the unsigned Verifiable Credential.
         *
         * @param holderDid DID of the credential subject (holder)
         * @return Unsigned {@link VerifiableCredential}
         * @throws TaSdkException if VC generation fails
         */
        public VerifiableCredential build(String holderDid) {
            try {
                BaseCoreVcUtil.setClaimInfo(param, claims);
                return BaseCoreVcUtil.generateVc(param, holderDid);
            } catch (CommonSdkException e) {
                throw new TaSdkException(TaSdkErrorCode.CERTIFICATE_VC_GENERATION_FAILED,
                    "Failed to generate VC for holder " + holderDid + ": " + e.getMessage());
            }
        }
    }
}
