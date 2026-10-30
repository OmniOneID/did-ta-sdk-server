package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.DidAuth;

/**
 * P142 Step 4: Request Restore DID Document 요청 DTO.
 *
 * <p>DID Auth를 검증하고 블록체인의 DID Document 상태를 ACTIVATED로 변경 요청한다.</p>
 *
 * <p><b>Protocol step:</b> P142-4</p>
 * <p><b>Client-provided fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 *   <li>{@code didAuth} - DID Auth 정보 (필수)</li>
 * </ul>
 *
 * <p><b>SDK 처리 내용:</b></p>
 * <ul>
 *   <li>serverToken 검증 ({@link org.omnione.did.tas.provider.TokenProvider})</li>
 *   <li>didAuth 검증 (authNonce 일치, DID 일치, 암호학적 서명 검증)</li>
 *   <li>블록체인 DID Document 상태 ACTIVATED로 변경</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRestoreDidDocRequest {

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

    /**
     * DID Auth 정보 (필수).
     *
     * <p>사용자가 자신의 DID 소유권을 증명하기 위한 인증 구조.</p>
     * <ul>
     *   <li>{@code did} - 사용자 DID</li>
     *   <li>{@code authNonce} - Step 1에서 받은 authNonce</li>
     *   <li>{@code proof} - authentication 키로 생성한 서명</li>
     * </ul>
     */
    private DidAuth didAuth;
}
