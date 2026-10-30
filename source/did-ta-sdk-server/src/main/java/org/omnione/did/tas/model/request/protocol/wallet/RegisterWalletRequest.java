package org.omnione.did.tas.model.request.protocol.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.common.datamodel.data.did.AttestedDidDoc;

/**
 * P131: Register Wallet 요청 DTO.
 *
 * <p>Wallet Provider가 생성한 AttestedDidDoc을 포함하여
 * 월렛 등록을 요청하는 DTO이다.</p>
 *
 * <p><b>Protocol step:</b> P131-1 (단일 단계)</p>
 * <p><b>Request fields:</b></p>
 * <ul>
 *   <li>{@code id} - 메시지 ID (필수)</li>
 *   <li>{@code attestedDidDoc} - Provider가 증명한 DID Document (필수)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWalletRequest {

    /**
     * 메시지 ID (필수).
     *
     * <p>클라이언트가 생성한 고유 메시지 식별자.</p>
     */
    private String id;

    /**
     * Provider가 증명한 DID Document (필수).
     *
     * <p>walletId, ownerDidDoc(multibase), provider, nonce, proof 포함.</p>
     */
    private AttestedDidDoc attestedDidDoc;
}
