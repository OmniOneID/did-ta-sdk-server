package org.omnione.did.tas.model.request.protocol.tas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * P110: TAS 등록 요청 DTO.
 *
 * <p>TAS self-enrollment request. The pre-distributed registration password
 * authorizes the one-time enrollment operation.</p>
 *
 * <p><b>Security notes:</b></p>
 * <ul>
 *   <li>Password should be at least 32 characters</li>
 *   <li>Store in environment variable or Vault, not in config files</li>
 *   <li>Change password after successful enrollment</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollTasRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;

    /**
     * 등록 요청 정보 (필수).
     */
    private Request request;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        /**
         * TAS 등록 비밀번호 (필수).
         *
         * <p>Must match the password configured on the server side via
         * {@link org.omnione.did.tas.provider.TasProvider#getTasPassword()}.</p>
         */
        private String password;
    }

    @Override
    public String toString() {
        return "EnrollTasRequest{id='" + id + "', request=[MASKED]}";
    }
}
