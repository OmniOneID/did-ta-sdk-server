package org.omnione.did.tas.model.response.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P120 Step 3: Request Enroll Entity 응답 DTO.
 *
 * <p>암호화된 Certificate VC와 암호화 IV를 포함한다.</p>
 *
 * <p><b>Protocol step:</b> P120-3</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code txId} - 거래 ID</li>
 *   <li>{@code iv} - 암호화 IV (16 bytes, multibase 인코딩)</li>
 *   <li>{@code encVc} - 암호화된 Certificate VC (multibase 인코딩)</li>
 *   <li>{@code vcId} - VC ID (서버에서 Transaction 업데이트용)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnrollEntityResponse {

    /**
     * 거래 ID.
     */
    private String txId;

    /**
     * 암호화 IV (16 bytes, multibase 인코딩).
     *
     * <p>Certificate VC 복호화 시 필요한 Initialization Vector.</p>
     */
    private String iv;

    /**
     * 암호화된 Certificate VC (multibase 인코딩).
     *
     * <p>Step 2에서 협상한 세션키와 이 IV를 사용하여 복호화한다.</p>
     */
    private String encVc;

    /**
     * Certificate VC 게시 URL.
     *
     * <p>SDK가 {@code CertificateProvider.publishCertificateVc()} 호출 결과로 채운다.</p>
     */
    private String vcUrl;
}
