package org.omnione.did.tas.model.request.protocol.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * P132 Step 4: Retrieve KYC 요청 DTO.
 *
 * <p>KYC 결과(PII)를 조회하는 요청이다. 서버 토큰으로 인가된 클라이언트가
 * KYC 시스템의 인증 결과를 TAS에 전달하면, TAS는 PII를 조회하여 사용자 등록에 사용한다.</p>
 *
 * <p><b>Protocol step:</b> P132-4</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code serverToken} - 서버 토큰 (필수)</li>
 *   <li>{@code kycToken} - KYC 시스템이 발급한 토큰 (선택 - Option A)</li>
 *   <li>{@code kycTxId} - KYC 시스템의 거래 ID (선택 - Option B)</li>
 * </ul>
 *
 * <p><b>PII 조회 방식 (반드시 하나 선택):</b></p>
 * <ul>
 *   <li><b>Option A (kycToken):</b> KYC 시스템이 발급한 토큰을 디코딩하여 PII 추출</li>
 *   <li><b>Option B (kycTxId):</b> KYC 거래 ID로 외부 KYC 서버에 API 호출</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveKycRequest {

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
     * 서버 토큰 (필수).
     *
     * <p>Step 3에서 받은 serverToken 값. {@link org.omnione.did.tas.provider.TokenProvider}로 검증한다.</p>
     */
    private String serverToken;

    /**
     * KYC 시스템이 발급한 토큰 (선택 - Option A).
     *
     * <p>KYC 시스템이 사용자 인증 완료 후 발급한 토큰.
     * 이 토큰을 {@link org.omnione.did.tas.provider.KycProvider#getPiiFromKycToken(String)}으로
     * 디코딩하여 PII를 추출한다.</p>
     *
     * <p>{@code kycToken}과 {@code kycTxId} 중 하나는 반드시 제공해야 한다.</p>
     */
    private String kycToken;

    /**
     * KYC 시스템의 거래 ID (선택 - Option B).
     *
     * <p>KYC 시스템이 사용자 인증 완료 후 발급한 거래 ID.
     * 이 ID를 {@link org.omnione.did.tas.provider.KycProvider#retrieveKycData(String)}에 전달하여
     * 외부 KYC 서버에서 PII를 조회한다.</p>
     *
     * <p>{@code kycToken}과 {@code kycTxId} 중 하나는 반드시 제공해야 한다.</p>
     */
    private String kycTxId;
}
