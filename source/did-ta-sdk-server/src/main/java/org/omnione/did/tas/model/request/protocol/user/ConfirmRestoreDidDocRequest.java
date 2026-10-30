package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P142 Step 5: Confirm Restore DID Document 요청 DTO.
 *
 * <p>DID Document 복구 완료를 확인하는 요청이다.
 * SDK는 serverToken을 검증하고, Server는 Transaction을 COMPLETED로 갱신한다.</p>
 *
 * <p><b>Protocol step:</b> P142-5</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRestoreDidDocRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;

    /**
     * 거래 ID (필수).
     *
     * <p>Step 1에서 받은 txId.</p>
     */
    private String txId;

    /**
     * 서버 토큰 (필수).
     *
     * <p>ECDH 및 CreateToken 단계에서 받은 serverToken 값.</p>
     */
    private String serverToken;
}
