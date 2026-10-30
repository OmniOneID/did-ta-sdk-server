package org.omnione.did.tas.model.enums;

/**
 * User 상태 Enum.
 *
 * <p>사용자의 현재 상태를 나타낸다.</p>
 *
 * <p><b>상태 설명:</b></p>
 * <ul>
 *   <li>{@link #ACTIVATED} - 활성화됨 (정상 사용 가능)</li>
 *   <li>{@link #DEACTIVATED} - 비활성화됨 (사용 불가)</li>
 *   <li>{@link #REVOKED} - 폐기됨 (영구 사용 불가)</li>
 *   <li>{@link #TERMINATED} - 탈퇴됨 (계정 삭제)</li>
 * </ul>
 *
 * @since 1.0.0
 */
public enum UserStatus {

    /**
     * 활성화됨.
     *
     * <p>사용자가 정상적으로 서비스를 이용할 수 있는 상태.
     * DID Document 갱신, VC 발급 등 모든 기능 사용 가능.</p>
     */
    ACTIVATED,

    /**
     * 비활성화됨.
     *
     * <p>임시로 서비스 이용이 중지된 상태.
     * 관리자 조치 또는 사용자 요청으로 비활성화될 수 있으며,
     * 재활성화 가능.</p>
     */
    DEACTIVATED,

    /**
     * 폐기됨.
     *
     * <p>영구적으로 사용이 불가능한 상태.
     * 보안 사고, 규정 위반 등으로 인해 폐기된 계정.</p>
     */
    REVOKED,

    /**
     * 탈퇴됨.
     *
     * <p>사용자가 서비스에서 탈퇴한 상태.
     * 개인정보는 삭제되며, 복구 불가능.</p>
     */
    TERMINATED
}
