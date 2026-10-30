package org.omnione.did.tas.protocol.user;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.user.ConfirmRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.ProposeRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.RequestRegisterUserRequest;
import org.omnione.did.tas.model.request.protocol.user.RetrieveKycRequest;
import org.omnione.did.tas.model.response.protocol.user.ConfirmRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.ProposeRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.RequestRegisterUserResponse;
import org.omnione.did.tas.model.response.protocol.user.RetrieveKycResponse;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P132: 사용자 등록 프로토콜 인터페이스.
 *
 * <p>사용자의 DID Document를 블록체인에 등록하는 프로토콜이다.</p>
 *
 * <p><b>Note:</b> Step 3 (requestCreateToken)은 서버의 TokenService가 직접 처리하므로
 * 이 인터페이스에 포함되지 않는다.</p>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * Step 1: proposeRegisterUser()
 *   → Generate txId
 *   → Server saves Transaction (PENDING)
 *
 * Step 2: ECDH (공통 ECDH 컴포넌트 처리)
 *   → Session key negotiation
 *
 * Step 3: requestCreateToken() - Server handles directly (TokenService)
 *
 * Step 4: retrieveKyc()
 *   → Validate serverToken (TokenProvider)
 *   → [Option A] kycToken → KycProvider.getPiiFromKycToken
 *   → [Option B] kycTxId → KycProvider.retrieveKycData
 *   → Server saves PII temporarily
 *
 * Step 5: requestRegisterUser()
 *   → Validate serverToken
 *   → Check user DID duplicate
 *   → Validate SignedDidDoc (wallet proof + ownerDidDoc key proofs)
 *   → Create InvokedDidDoc (TAS invoke 키로 서명)
 *   → Register DID Document to blockchain
 *   → Server saves User record
 *
 * Step 6: confirmRegisterUser()
 *   → Validate serverToken
 *   → Server marks registration as COMPLETED
 * </pre>
 */
public interface UserRegistrationProtocol extends Protocol {

    /**
     * Step 1: Propose Register User.
     *
     * <p>사용자 등록 거래를 시작하고 txId를 생성한다.</p>
     *
     * @param request Propose 요청 (메시지 ID)
     * @return Propose 응답 (txId)
     * @throws TaSdkException on unexpected errors
     */
    ProposeRegisterUserResponse proposeRegisterUser(ProposeRegisterUserRequest request)
            throws TaSdkException;

    /**
     * Step 4: Retrieve KYC.
     *
     * <p>서버 토큰을 검증하고 KYC 시스템에서 사용자 PII를 조회한다.</p>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save PII (response.getPiiData()) linked to txId</li>
     *   <li>Update Transaction (SubTransaction: RETRIEVE_KYC)</li>
     * </ul>
     *
     * @param request RetrieveKyc 요청 (txId, serverToken, kycToken? or kycTxId?)
     * @return RetrieveKyc 응답 (txId, piiData[내부전용])
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA32005 - 서버 토큰 유효하지 않음</li>
     *           <li>SSDKTRA32006 - KYC 데이터 조회 실패</li>
     *           <li>SSDKTRA32007 - kycToken과 kycTxId 모두 없음</li>
     *         </ul>
     */
    RetrieveKycResponse retrieveKyc(RetrieveKycRequest request)
            throws TaSdkException;

    /**
     * Step 5: Request Register User.
     *
     * <p>SignedDidDoc를 검증하고 블록체인에 DID Document를 등록한다.</p>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save User record (did, status=ACTIVATED)</li>
     *   <li>Update Transaction (SubTransaction: REQUEST_REGISTER_USER)</li>
     * </ul>
     *
     * @param request RegisterUser 요청 (txId, serverToken, signedDidDoc)
     * @return RegisterUser 응답 (txId)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA32005 - 서버 토큰 유효하지 않음</li>
     *           <li>SSDKTRA32008 - 이미 등록된 사용자 DID</li>
     *           <li>SSDKTRA32010 - SignedDidDoc 월렛 서명 검증 실패</li>
     *           <li>SSDKTRA32011 - ownerDidDoc 키 서명 검증 실패</li>
     *         </ul>
     */
    RequestRegisterUserResponse requestRegisterUser(RequestRegisterUserRequest request)
            throws TaSdkException;

    /**
     * Step 6: Confirm Register User.
     *
     * <p>서버 토큰을 검증하고 사용자 등록 완료를 확인한다.</p>
     *
     * @param request Confirm 요청 (txId, serverToken)
     * @return Confirm 응답 (txId)
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA32005 - 서버 토큰 유효하지 않음</li>
     *         </ul>
     */
    ConfirmRegisterUserResponse confirmRegisterUser(ConfirmRegisterUserRequest request)
            throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P132";
    }

    @Override
    default String getProtocolName() {
        return "User Registration Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
