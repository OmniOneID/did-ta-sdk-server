package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P142 Step 1: Propose Restore DID Document 요청 DTO.
 *
 * <p>사용자 DID Document 복구 거래를 시작하는 요청이다.
 * TAS는 이 요청을 받아 txId와 authNonce를 생성하고 응답한다.</p>
 *
 * <p><b>Protocol step:</b> P142-1</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code offerId} - Offer 단계에서 받은 Offer ID (필수)</li>
 *   <li>{@code did} - 복구할 사용자 DID (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeRestoreDidDocRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;

    /**
     * Offer ID (필수).
     *
     * <p>Offer 단계(Push 또는 Email)에서 받은 offerId.
     * 서버는 이 offerId와 DID가 일치하는지 검증한다.</p>
     */
    private String offerId;

    /**
     * 복구할 사용자 DID (필수).
     *
     * <p>DID Document를 복구하려는 사용자의 DID.
     * 사용자는 반드시 DEACTIVATED 상태여야 한다.</p>
     *
     * <p>Example: "did:omn:user123"</p>
     */
    private String did;
}
