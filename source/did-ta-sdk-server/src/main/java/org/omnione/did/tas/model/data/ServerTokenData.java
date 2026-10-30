package org.omnione.did.tas.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.common.datamodel.data.provider.Provider;
import org.omnione.did.common.datamodel.data.signature.Proof;
import org.omnione.did.tas.model.enums.ServerTokenPurpose;

/**
 * 서버 토큰 데이터 클래스.
 *
 * <p>TAS가 서버 토큰 생성을 위해 빌드하는 데이터 객체이다.
 * TAS assert 키로 서명한 후 SHA256 해시하여 serverToken 값을 생성한다.</p>
 *
 * <p><b>생성 흐름 (Step 3 - requestCreateToken):</b></p>
 * <ol>
 *   <li>proofValue=null 인 상태로 빌드 (unsigned)</li>
 *   <li>{@code JsonUtil.serializeAndSort(unsignedStd)} → Canonical JSON</li>
 *   <li>SHA256(canonicalJson) → signatureMessage</li>
 *   <li>TAS assert 키로 서명 → proofValue</li>
 *   <li>proofValue를 채워서 signedStd 완성</li>
 *   <li>SHA256({@code JsonUtil.serializeAndSort(signedStd)}) → serverToken bytes</li>
 *   <li>encrypt(serializedSignedStd, sessionKey, iv) → encStd (클라이언트에 전달)</li>
 * </ol>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerTokenData {

    /**
     * 서버 토큰 용도.
     *
     * <p>P132에서는 {@link ServerTokenPurpose#CREATE_DID} (5) 또는
     * {@link ServerTokenPurpose#CREATE_DID_AND_ISSUE_VC} (13) 사용.</p>
     */
    private ServerTokenPurpose purpose;

    /**
     * 월렛 ID.
     *
     * <p>seed.walletInfo.wallet.id 에서 추출한다.</p>
     */
    private String walletId;

    /**
     * CA 앱 ID.
     *
     * <p>seed.caAppInfo.appId 에서 추출한다.</p>
     */
    private String appId;

    /**
     * 토큰 유효 기간 (ISO 8601 UTC).
     *
     * <p>예: "2026-03-01T00:00:00.000000Z"</p>
     */
    private String validUntil;

    /**
     * TAS Provider 정보 (did, certVcRef).
     *
     * <p>토큰을 발급한 TAS의 DID와 Certificate VC URL.</p>
     */
    private Provider provider;

    /**
     * 서명 nonce (multibase 인코딩).
     *
     * <p>replay attack 방지를 위한 1회용 값. 16바이트 랜덤.</p>
     */
    private String nonce;

    /**
     * TAS assert 키 서명 증명.
     *
     * <p>proofValue=null 상태로 먼저 빌드하여 서명 메시지를 추출한 후
     * proofValue를 채운다.</p>
     */
    private Proof proof;
}
