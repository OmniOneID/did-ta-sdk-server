package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P132 Step 1: Propose Register User 응답 DTO.
 *
 * <p>거래 ID를 포함한다.
 * 이후 모든 단계에서 이 txId로 거래를 식별한다.</p>
 *
 * <p><b>Protocol step:</b> P132-1</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 ID (UUID)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeRegisterUserResponse {

    /**
     * 거래 ID (UUID).
     *
     * <p>이후 모든 프로토콜 단계(Step 2~6)에서 이 txId를 사용하여 거래를 식별한다.</p>
     */
    private String txId;
}
