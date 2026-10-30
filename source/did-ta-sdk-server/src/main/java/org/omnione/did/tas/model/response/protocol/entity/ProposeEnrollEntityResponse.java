package org.omnione.did.tas.model.response.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P120 Step 1: Propose Enroll Entity 응답 DTO.
 *
 * <p>거래 ID와 DID Auth용 nonce를 포함한다.</p>
 *
 * <p><b>Protocol step:</b> P120-1</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 ID (UUID)</li>
 *   <li>{@code authNonce} - 인증 nonce (16 bytes, multibase 인코딩)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeEnrollEntityResponse {

    /**
     * 거래 ID (UUID).
     *
     * <p>이후 모든 프로토콜 단계에서 이 txId를 사용하여 거래를 식별한다.</p>
     */
    private String txId;

    /**
     * 인증 nonce (16 bytes, multibase 인코딩).
     *
     * <p>Step 3에서 DID Auth 검증 시 사용된다.</p>
     */
    private String authNonce;
}
