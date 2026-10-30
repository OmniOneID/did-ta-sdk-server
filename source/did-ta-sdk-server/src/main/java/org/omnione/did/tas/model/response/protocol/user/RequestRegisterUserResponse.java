package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P132 Step 5: Request Register User 응답 DTO.
 *
 * <p>사용자 등록 처리 완료 후 거래 ID를 반환한다.
 * 블록체인 DID 등록과 User VC 발급이 완료된 상태이다.</p>
 *
 * <p><b>Protocol step:</b> P132-5</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 ID</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegisterUserResponse {

    /**
     * 거래 ID.
     *
     * <p>Step 6 (confirmRegisterUser) 요청 시 사용한다.</p>
     */
    private String txId;
}
