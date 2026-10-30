package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P141 Step 1: Propose Update DID Document 요청 DTO.
 *
 * <p>사용자 DID Document 갱신 거래를 시작하는 요청이다.
 * TAS는 이 요청을 받아 txId와 authNonce를 생성하고 응답한다.</p>
 *
 * <p><b>Protocol step:</b> P141-1</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code did} - 갱신할 사용자 DID (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeUpdateDidDocRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;

    /**
     * 갱신할 사용자 DID (필수).
     *
     * <p>DID Document를 갱신하려는 사용자의 DID.
     * 사용자는 반드시 ACTIVATED 상태여야 한다.</p>
     *
     * <p>Example: "did:omn:user123"</p>
     */
    private String did;
}
