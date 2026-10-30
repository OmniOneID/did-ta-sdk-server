package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P141 Step 5: Confirm Update DID Document 응답 DTO.
 *
 * <p>DID Document 갱신 거래 완료에 대한 응답이다.</p>
 *
 * <p><b>Protocol step:</b> P141-5</p>
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
public class ConfirmUpdateDidDocResponse {

    /**
     * 거래 코드.
     *
     * <p>갱신 거래가 완료된 거래 식별자.</p>
     */
    private String txId;
}
