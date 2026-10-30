package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * PII (Personally Identifiable Information) domain model.
 *
 * <p>Represents identity information retrieved from the KYC system.
 * The server maps KYC response data to this domain object before
 * returning it to the SDK via {@link org.omnione.did.tas.provider.KycProvider}.</p>
 *
 * <p><b>SDK usage:</b> The SDK uses this object to:</p>
 * <ul>
 *   <li>Check for duplicate users via {@link org.omnione.did.tas.provider.UserProvider}</li>
 *   <li>Build VC claims for the User VC in Step 5 (requestRegisterUser)</li>
 * </ul>
 *
 * <p><b>Server-side mapping example (from KYC API response):</b></p>
 * <pre>
 * {@code
 * PiiData.builder()
 *     .pii(kycResponse.getCi())
 *     .name(kycResponse.getName())
 *     .birthDate(kycResponse.getBirthDate())
 *     .phoneNumber(kycResponse.getPhoneNumber())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PiiData {

    /**
     * PII / CI (Connecting Information) value.
     *
     * <p>A unique identifier derived from the user's identity by the KYC system.
     * Used as the primary key for duplicate user detection.</p>
     */
    private String pii;

    /**
     * User's full name from KYC verification.
     */
    private String name;

    /**
     * User's birth date from KYC verification (format: YYYYMMDD).
     */
    private String birthDate;

    /**
     * User's phone number from KYC verification (optional).
     */
    private String phoneNumber;

    /**
     * User's address from KYC verification (optional).
     */
    private String address;

    /**
     * Additional claims from KYC verification (optional).
     *
     * <p>Key-value pairs for any extra identity attributes provided by the
     * KYC system that do not fit into the standard fields above.</p>
     */
    private Map<String, String> additionalClaims;
}
