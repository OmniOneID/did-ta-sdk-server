package org.omnione.did.tas.model.response.protocol.tas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P110: TAS 등록 응답 DTO.
 *
 * <p>Returns the transaction ID and the public URL of the issued TAS Certificate VC.
 * Other entities use this URL to retrieve and verify TAS's Certificate VC.</p>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollTasResponse {

    /**
     * 거래 ID.
     *
     * <p>서버가 생성한 감사 추적(audit trail)용 트랜잭션 ID.</p>
     */
    private String txId;

    /**
     * Certificate VC 참조 URL.
     *
     * <p>The public URL where TAS's Certificate VC can be retrieved.
     * Example: {@code https://tas.example.com/certificate-vc/did:omn:tas}</p>
     *
     * <p>This URL is stored in subsequent VC issuances as the issuer's
     * {@code certVcRef} field, allowing relying parties to validate
     * the issuer's authority.</p>
     */
    private String certVcRef;

    @Override
    public String toString() {
        return "EnrollTasResponse{txId='" + txId + "', certVcRef='" + certVcRef + "'}";
    }
}
