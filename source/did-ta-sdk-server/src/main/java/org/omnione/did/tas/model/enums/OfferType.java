package org.omnione.did.tas.model.enums;

/**
 * Offer 전송 방식 Enum.
 *
 * <p>P142 DID Document 복구 프로토콜에서 사용되는 Offer 전송 방식을 나타낸다.</p>
 *
 * <p><b>전송 방식:</b></p>
 * <ul>
 *   <li>{@link #PUSH} - Push 알림으로 전송</li>
 *   <li>{@link #EMAIL} - Email로 전송</li>
 * </ul>
 *
 * @since 1.0.0
 */
public enum OfferType {

    /**
     * Push 알림으로 전송.
     *
     * <p>사용자의 등록된 디바이스로 Push 알림을 전송한다.
     * Firebase FCM 등의 Push 서비스를 통해 전달된다.</p>
     */
    PUSH,

    /**
     * Email로 전송.
     *
     * <p>사용자의 등록된 이메일 주소로 인증 코드를 전송한다.</p>
     */
    EMAIL
}
