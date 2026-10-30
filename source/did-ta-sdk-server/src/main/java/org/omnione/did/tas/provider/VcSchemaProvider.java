package org.omnione.did.tas.provider;

/**
 * Provider interface for VC Schema retrieval.
 *
 * <p>The TA Server implements this interface to supply VC schema JSON from its
 * database. The SDK remains stateless and delegates schema lookups to the server.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>{@code VcSchemaProcessor} - loading and parsing VC schemas by ID</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class TasVcSchemaProvider implements VcSchemaProvider {
 *
 *     @Override
 *     public String getVcSchemaJson(String schemaId) {
 *         VcSchema schema = vcSchemaRepository.findBySchemaId(schemaId)
 *             .orElseThrow(() -> new TaSdkException(TaSdkErrorCode.SDK_VC_SCHEMA_NOT_FOUND));
 *         return schema.getSchema();
 *     }
 * }
 * }
 * </pre>
 */
public interface VcSchemaProvider {

    /**
     * Retrieve VC schema JSON by schema identifier.
     *
     * @param schemaId Schema identifier (e.g., URL or DB key used to look up the schema)
     * @return VC schema as a JSON string
     * @throws org.omnione.did.tas.exception.TaSdkException if schema not found or retrieval fails
     */
    String getVcSchemaJson(String schemaId);
}
