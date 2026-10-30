package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.core.data.rest.IssueVcParam;
import org.omnione.did.data.model.schema.VcSchema;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.provider.VcSchemaProvider;

/**
 * VC Schema Processor for TA SDK.
 *
 * <p>Handles VC schema loading and parsing by combining the
 * {@link VcSchemaProvider} (server-supplied schema JSON) with
 * {@link BaseCoreVcUtil} (OmniOne SDK schema parsing).</p>
 *
 * <p><b>Reused from did-common-sdk (no re-implementation):</b></p>
 * <ul>
 *   <li>{@link BaseCoreVcUtil#parseVcSchema} - Parses schema JSON into VcSchema object</li>
 *   <li>{@link BaseCoreVcUtil#setVcSchema} - Configures IssueVcParam with parsed schema</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * VcSchemaProcessor schemaProcessor = new VcSchemaProcessor(vcSchemaProvider);
 *
 * // Load and configure schema for VC issuance
 * IssueVcParam param = new IssueVcParam();
 * schemaProcessor.configure(param, "certificate-vc-schema-v1");
 *
 * // Or just parse raw schema JSON
 * VcSchema schema = schemaProcessor.parse(schemaJson);
 * }
 * </pre>
 */
@Slf4j
public class VcSchemaProcessor {

    private final VcSchemaProvider vcSchemaProvider;

    /**
     * Create VcSchemaProcessor.
     *
     * @param vcSchemaProvider Server-provided schema JSON supplier
     */
    public VcSchemaProcessor(VcSchemaProvider vcSchemaProvider) {
        this.vcSchemaProvider = vcSchemaProvider;
    }

    /**
     * Parse VC schema from a JSON string.
     *
     * <p>Use this when the schema JSON is already available (e.g., from a
     * previously loaded profile or network response).</p>
     *
     * @param vcSchemaJson VC schema JSON string
     * @return Parsed VcSchema object
     * @throws TaSdkException if parsing fails
     */
    public VcSchema parse(String vcSchemaJson) {
        try {
            return BaseCoreVcUtil.parseVcSchema(vcSchemaJson);
        } catch (CommonSdkException e) {
            log.error("Failed to parse VC schema JSON", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PARAMETER, e);
        }
    }

    /**
     * Load a VC schema from the server by schema identifier.
     *
     * <p>Fetches the JSON from the server via {@link VcSchemaProvider}
     * then parses it into a VcSchema object.</p>
     *
     * @param schemaId Schema identifier (URL or DB key)
     * @return Parsed VcSchema object
     * @throws TaSdkException if schema not found or parsing fails
     */
    public VcSchema load(String schemaId) {
        log.debug("Loading VC schema: {}", schemaId);
        String schemaJson = vcSchemaProvider.getVcSchemaJson(schemaId);
        return parse(schemaJson);
    }

    /**
     * Load schema and configure an IssueVcParam in one step.
     *
     * <p>Convenience method combining {@link #load} and
     * {@link BaseCoreVcUtil#setVcSchema}. Protocol classes call this
     * to prepare the schema field of an issuance parameter object.</p>
     *
     * @param param    IssueVcParam to configure
     * @param schemaId Schema identifier
     * @throws TaSdkException if schema not found or parsing fails
     */
    public void configure(IssueVcParam param, String schemaId) {
        try {
            log.debug("Configuring IssueVcParam with schema: {}", schemaId);
            String schemaJson = vcSchemaProvider.getVcSchemaJson(schemaId);
            BaseCoreVcUtil.setVcSchema(param, schemaJson);
        } catch (CommonSdkException e) {
            log.error("Failed to configure VC schema for: {}", schemaId, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PARAMETER, e);
        }
    }
}
