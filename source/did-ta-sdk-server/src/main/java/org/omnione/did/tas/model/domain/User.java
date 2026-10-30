package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.enums.UserStatus;

/**
 * User domain model.
 *
 * <p>Represents a User as seen by the SDK.
 * The server maps its database entity to this domain object before
 * passing it to the SDK via {@link org.omnione.did.tas.provider.UserProvider}.</p>
 *
 * <p><b>User status:</b></p>
 * <ul>
 *   <li>{@link UserStatus#ACTIVATED} - 정상 사용 가능</li>
 *   <li>{@link UserStatus#DEACTIVATED} - 비활성화됨</li>
 *   <li>{@link UserStatus#REVOKED} - 폐기됨</li>
 *   <li>{@link UserStatus#TERMINATED} - 탈퇴됨</li>
 * </ul>
 *
 * <p><b>Server-side mapping example:</b></p>
 * <pre>
 * {@code
 * User.builder()
 *     .did(userEntity.getDid())
 *     .status(UserStatus.valueOf(userEntity.getStatus().name()))
 *     .pii(userEntity.getPii())
 *     .build()
 * }
 * </pre>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * User DID (e.g., "did:omn:user:12345").
     *
     * <p>사용자의 고유 DID 식별자.</p>
     */
    private String did;

    /**
     * User status.
     *
     * <p>사용자의 현재 상태 (ACTIVATED, DEACTIVATED, REVOKED, TERMINATED).</p>
     */
    private UserStatus status;

    /**
     * PII (Personally Identifiable Information).
     *
     * <p>KYC 검증을 통해 획득한 사용자 개인식별정보.
     * 주로 CI (Connecting Information) 값을 사용.</p>
     *
     * <p><b>보안:</b> 민감 정보이므로 로깅 시 마스킹 필요.</p>
     */
    private String pii;
}
