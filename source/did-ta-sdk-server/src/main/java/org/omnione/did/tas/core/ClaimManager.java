package org.omnione.did.tas.core;

import org.omnione.did.common.util.vc.BaseCoreVcUtil;
import org.omnione.did.core.data.rest.ClaimInfo;
import org.omnione.did.core.data.rest.IssueVcParam;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Claim Manager for TA SDK.
 *
 * <p>Provides static factory methods to build {@link ClaimInfo} objects and
 * {@code HashMap<String, ClaimInfo>} claim maps for VC issuance. Wraps
 * {@link BaseCoreVcUtil#setClaimInfo} for configuring {@link IssueVcParam}.</p>
 *
 * <p><b>Standard OpenDID claim codes:</b></p>
 * <ul>
 *   <li>{@code "org.opendid.v1.subject"} — Entity/user subject name (e.g., {@code "o=TAS"})</li>
 *   <li>{@code "org.opendid.v1.role"}    — Entity role (e.g., {@code "Tas"}, {@code "Issuer"})</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * // Build a claim map for a certificate VC
 * HashMap<String, ClaimInfo> claims = ClaimManager.build(
 *     "org.opendid.v1.subject", "o=MyTAS",
 *     "org.opendid.v1.role",    "Tas"
 * );
 *
 * // Configure IssueVcParam
 * ClaimManager.configure(param, claims);
 *
 * // Or use the CredentialSubject
 * CredentialSubject subject = new CredentialSubject();
 * subject.setHolderDid("did:omn:tas");
 * subject.setClaims(claims);
 * }
 * </pre>
 */
public class ClaimManager {

    private ClaimManager() {
        // Static utility class
    }

    /**
     * Create a single {@link ClaimInfo} from a code and string value.
     *
     * <p>The value is UTF-8 encoded into bytes, matching the OpenDID protocol
     * convention for claim values.</p>
     *
     * @param code  Claim code (e.g., {@code "org.opendid.v1.role"})
     * @param value Claim value as a plain string (e.g., {@code "Issuer"})
     * @return Configured ClaimInfo
     */
    public static ClaimInfo create(String code, String value) {
        ClaimInfo claim = new ClaimInfo();
        claim.setCode(code);
        claim.setValue(value.getBytes(StandardCharsets.UTF_8));
        return claim;
    }

    /**
     * Build a claim map from alternating code-value pairs.
     *
     * <p>The varargs must be pairs: {@code code1, value1, code2, value2, ...}</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * {@code
     * HashMap<String, ClaimInfo> claims = ClaimManager.build(
     *     "org.opendid.v1.subject", "o=MyOrg",
     *     "org.opendid.v1.role",    "Issuer"
     * );
     * }
     * </pre>
     *
     * @param codeValuePairs Alternating code and value strings
     * @return Claim map (code → ClaimInfo)
     * @throws IllegalArgumentException if an odd number of arguments is provided
     */
    public static HashMap<String, ClaimInfo> build(String... codeValuePairs) {
        if (codeValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException(
                "codeValuePairs must be an even number (code-value pairs): " + codeValuePairs.length);
        }
        HashMap<String, ClaimInfo> map = new HashMap<>();
        for (int i = 0; i + 1 < codeValuePairs.length; i += 2) {
            String code = codeValuePairs[i];
            String value = codeValuePairs[i + 1];
            map.put(code, create(code, value));
        }
        return map;
    }

    /**
     * Configure an IssueVcParam with the given claim map.
     *
     * <p>Delegates to {@link BaseCoreVcUtil#setClaimInfo}.</p>
     *
     * @param param  IssueVcParam to configure
     * @param claims Claim map to set
     */
    public static void configure(IssueVcParam param, HashMap<String, ClaimInfo> claims) {
        BaseCoreVcUtil.setClaimInfo(param, claims);
    }
}
