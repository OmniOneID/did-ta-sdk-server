package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P141 Step 4: Request Update DID Document 응답 DTO.
 *
 * <p>DID Document 블록체인 갱신 완료에 대한 응답이다.</p>
 *
 * <p><b>Protocol step:</b> P141-4</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 코드</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDidDocResponse {

    /**
     * 거래 코드.
     *
     * <p>DID Document 갱신이 완료된 거래 식별자.</p>
     */
    private String txId;
}
