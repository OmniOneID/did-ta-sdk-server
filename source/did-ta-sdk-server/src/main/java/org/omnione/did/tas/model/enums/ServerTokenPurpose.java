package org.omnione.did.tas.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Server Token Purpose 열거형.
 *
 * <p>TAS가 발급하는 서버 토큰의 용도를 구분한다.
 * Data Specification의 SERVER_TOKEN_PURPOSE 정의를 따른다.</p>
 *
 * <p><b>P132 User Registration에서 사용되는 값:</b></p>
 * <ul>
 *   <li>{@link #CREATE_DID} (5) - 사용자 DID 생성 및 등록</li>
 *   <li>{@link #CREATE_DID_AND_ISSUE_VC} (13) - DID 생성 + VC 발급 (통합)</li>
 * </ul>
 *
 * @since 1.0.0
 */
public enum ServerTokenPurpose {

    /** DID 생성 (purpose = 5) */
    CREATE_DID(5),

    /** DID 업데이트 (purpose = 6) */
    UPDATE_DID(6),

    /** DID 복원 (purpose = 7) */
    RESTORE_DID(7),

    /** VC 발급 (purpose = 8) */
    ISSUE_VC(8),

    /** VC 폐기 (purpose = 9) */
    REVOKE_VC(9),

    /** VP 제출 (purpose = 10) */
    PRESENT_VP(10),

    /** DID 생성 + VC 발급 통합 (purpose = 13) */
    CREATE_DID_AND_ISSUE_VC(13),
    ;

    private final Integer purpose;

    ServerTokenPurpose(Integer purpose) {
        this.purpose = purpose;
    }

    @JsonValue
    public Integer getPurpose() {
        return purpose;
    }
}
