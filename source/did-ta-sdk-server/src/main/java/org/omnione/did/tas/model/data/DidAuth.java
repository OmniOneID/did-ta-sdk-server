package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.common.datamodel.data.signature.Proof;

/**
 * DID Auth 데이터 구조.
 *
 * <p>사용자가 자신의 DID 소유권을 증명하기 위한 인증 구조이다.
 * authNonce와 DID를 사용하여 서명하고, 해당 서명을 Proof로 전달한다.</p>
 *
 * <p><b>사용 프로토콜:</b></p>
 * <ul>
 *   <li>P141 Step 4: DID Document 갱신 시 DID Auth 검증</li>
 *   <li>P143 Step 4: DID Document 복원 시 DID Auth 검증</li>
 * </ul>
 *
 * <p><b>구조:</b></p>
 * <ul>
 *   <li>{@code did} - 사용자 DID</li>
 *   <li>{@code authNonce} - Step 1에서 받은 authNonce</li>
 *   <li>{@code proof} - DID의 authentication 키로 생성한 서명</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DidAuth {

    /**
     * 사용자 DID (필수).
     *
     * <p>DID Document를 갱신/복원하려는 사용자의 DID.</p>
     *
     * <p>Example: "did:omn:user123"</p>
     */
    private String did;

    /**
     * DID Auth용 Nonce (필수).
     *
     * <p>Step 1에서 TAS로부터 받은 authNonce 값.
     * 이 값을 사용하여 서명을 생성한다.</p>
     *
     * <p>Format: multibase (16-byte)</p>
     */
    private String authNonce;

    /**
     * DID Auth 서명 Proof (필수).
     *
     * <p>사용자가 자신의 DID Document의 authentication 키로 생성한 서명.
     * 서명 대상: {@code did + authNonce}</p>
     *
     * <p>verificationMethod는 반드시 authentication purpose 키여야 한다.</p>
     */
    private Proof proof;
}
