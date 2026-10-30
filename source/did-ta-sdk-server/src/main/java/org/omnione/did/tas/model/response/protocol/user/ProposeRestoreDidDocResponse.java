package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P142 Step 1: Propose Restore DID Document 응답 DTO.
 *
 * <p>DID Document 복구 거래 시작에 대한 응답이다.
 * 클라이언트는 이 응답에서 받은 txId와 authNonce를 후속 단계에서 사용한다.</p>
 *
 * <p><b>Protocol step:</b> P142-1</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 코드</li>
 *   <li>{@code authNonce} - DID Auth용 Nonce (16-byte, multibase)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposeRestoreDidDocResponse {

    /**
     * 거래 코드.
     *
     * <p>TAS가 생성한 고유 거래 식별자.
     * 이후 모든 단계에서 이 txId를 사용한다.</p>
     */
    private String txId;

    /**
     * DID Auth용 Nonce (16-byte, multibase).
     *
     * <p>Step 4에서 DID Auth 검증 시 사용된다.
     * 클라이언트는 이 nonce를 사용하여 DID Auth를 생성해야 한다.</p>
     */
    private String authNonce;
}
