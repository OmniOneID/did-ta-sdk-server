package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.common.datamodel.data.provider.Provider;
import org.omnione.did.common.datamodel.data.signature.Proof;

/**
 * CA 앱 인증 정보 데이터 클래스.
 *
 * <p>클라이언트 앱(CA App)의 인증 정보와 CA(Certificate Authority) Provider가
 * 발급한 증명을 포함한다.
 * {@link ServerTokenSeed}의 {@code caAppInfo} 필드로 사용된다.</p>
 *
 * <p><b>검증 내용 (Step 3 - requestCreateToken):</b></p>
 * <ul>
 *   <li>appId가 CA Provider에 등록된 앱인지 확인</li>
 *   <li>proof의 서명이 CA Provider의 키로 올바르게 생성되었는지 검증</li>
 *   <li>nonce가 만료되지 않았는지 확인</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttestedAppInfo {

    /**
     * CA 앱 ID.
     *
     * <p>CA Provider에 등록된 클라이언트 앱의 고유 식별자.</p>
     */
    private String appId;

    /**
     * CA Provider 정보 (did, certVcRef).
     *
     * <p>이 앱을 인증한 CA Provider의 DID와 Certificate VC 참조.</p>
     */
    private Provider provider;

    /**
     * 서명 nonce (multibase 인코딩).
     *
     * <p>replay attack 방지를 위한 1회용 값.</p>
     */
    private String nonce;

    /**
     * CA Provider의 서명 증명.
     *
     * <p>caAppInfo 전체를 CA Provider 키로 서명한 결과.</p>
     */
    private Proof proof;
}
