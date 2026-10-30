package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P141 Step 5: Confirm Update DID Document 요청 DTO.
 *
 * <p>DID Document 갱신 거래를 완료하는 요청이다.
 * 클라이언트는 serverToken을 전송하여 거래 완료를 확인한다.</p>
 *
 * <p><b>Protocol step:</b> P141-5</p>
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
public class ConfirmUpdateDidDocRequest {

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
     * <p>Step 3에서 받은 serverToken 값.</p>
     */
    private String serverToken;
}
