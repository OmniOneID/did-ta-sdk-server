package org.omnione.did.tas.protocol.diddoc;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.user.*;
import org.omnione.did.tas.model.response.protocol.user.*;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P142: DID Document 복구 프로토콜 인터페이스.
 *
 * <p>비활성화된 사용자의 DID Document를 안전하게 복구하는 프로토콜이다.
 * Offer 생성/검증은 서버가 별도 API(/offer-restore-did/push|email)로 처리한다.</p>
 *
 * <p><b>Note:</b> ECDH와 CreateToken은 공통 컴포넌트를 사용하므로
 * 이 인터페이스에는 핵심 3개 메서드만 정의한다.</p>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * [서버 선행] offerRestoreDidPush/Email()
 *   → Create Offer (offerId) + Send Push/Email
 *
 * Step 1: proposeRestoreDidDoc()
 *   → Validate offer (server responsibility)
 *   → Generate txId, authNonce
 *   → Server saves Transaction (PENDING)
 *
 * ECDH (공통 컴포넌트)
 *   → Session key negotiation
 *
 * CreateToken (공통 컴포넌트)
 *   → purpose=7 (RESTORE_DID)
 *   → Generate serverToken
 *
 * Step 4: requestRestoreDidDoc() ⭐ (핵심)
 *   → Validate serverToken
 *   → Validate didAuth (authNonce 일치, DID 일치, 서명 검증)
 *   → Update DID Document status to ACTIVATED on blockchain
 *
 * Step 5: confirmRestoreDidDoc()
 *   → Validate serverToken
 *   → Server updates User status (DEACTIVATED → ACTIVATED)
 *   → Server marks restore as COMPLETED
 * </pre>
 */
public interface DidDocRestoreProtocol extends Protocol {

    /**
     * Step 1: Propose Restore DID Document.
     *
     * <p>DID Document 복구 거래를 시작하고 txId 및 authNonce를 생성한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>txId 생성 (UUID)</li>
     *   <li>authNonce 생성 (16-byte, multibase)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Offer 검증 (서버가 호출 전에 수행)</li>
     *   <li>User 상태 검증 (DEACTIVATED 필수)</li>
     *   <li>Save Transaction (type=RESTORE_DIDDOC, status=PENDING, did, authNonce)</li>
     *   <li>Save SubTransaction (step=1, type=PROPOSE_RESTORE_DIDDOC)</li>
     * </ul>
     *
     * @param request Propose 요청 (did)
     * @return Propose 응답 (txId, authNonce)
     * @throws TaSdkException if generation fails
     */
    ProposeRestoreDidDocResponse proposeRestoreDidDoc(ProposeRestoreDidDocRequest request)
            throws TaSdkException;

    /**
     * Step 4: Request Restore DID Document.
     *
     * <p>DID Auth를 검증하고 블록체인의 DID Document 상태를 ACTIVATED로 변경한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>serverToken 검증 (TokenProvider)</li>
     *   <li>didAuth 검증: authNonce 일치, DID 일치, 서명 검증 (암호학적)</li>
     *   <li>블록체인 DID Document 상태 ACTIVATED로 변경 (StorageProvider)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save SubTransaction (step=4, type=REQUEST_RESTORE_DIDDOC)</li>
     * </ul>
     *
     * @param request RestoreDidDoc 요청 (txId, serverToken, didAuth)
     * @return RestoreDidDoc 응답 (txId)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA19003 - 제공된 토큰이 유효하지 않습니다</li>
     *           <li>SSDKTRA16520 - 'authNonce' 가 일치하지 않습니다</li>
     *           <li>SSDKTRA18016 - DID Document 상태 변경에 실패했습니다</li>
     *         </ul>
     */
    RequestRestoreDidDocResponse requestRestoreDidDoc(RequestRestoreDidDocRequest request)
            throws TaSdkException;

    /**
     * Step 5: Confirm Restore DID Document.
     *
     * <p>서버 토큰을 검증하고 DID Document 복구 완료를 확인한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>serverToken 검증 (TokenProvider)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Update User status (DEACTIVATED → ACTIVATED)</li>
     *   <li>Save SubTransaction (step=5, type=CONFIRM_RESTORE_DIDDOC)</li>
     *   <li>Update Transaction (status=COMPLETED)</li>
     * </ul>
     *
     * @param request Confirm 요청 (txId, serverToken)
     * @return Confirm 응답 (txId)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA19003 - 제공된 토큰이 유효하지 않습니다</li>
     *         </ul>
     */
    ConfirmRestoreDidDocResponse confirmRestoreDidDoc(ConfirmRestoreDidDocRequest request)
            throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P142";
    }

    @Override
    default String getProtocolName() {
        return "DID Document Restore Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
