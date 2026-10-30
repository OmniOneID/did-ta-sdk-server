package org.omnione.did.tas.protocol.entity;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.entity.ConfirmEnrollEntityRequest;
import org.omnione.did.tas.model.request.protocol.entity.ProposeEnrollEntityRequest;
import org.omnione.did.tas.model.request.protocol.entity.RequestEnrollEntityRequest;
import org.omnione.did.tas.model.response.protocol.entity.ConfirmEnrollEntityResponse;
import org.omnione.did.tas.model.response.protocol.entity.ProposeEnrollEntityResponse;
import org.omnione.did.tas.model.response.protocol.entity.RequestEnrollEntityResponse;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P120: Entity 등록 프로토콜 인터페이스.
 *
 * <p>Entity(Issuer, Verifier 등)의 DID 등록 및 Certificate VC 발급을 처리하는 4단계 프로토콜이다.</p>
 *
 * <p><b>Protocol characteristics:</b></p>
 * <ul>
 *   <li>4-step protocol (Propose → ECDH → Request → Confirm)</li>
 *   <li>ECDH-based secure communication (Step 2)</li>
 *   <li>DID Auth verification (Step 3)</li>
 *   <li>Certificate VC issuance with encryption (Step 3)</li>
 *   <li>VcMeta blockchain registration (Step 3)</li>
 * </ul>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * Step 1: proposeEnrollEntity()
 *   → Generate txId, authNonce
 *   → Server saves Transaction (PENDING)
 *
 * Step 2: ECDH (handled by common ECDH component)
 *   → Session key negotiation
 *   → Server saves ECDH session
 *
 * Step 3: requestEnrollEntity()
 *   → Validate DID Auth (authNonce + signature)
 *   → Generate and sign Certificate VC
 *   → Register VcMeta to blockchain
 *   → Encrypt VC with session key
 *   → Server saves Certificate VC
 *
 * Step 4: confirmEnrollEntity()
 *   → Validate VC ID
 *   → Server updates Entity status to COMPLETED
 *   → Server completes Transaction
 * </pre>
 *
 * <p><b>Usage example (server-side):</b></p>
 * <pre>
 * {@code
 * // Step 1
 * ProposeEnrollEntityResponse response1 = entityEnrollmentProtocol.proposeEnrollEntity(
 *     ProposeEnrollEntityRequest.builder().id("msg-001").build()
 * );
 * // Server saves Transaction with txId and authNonce
 *
 * // Step 2: ECDH (handled separately)
 *
 * // Step 3
 * RequestEnrollEntityResponse response3 = entityEnrollmentProtocol.requestEnrollEntity(
 *     RequestEnrollEntityRequest.builder()
 *         .txId(response1.getTxId())
 *         .didAuth(didAuth)
 *         .dn("cn=Issuer,o=Example")
 *         .build()
 * );
 * // Server saves Certificate VC and updates Transaction.certificateId
 *
 * // Step 4
 * ConfirmEnrollEntityResponse response4 = entityEnrollmentProtocol.confirmEnrollEntity(
 *     ConfirmEnrollEntityRequest.builder()
 *         .txId(response1.getTxId())
 *         .vcId(response3.getVcId())
 *         .build()
 * );
 * // Server updates Entity status to COMPLETED
 * }
 * </pre>
 */
public interface EntityEnrollmentProtocol extends Protocol {

    /**
     * Step 1: Propose Enroll Entity.
     *
     * <p>Entity 등록 거래를 시작하고 DID Auth용 nonce를 생성한다.</p>
     *
     * <p><b>SDK responsibilities:</b></p>
     * <ul>
     *   <li>Generate txId (UUID)</li>
     *   <li>Generate authNonce (16 bytes, multibase-encoded)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save Transaction (status=PENDING, type=ENTITY_REGISTRATION)</li>
     *   <li>Save SubTransaction (type=PROPOSE_ENROLL_ENTITY)</li>
     * </ul>
     *
     * @param request Propose request (message ID)
     * @return Propose response (txId, authNonce)
     * @throws TaSdkException on generation failure
     */
    ProposeEnrollEntityResponse proposeEnrollEntity(ProposeEnrollEntityRequest request)
            throws TaSdkException;

    /**
     * Step 2: Request ECDH.
     *
     * <p>공통 ECDH 컴포넌트를 사용한다.</p>
     * <p>별도 설계서 참조: 공통_ECDH_컴포넌트_설계.md</p>
     *
     * @see org.omnione.did.common.service.impl.ecdh.BaseEcdhServiceImpl
     */
    // ECDH step is handled by common ECDH component

    /**
     * Step 3: Request Enroll Entity.
     *
     * <p>DID Auth를 검증하고 Certificate VC를 발급한 후 세션키로 암호화하여 응답한다.</p>
     *
     * <p><b>Server responsibilities (before SDK call):</b></p>
     * <ul>
     *   <li>Validate transaction state (PENDING, not expired, previous step = REQUEST_ECDH)</li>
     * </ul>
     *
     * <p><b>SDK responsibilities:</b></p>
     * <ul>
     *   <li>Validate DID Auth (authNonce match, signature verification)</li>
     *   <li>Generate Certificate VC (subject=Entity name, role=Issuer/Verifier)</li>
     *   <li>Sign Certificate VC (TAS assert key via WalletProvider)</li>
     *   <li>Generate VcMeta and register to blockchain (StorageProvider)</li>
     *   <li>Generate IV (16 bytes)</li>
     *   <li>Encrypt VC with session key (from EcdhSessionProvider)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Publish Certificate VC (optional, via CertificateProvider)</li>
     *   <li>Update Entity DN (if provided in request)</li>
     *   <li>Update Transaction.certificateId</li>
     *   <li>Save SubTransaction (type=REQUEST_ENROLL_ENTITY)</li>
     * </ul>
     *
     * @param request Request (txId, didAuth, dn)
     * @return Response (txId, iv, encVc, vcId)
     * @throws TaSdkException on validation failure, VC generation failure,
     *                        blockchain registration failure, encryption failure
     */
    RequestEnrollEntityResponse requestEnrollEntity(RequestEnrollEntityRequest request)
            throws TaSdkException;

    /**
     * Step 4: Confirm Enroll Entity.
     *
     * <p>Entity 등록을 완료한다.</p>
     *
     * <p><b>Server responsibilities (before SDK call):</b></p>
     * <ul>
     *   <li>Validate transaction state (PENDING, not expired, previous step = REQUEST_ENROLL_ENTITY)</li>
     *   <li>Validate VC ID (request.vcId == Transaction.certificateId)</li>
     * </ul>
     *
     * <p><b>SDK responsibilities:</b></p>
     * <ul>
     *   <li>Return success response (minimal processing)</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Update Entity status to COMPLETED</li>
     *   <li>Update Transaction status to COMPLETED</li>
     *   <li>Save SubTransaction (type=CONFIRM_ENROLL_ENTITY)</li>
     * </ul>
     *
     * <p><b>Note:</b> Step 4 is primarily a server-side completion step.
     * SDK involvement is minimal since all validation is performed by the server.</p>
     *
     * @param request Confirm request (txId, vcId)
     * @return Confirm response (txId)
     * @throws TaSdkException on unexpected errors
     */
    ConfirmEnrollEntityResponse confirmEnrollEntity(ConfirmEnrollEntityRequest request)
            throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P120";
    }

    @Override
    default String getProtocolName() {
        return "Entity Registration Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
