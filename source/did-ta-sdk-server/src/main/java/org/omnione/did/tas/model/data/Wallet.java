package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Wallet 정보 데이터 클래스.
 *
 * <p>클라이언트 앱과 연결된 월렛의 식별 정보를 담는다.
 * {@link SignedWalletInfo} 내에서 월렛 정보를 표현하는 데 사용된다.</p>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    /**
     * 월렛 ID.
     *
     * <p>TAS에 등록된 월렛의 고유 식별자.</p>
     */
    private String id;

    /**
     * 월렛 DID.
     *
     * <p>블록체인에 등록된 월렛 DID Document의 DID.</p>
     */
    private String did;
}
