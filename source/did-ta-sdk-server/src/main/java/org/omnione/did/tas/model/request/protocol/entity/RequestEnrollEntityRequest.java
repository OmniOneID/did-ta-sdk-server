package org.omnione.did.tas.model.request.protocol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.common.datamodel.data.did.DidAuth;

/**
 * P120 Step 3: Request Enroll Entity 요청 DTO.
 *
 * <p>DID Auth 정보를 포함하여 Certificate VC 발급을 요청한다.</p>
 *
 * <p><b>Protocol step:</b> P120-3</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code txId} - 거래 ID (필수)</li>
 *   <li>{@code didAuth} - DID Auth 정보 (필수)</li>
 *   <li>{@code dn} - Distinguished Name (선택)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEnrollEntityRequest {

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
     * DID Auth 정보 (필수).
     *
     * <p>Entity의 DID, authNonce, 서명 정보를 포함한다.
     * authNonce는 Step 1에서 받은 값과 일치해야 한다.</p>
     */
    private DidAuth didAuth;

    /**
     * Distinguished Name (선택).
     *
     * <p>Entity의 인증서 DN 정보. Certificate VC의
     * {@code org.opendid.v1.subject} claim에 사용된다.</p>
     *
     * <p>Example: "cn=National ID Issuer,o=Government,c=KR"</p>
     */
    private String dn;
}
