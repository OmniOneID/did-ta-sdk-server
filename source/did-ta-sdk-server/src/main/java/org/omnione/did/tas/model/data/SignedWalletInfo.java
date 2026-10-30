package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.common.datamodel.data.signature.Proof;

/**
 * 서명된 월렛 정보 데이터 클래스.
 *
 * <p>클라이언트 앱이 보유한 월렛 정보와 해당 월렛의 서명 증명을 포함한다.
 * {@link ServerTokenSeed}의 {@code walletInfo} 필드로 사용된다.</p>
 *
 * <p><b>검증 내용 (Step 3 - requestCreateToken):</b></p>
 * <ul>
 *   <li>월렛이 TAS에 등록된 월렛인지 확인 (walletId, walletDid 유효성)</li>
 *   <li>proof의 서명이 월렛 키로 올바르게 생성되었는지 검증</li>
 *   <li>nonce가 만료되지 않았는지 확인</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedWalletInfo {

    /**
     * 월렛 식별 정보 (walletId, walletDid).
     */
    private Wallet wallet;

    /**
     * 서명 nonce (multibase 인코딩).
     *
     * <p>replay attack 방지를 위한 1회용 값.</p>
     */
    private String nonce;

    /**
     * 월렛의 서명 증명.
     *
     * <p>walletInfo 전체를 월렛 키로 서명한 결과.</p>
     */
    private Proof proof;
}
