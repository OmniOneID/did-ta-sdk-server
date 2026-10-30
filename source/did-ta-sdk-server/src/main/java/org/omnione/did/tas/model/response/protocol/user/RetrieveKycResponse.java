package org.omnione.did.tas.model.response.protocol.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.domain.PiiData;

/**
 * P132 Step 4: Retrieve KYC 응답 DTO.
 *
 * <p>KYC 조회 완료 후 거래 ID를 반환한다.
 * PII는 서버 내부에 저장되고 외부로 노출되지 않는다.</p>
 *
 * <p><b>Protocol step:</b> P132-4</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 ID</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveKycResponse {

    /**
     * 거래 ID.
     *
     * <p>Step 5 (requestRegisterUser) 요청 시 사용한다.</p>
     */
    private String txId;

    /**
     * KYC에서 조회한 PII 데이터 (내부 전용 - 클라이언트에 전달하지 않음).
     *
     * <p>서버는 이 값을 txId와 연결하여 임시 저장하고 Step 5에서 사용한다.
     * name, birthDate, pii(CI), phoneNumber, address 등을 포함한다.</p>
     */
    @JsonIgnore
    private PiiData piiData;
}
