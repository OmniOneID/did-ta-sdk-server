package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.data.DidAuth;
import org.omnione.did.tas.model.data.SignedDidDoc;

/**
 * P141 Step 4: Request Update DID Document 요청 DTO.
 *
 * <p>갱신된 DID Document를 블록체인에 등록 요청한다.
 * 클라이언트는 월렛이 서명한 {@link SignedDidDoc}과 {@link DidAuth}를 전송하고,
 * TAS는 이를 검증하여 블록체인에 등록한다.</p>
 *
 * <p><b>Protocol step:</b> P141-4</p>
 * <p><b>Client-provided fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 *   <li>{@code signedDidDoc} - 월렛이 서명한 갱신된 DID Document (필수)</li>
 *   <li>{@code didAuth} - DID Auth 정보 (필수)</li>
 * </ul>
 *
 * <p><b>SDK 처리 내용:</b></p>
 * <ul>
 *   <li>serverToken 검증 ({@link org.omnione.did.tas.provider.TokenProvider})</li>
 *   <li>didAuth 검증 (authNonce 일치 확인)</li>
 *   <li>signedDidDoc 월렛 서명 검증</li>
 *   <li>ownerDidDoc multibase 디코딩 및 키 서명 검증</li>
 *   <li>현재 DID Document 조회 ({@link org.omnione.did.tas.provider.DidDocumentProvider})</li>
 *   <li>DID Document 갱신 검증:
 *     <ul>
 *       <li>버전 검증: versionId = current + 1</li>
 *       <li>시간 검증: updated > current.updated</li>
 *       <li>불변 필드 검증: context, id, controller, created, deactivated</li>
 *     </ul>
 *   </li>
 *   <li>InvokedDidDoc 생성 (TAS invoke 키로 서명)</li>
 *   <li>블록체인에 갱신된 DID Document 등록</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDidDocRequest {

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

    /**
     * 월렛이 서명한 갱신된 DID Document (필수).
     *
     * <p>구조:</p>
     * <ul>
     *   <li>{@code ownerDidDoc} - multibase 인코딩된 OwnerDidDoc JSON
     *       (갱신된 didDoc JSON + proofs: 각 키의 자기서명 목록)</li>
     *   <li>{@code wallet} - 월렛 식별 정보 (walletId, walletDid)</li>
     *   <li>{@code nonce} - 서명 nonce (multibase)</li>
     *   <li>{@code proof} - 월렛 키로 서명한 증명</li>
     * </ul>
     *
     * <p><b>주의:</b> ownerDidDoc의 versionId는 현재 버전 + 1 이어야 하고,
     * updated 시간은 현재 시간보다 이후여야 한다.</p>
     */
    private SignedDidDoc signedDidDoc;

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
