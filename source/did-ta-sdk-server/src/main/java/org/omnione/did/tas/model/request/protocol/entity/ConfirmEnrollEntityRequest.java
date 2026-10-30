package org.omnione.did.tas.model.request.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * P120 Step 4: Confirm Enroll Entity 요청 DTO.
 *
 * <p>발급받은 VC ID를 포함하여 등록 완료를 확인한다.</p>
 *
 * <p><b>Protocol step:</b> P120-4</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code vcId} - VC ID (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmEnrollEntityRequest {

    /**
     * 메시지 ID (필수).
     */
    private String id;

    /**
     * 거래 ID (필수).
     *
     * <p>Step 1에서 받은 txId.</p>
     */
    private String txId;

    /**
     * VC ID (필수).
     *
     * <p>Step 3에서 받은 Certificate VC의 ID.
     * Transaction.certificateId와 일치해야 한다.</p>
     */
    private String vcId;
}
