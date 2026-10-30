package org.omnione.did.tas.model.response.protocol.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * P131: Register Wallet 응답 DTO.
 *
 * <p>월렛 등록 결과를 담는 DTO이다.
 * SDK는 블록체인에 등록된 Wallet DID를 반환하며,
 * txId는 서버에서 Transaction 생성 후 추가한다.</p>
 *
 * <p><b>Protocol step:</b> P131-1 (단일 단계)</p>
 * <p><b>Response fields:</b></p>
 * <ul>
 *   <li>{@code walletDid} - 블록체인에 등록된 Wallet DID (SDK 반환)</li>
 * </ul>
 *
 * @since 1.0.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWalletResponse {

    /**
     * 등록된 Wallet DID.
     *
     * <p>블록체인에 등록된 Wallet의 DID 식별자.</p>
     */
    private String walletDid;

    /**
     * 거래 ID.
     *
     * <p>서버에서 Transaction 생성 후 채운다.</p>
     */
    private String txId;
}
