package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * P132 Step 6: Confirm Register User 요청 DTO.
 *
 * <p>사용자 등록 완료를 확인하는 요청이다. 클라이언트가 User VC 수신을 확인하면
 * TAS는 거래를 종료하고 등록 완료 처리를 한다.</p>
 *
 * <p><b>Protocol step:</b> P132-6</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 * </ul>
 *
 * <p><b>SDK 처리 내용:</b></p>
 * <ul>
 *   <li>serverToken 검증 ({@link org.omnione.did.tas.provider.TokenProvider})</li>
 *   <li>거래 완료 처리 (서버 책임 - SDK는 처리 완료 신호만 전달)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRegisterUserRequest {

    /**
     * 메시지 ID (필수).
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
