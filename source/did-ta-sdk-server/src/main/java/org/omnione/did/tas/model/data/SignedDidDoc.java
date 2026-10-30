package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omnione.did.common.datamodel.data.signature.Proof;

/**
 * 서명된 DID Document 데이터 클래스 (P132 사용자 등록용).
 *
 * <p>사용자의 DID Document에 월렛이 서명한 데이터 구조이다.
 * Step 5 (requestRegisterUser)에서 클라이언트가 전송한다.</p>
 *
 * <p><b>구조:</b></p>
 * <ul>
 *   <li>{@code ownerDidDoc} - multibase 인코딩된 OwnerDidDoc JSON
 *       <ul>
 *         <li>OwnerDidDoc = didDoc(multibase DidDocument) + proofs(키별 자기서명 목록)</li>
 *       </ul>
 *   </li>
 *   <li>{@code wallet} - 월렛 식별 정보 (walletId, walletDid)</li>
 *   <li>{@code nonce} - 서명 nonce (multibase 인코딩)</li>
 *   <li>{@code proof} - 월렛 키로 서명한 증명</li>
 * </ul>
 *
 * <p><b>SDK 검증 흐름 (Step 5):</b></p>
 * <ol>
 *   <li>ownerDidDoc multibase 디코딩 → JSON 파싱</li>
 *   <li>OwnerDidDoc 내 각 키의 self-signed proof 검증</li>
 *   <li>wallet.proof 서명 검증 (월렛 키 사용)</li>
 * </ol>
 *
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedDidDoc {

    /**
     * multibase 인코딩된 OwnerDidDoc JSON.
     *
     * <p>OwnerDidDoc JSON 구조:
     * <pre>
     * {
     *   "didDoc": "&lt;multibase-encoded DID Document JSON&gt;",
     *   "proofs": [ { key1 proof }, { key2 proof }, ... ]
     * }
     * </pre>
     * </p>
     */
    private String ownerDidDoc;

    /**
     * 월렛 식별 정보 (walletId, walletDid).
     *
     * <p>이 SignedDidDoc에 서명한 월렛의 식별 정보.</p>
     */
    private Wallet wallet;

    /**
     * 서명 nonce (multibase 인코딩).
     *
     * <p>replay attack 방지를 위한 1회용 값.</p>
     */
    private String nonce;

    /**
     * 월렛 키 서명 증명.
     *
     * <p>ownerDidDoc + wallet + nonce 를 월렛 키로 서명한 결과.</p>
     */
    private Proof proof;
}
