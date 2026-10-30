package org.omnione.did.tas.model.response.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P142 Step 5: Confirm Restore DID Document 응답 DTO.
 *
 * <p>DID Document 복구 완료 확인에 대한 응답이다.</p>
 *
 * <p><b>Protocol step:</b> P142-5</p>
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
public class ConfirmRestoreDidDocResponse {

    /**
     * 거래 코드.
     *
     * <p>복구가 완료된 거래 식별자.</p>
     */
    private String txId;
}
