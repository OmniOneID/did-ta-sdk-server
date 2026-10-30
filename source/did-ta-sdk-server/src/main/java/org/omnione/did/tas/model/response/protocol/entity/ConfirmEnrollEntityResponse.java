package org.omnione.did.tas.model.response.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P120 Step 4: Confirm Enroll Entity 응답 DTO.
 *
 * <p>등록 완료 확인 응답.</p>
 *
 * <p><b>Protocol step:</b> P120-4</p>
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
public class ConfirmEnrollEntityResponse {

    /**
     * 거래 ID.
     *
     * <p>프로토콜 완료 확인용.</p>
     */
    private String txId;
}
