package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P132 Step 6: Confirm Register User 응답 DTO.
 *
 * <p>사용자 등록 완료 확인 후 거래 ID를 반환한다.
 * 이 응답을 받으면 P132 사용자 등록 프로토콜이 정상 완료된 것이다.</p>
 *
 * <p><b>Protocol step:</b> P132-6</p>
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
public class ConfirmRegisterUserResponse {

    /**
     * 거래 ID.
     *
     * <p>P132 프로토콜이 완료된 거래의 ID.</p>
     */
    private String txId;
}
