package org.omnione.did.tas.protocol.diddoc;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.user.ConfirmUpdateDidDocRequest;
import org.omnione.did.tas.model.request.protocol.user.ProposeUpdateDidDocRequest;
import org.omnione.did.tas.model.request.protocol.user.RequestUpdateDidDocRequest;
import org.omnione.did.tas.model.response.protocol.user.ConfirmUpdateDidDocResponse;
import org.omnione.did.tas.model.response.protocol.user.ProposeUpdateDidDocResponse;
import org.omnione.did.tas.model.response.protocol.user.RequestUpdateDidDocResponse;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P141: DID Document 갱신 프로토콜 인터페이스.
 *
 * <p>이미 등록된 사용자의 DID Document를 안전하게 갱신하는 5단계 프로토콜이다.</p>
 *
 * <p><b>Note:</b> Step 2 (ECDH)와 Step 3 (CreateToken)은 공통 컴포넌트를 사용하므로
 * 이 인터페이스에는 Step 1, 4, 5만 정의한다.</p>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * Step 1: proposeUpdateDidDoc()
 *   → Generate txId, authNonce
 *   → Validate user status (ACTIVATED 필수)
 *   → Server saves Transaction (PENDING)
 *
 * Step 2: ECDH (공통 ECDH 컴포넌트 처리)
 *   → Session key negotiation
 *
 * Step 3: CreateToken (공통 CreateTokenHelper 처리)
 *   → purpose=6 (UPDATE_DID)
 *   → walletInfo/caAppInfo 검증, ServerTokenData 서명/암호화
 *   → Server saves serverToken
 *
 * Step 4: requestUpdateDidDoc() ⭐ (핵심)
 *   → Validate serverToken
 *   → Validate didAuth (authNonce 일치)
 *   → Validate SignedDidDoc (wallet proof + ownerDidDoc key proofs)
 *   → Retrieve current DID Document (DidDocumentProvider)
 *   → Validate DID Document update:
 *     * versionId = current + 1
 *     * updated > current.updated
 *     * 불변 필드 동일성 (context, id, controller, created, deactivated)
 *   → Create InvokedDidDoc (TAS invoke 키로 서명)
 *   → Register updated DID Document to blockchain
 *
 * Step 5: confirmUpdateDidDoc()
 *   → Validate serverToken
 *   → Server marks update as COMPLETED
 * </pre>
 *
 * <p><b>DID Document 갱신 가능 항목:</b></p>
 * <ul>
 *   <li><b>갱신 필수:</b> versionId (이전 + 1), updated (이전보다 이후)</li>
 *   <li><b>갱신 가능:</b> verificationMethod, service</li>
 *   <li><b>불변:</b> @context, id, controller, created, deactivated</li>
 * </ul>
 */
public interface DidDocUpdateProtocol extends Protocol {

    /**
     * Step 1: Propose Update DID Document.
     *
     * <p>DID Document 갱신 거래를 시작하고 txId 및 authNonce를 생성한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>txId 생성 (UUID)</li>
     *   <li>User 조회 및 상태 확인 (ACTIVATED 필수)</li>
     *   <li>authNonce 생성 (16-byte, multibase)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save Transaction (type=UPDATE_DIDDOC, status=PENDING, did, authNonce)</li>
     *   <li>Save SubTransaction (step=1, type=PROPOSE_UPDATE_DIDDOC)</li>
     * </ul>
     *
     * @param request Propose 요청 (did)
     * @return Propose 응답 (txId, authNonce)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA17002 - 사용자를 찾을 수 없습니다</li>
     *           <li>SSDKTRA17003 - 사용자 상태가 'Activated'가 아닙니다</li>
     *         </ul>
     */
    ProposeUpdateDidDocResponse proposeUpdateDidDoc(ProposeUpdateDidDocRequest request)
            throws TaSdkException;

    /**
     * Step 4: Request Update DID Document.
     *
     * <p>갱신된 DID Document를 검증하고 블록체인에 등록한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>serverToken 검증 (TokenProvider)</li>
     *   <li>didAuth 검증 (authNonce 일치, DID 일치, 서명 검증)</li>
     *   <li>SignedDidDoc 월렛 서명 검증</li>
     *   <li>ownerDidDoc multibase 디코딩 및 키 서명 검증</li>
     *   <li>현재 DID Document 조회 (DidDocumentProvider)</li>
     *   <li>DID Document 검증:
     *     <ul>
     *       <li>버전 검증: updated.versionId == current.versionId + 1</li>
     *       <li>시간 검증: updated.updated > current.updated</li>
     *       <li>불변 필드 검증: context, id, controller, created, deactivated</li>
     *     </ul>
     *   </li>
     *   <li>InvokedDidDoc 생성 (TAS invoke 키로 서명)</li>
     *   <li>블록체인에 갱신된 DID Document 등록 (StorageProvider)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save SubTransaction (step=4, type=REQUEST_UPDATE_DIDDOC)</li>
     * </ul>
     *
     * @param request UpdateDidDoc 요청 (txId, serverToken, signedDidDoc, didAuth)
     * @return UpdateDidDoc 응답 (txId)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA19003 - 제공된 토큰이 유효하지 않습니다</li>
     *           <li>SSDKTRA16520 - 'authNonce' 가 일치하지 않습니다</li>
     *           <li>SSDKTRA16516 - DID Document 키 서명 검증에 실패했습니다</li>
     *           <li>SSDKTRA12009 - Invalid DID Document version</li>
     *           <li>SSDKTRA18006 - 잘못된 updated 입니다</li>
     *           <li>SSDKTRA18007 - 잘못된 context 입니다</li>
     *           <li>SSDKTRA18008 - 잘못된 id 입니다</li>
     *           <li>SSDKTRA18009 - 잘못된 controller 입니다</li>
     *           <li>SSDKTRA18014 - 잘못된 creation 입니다</li>
     *           <li>SSDKTRA18015 - 잘못된 deactivated 입니다</li>
     *           <li>SSDKTRA18016 - DID Document 등록에 실패했습니다</li>
     *         </ul>
     */
    RequestUpdateDidDocResponse requestUpdateDidDoc(RequestUpdateDidDocRequest request)
            throws TaSdkException;

    /**
     * Step 5: Confirm Update DID Document.
     *
     * <p>서버 토큰을 검증하고 DID Document 갱신 완료를 확인한다.</p>
     *
     * <p><b>SDK 처리 내용:</b></p>
     * <ul>
     *   <li>serverToken 검증 (TokenProvider)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save SubTransaction (step=5, type=CONFIRM_UPDATE_DIDDOC)</li>
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
    ConfirmUpdateDidDocResponse confirmUpdateDidDoc(ConfirmUpdateDidDocRequest request)
            throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P141";
    }

    @Override
    default String getProtocolName() {
        return "DID Document Update Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
