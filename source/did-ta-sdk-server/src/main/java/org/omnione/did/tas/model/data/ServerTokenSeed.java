package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.tas.model.enums.ServerTokenPurpose;

/**
 * 서버 토큰 생성을 위한 Seed 데이터 클래스.
 *
 * <p>클라이언트가 Step 3 (requestCreateToken)에서 TAS로 전송하는 토큰 생성 요청의
 * 핵심 데이터이다. TAS는 이 데이터를 검증하고 서명하여 ServerToken을 생성한다.</p>
 *
 * <p><b>TAS의 처리 흐름 (Step 3):</b></p>
 * <ol>
 *   <li>walletInfo.proof 검증 (월렛이 서명한 nonce 유효성)</li>
 *   <li>caAppInfo.proof 검증 (CA Provider가 서명한 앱 인증 유효성)</li>
 *   <li>ServerTokenData 객체 구성 (purpose, walletId, caAppId, validUntil, nonce)</li>
 *   <li>TAS assert 키로 ServerTokenData 서명</li>
 *   <li>SHA256(ServerTokenData JSON) → serverToken 생성</li>
 *   <li>serverToken을 ECDH 세션 키로 암호화하여 응답</li>
 * </ol>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerTokenSeed {

    /**
     * 서버 토큰 용도.
     *
     * <p>P132 사용자 등록에서는 {@link ServerTokenPurpose#CREATE_DID} (5) 또는
     * {@link ServerTokenPurpose#CREATE_DID_AND_ISSUE_VC} (13) 값을 사용한다.</p>
     */
    private ServerTokenPurpose purpose;

    /**
     * 서명된 월렛 정보.
     *
     * <p>월렛 식별 정보(walletId, walletDid)와 월렛 키 서명 증명을 포함한다.
     * TAS는 이 정보를 검증하여 요청이 합법적인 월렛으로부터 왔음을 확인한다.</p>
     */
    private SignedWalletInfo walletInfo;

    /**
     * CA 앱 인증 정보.
     *
     * <p>앱 ID와 CA Provider의 서명 증명을 포함한다.
     * TAS는 이 정보를 검증하여 요청이 CA가 인증한 앱으로부터 왔음을 확인한다.</p>
     */
    private AttestedAppInfo caAppInfo;
}
