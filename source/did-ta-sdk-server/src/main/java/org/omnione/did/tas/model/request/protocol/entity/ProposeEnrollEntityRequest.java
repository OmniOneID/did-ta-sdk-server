package org.omnione.did.tas.model.request.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * P120 Step 1: Propose Enroll Entity 요청 DTO.
 *
 * <p>Entity 등록 거래를 시작하는 요청이다.</p>
 *
 * <p><b>Protocol step:</b> P120-1</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeEnrollEntityRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;
}
