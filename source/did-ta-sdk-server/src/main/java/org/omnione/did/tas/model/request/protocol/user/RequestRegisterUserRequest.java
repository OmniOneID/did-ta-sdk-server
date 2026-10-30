package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.tas.model.data.SignedDidDoc;

/**
 * P132 Step 5: Request Register User 요청 DTO.
 *
 * <p>사용자 DID Document 등록을 요청한다. 클라이언트는 월렛이 서명한
 * {@link SignedDidDoc}을 전송하고, TAS는 이를 검증하여 블록체인에 등록한다.</p>
 *
 * <p><b>Protocol step:</b> P132-5</p>
 * <p><b>Client-provided fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 *   <li>{@code signedDidDoc} - 월렛이 서명한 DID Document (필수)</li>
 * </ul>
 *
 * <p><b>SDK 처리 내용:</b></p>
 * <ul>
 *   <li>serverToken 검증 ({@link org.omnione.did.tas.provider.TokenProvider})</li>
 *   <li>사용자 DID 중복 확인 ({@link org.omnione.did.tas.provider.UserProvider})</li>
 *   <li>ownerDidDoc multibase 디코딩 및 키 서명 검증</li>
 *   <li>signedDidDoc 월렛 서명 검증</li>
 *   <li>InvokedDidDoc 생성 (TAS invoke 키로 서명)</li>
 *   <li>블록체인에 DID Document 등록</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegisterUserRequest {

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

    /**
     * 월렛이 서명한 DID Document (필수).
     *
     * <p>구조:</p>
     * <ul>
     *   <li>{@code ownerDidDoc} - multibase 인코딩된 OwnerDidDoc JSON
     *       (didDoc JSON + proofs: 각 키의 자기서명 목록)</li>
     *   <li>{@code wallet} - 월렛 식별 정보 (walletId, walletDid)</li>
     *   <li>{@code nonce} - 서명 nonce (multibase)</li>
     *   <li>{@code proof} - 월렛 키로 서명한 증명</li>
     * </ul>
     */
    private SignedDidDoc signedDidDoc;
}
