package org.omnione.did.tas.exception;

import lombok.Getter;

/**
 * TA SDK Error Code Enum
 *
 * <p>SDK-specific error codes starting with SSDKTRA prefix.</p>
 * <p>Error code format: SSDKTRA{category}{number}</p>
 * <ul>
 *   <li>SSDKTRA001xxx - SDK validation errors</li>
 *   <li>SSDKTRA002xxx - SDK protocol errors</li>
 *   <li>SSDKTRA003xxx - SDK crypto errors</li>
 *   <li>SSDKTRA004xxx - SDK blockchain errors</li>
 *   <li>SSDKTRA005xxx - SDK wallet errors</li>
 *   <li>SSDKTRA006xxx - SDK provider errors</li>
 *   <li>SSDKTRA009xxx - SDK general errors</li>
 * </ul>
 */
@Getter
public enum TaSdkErrorCode {

    // ===== SDK Validation Errors (SSDKTRA001xxx) =====

    SDK_INVALID_DID("SSDKTRA001001", "Invalid DID format.", 400),
    SDK_INVALID_PROOF("SSDKTRA001002", "Invalid proof.", 400),
    SDK_INVALID_SIGNATURE("SSDKTRA001003", "Invalid signature.", 400),
    SDK_INVALID_MULTIBASE("SSDKTRA001004", "Invalid multibase encoding.", 400),
    SDK_FIELD_REQUIRED("SSDKTRA001005", "Required field is missing.", 400),
    SDK_INVALID_TRANSACTION("SSDKTRA001006", "Invalid transaction.", 400),
    SDK_INVALID_PARAMETER("SSDKTRA001007", "Invalid parameter.", 400),
    SDK_INVALID_CONFIGURATION("SSDKTRA001008", "Invalid configuration.", 500),

    // ===== SDK Protocol Errors (SSDKTRA002xxx) =====

    SDK_INVALID_PROTOCOL_STEP("SSDKTRA002001", "Invalid protocol step.", 400),
    SDK_INVALID_TRANSACTION_STATUS("SSDKTRA002002", "Invalid transaction status.", 400),
    SDK_INVALID_SUB_TRANSACTION_STATUS("SSDKTRA002003", "Invalid sub-transaction status.", 400),
    SDK_TRANSACTION_EXPIRED("SSDKTRA002004", "Transaction has expired.", 400),
    SDK_TRANSACTION_NOT_FOUND("SSDKTRA002005", "Transaction not found.", 404),

    // ===== SDK Crypto Errors (SSDKTRA003xxx) =====

    SDK_SIGNATURE_GENERATION_FAILED("SSDKTRA003001", "Failed to generate signature.", 500),
    SDK_SIGNATURE_VERIFICATION_FAILED("SSDKTRA003002", "Failed to verify signature.", 400),
    SDK_ECDH_KEY_AGREEMENT_FAILED("SSDKTRA003003", "ECDH key agreement failed.", 500),
    SDK_ENCRYPTION_FAILED("SSDKTRA003004", "Encryption failed.", 500),
    SDK_DECRYPTION_FAILED("SSDKTRA003005", "Decryption failed.", 500),
    SDK_VC_ISSUANCE_FAILED("SSDKTRA003006", "Failed to issue VC.", 500),

    // ===== SDK Blockchain Errors (SSDKTRA004xxx) =====

    SDK_DID_DOCUMENT_NOT_FOUND("SSDKTRA004001", "DID Document not found.", 404),
    SDK_DID_DOCUMENT_REGISTRATION_FAILED("SSDKTRA004002", "Failed to register DID Document.", 500),
    SDK_DID_DOCUMENT_UPDATE_FAILED("SSDKTRA004003", "Failed to update DID Document.", 500),
    SDK_VC_META_NOT_FOUND("SSDKTRA004004", "VC Metadata not found.", 404),
    SDK_BLOCKCHAIN_CONNECTION_FAILED("SSDKTRA004005", "Blockchain connection failed.", 500),
    SDK_VC_REVOKED("SSDKTRA004006", "VC has been revoked.", 400),

    // ===== SDK Wallet Errors (SSDKTRA005xxx) =====

    SDK_WALLET_CONNECTION_FAILED("SSDKTRA005001", "Failed to connect to wallet.", 500),
    SDK_WALLET_NOT_CONNECTED("SSDKTRA005002", "Wallet is not connected.", 500),
    SDK_WALLET_CREATION_FAILED("SSDKTRA005003", "Failed to create wallet.", 500),
    SDK_KEY_NOT_FOUND("SSDKTRA005004", "Key not found in wallet.", 404),
    SDK_KEY_GENERATION_FAILED("SSDKTRA005005", "Failed to generate key.", 500),

    // ===== SDK Provider Errors (SSDKTRA006xxx) =====

    SDK_USER_NOT_FOUND("SSDKTRA006001", "User not found.", 404),
    SDK_ENTITY_NOT_FOUND("SSDKTRA006002", "Entity not found.", 404),
    SDK_WALLET_INFO_NOT_FOUND("SSDKTRA006003", "Wallet information not found.", 404),
    SDK_ECDH_SESSION_NOT_FOUND("SSDKTRA006004", "ECDH session not found.", 404),
    SDK_TOKEN_NOT_FOUND("SSDKTRA006005", "Token not found.", 404),
    SDK_VC_SCHEMA_NOT_FOUND("SSDKTRA006006", "VC Schema not found.", 404),

    // ===== P110: TAS 등록 프로토콜 에러 (SSDKTRA11xxx) =====

    /** Password 불일치 - 요청한 password가 저장된 password와 일치하지 않는다. */
    PASSWORD_MISMATCH("SSDKTRA011001", "Password mismatch.", 400),

    /** TAS DID Document 없음 - 블록체인에서 TAS DID Document를 찾을 수 없다. */
    TAS_DID_DOCUMENT_NOT_FOUND("SSDKTRA011003", "TAS DID Document not found.", 404),

    /** Certificate VC 생성 실패 - Certificate VC 생성 중 오류가 발생했다. */
    CERTIFICATE_VC_GENERATION_FAILED("SSDKTRA011004", "Failed to generate certificate VC.", 500),

    /** Certificate VC 서명 실패 - Certificate VC 서명 중 오류가 발생했다. */
    CERTIFICATE_VC_SIGNING_FAILED("SSDKTRA011005", "Failed to sign certificate VC.", 500),

    /** Certificate VC 게시 실패 - Certificate VC 게시 중 오류가 발생했다. */
    CERTIFICATE_VC_PUBLISHING_FAILED("SSDKTRA011006", "Failed to publish certificate VC.", 500),

    /** VcMeta 등록 실패 - 블록체인에 VcMeta 등록 중 오류가 발생했다. */
    VC_META_REGISTRATION_FAILED("SSDKTRA011007", "Failed to register VcMeta to blockchain.", 500),

    /** TAS 등록 실패 (일반) - TAS 등록 중 예상치 못한 오류가 발생했다. */
    TAS_ENROLLMENT_FAILED("SSDKTRA011008", "Failed to enroll TAS.", 500),

    /** TAS Password 미설정 - TAS 등록 password가 설정되지 않았다. */
    TAS_PASSWORD_NOT_CONFIGURED("SSDKTRA011009", "TAS password not configured.", 500),

    // ===== P131: 월렛 등록 프로토콜 에러 (SSDKTRA13xxx) =====

    /** SSDKTRA13001: Wallet Provider가 등록되지 않았거나 역할이 WALLET_PROVIDER가 아님. */
    WALLET_PROVIDER_NOT_REGISTERED("SSDKTRA013001", "Wallet Provider not registered.", 400),

    /** SSDKTRA13002: Entity가 COMPLETED 상태가 아님. */
    ENTITY_NOT_COMPLETED("SSDKTRA013002", "Entity is not in COMPLETED status.", 400),

    /** SSDKTRA13003: Certificate VC를 찾을 수 없음. */
    CERTIFICATE_VC_NOT_FOUND("SSDKTRA013003", "Certificate VC not found.", 404),

    /** SSDKTRA13004: Provider DID와 Certificate VC subject DID가 일치하지 않음. */
    PROVIDER_DID_MISMATCH("SSDKTRA013004", "Provider DID mismatch in Certificate VC.", 400),

    /** SSDKTRA13005: Certificate VC의 Issuer가 TAS가 아니거나 만료됨. */
    INVALID_CERTIFICATE_VC_ISSUER("SSDKTRA013005", "Invalid Certificate VC issuer or VC expired.", 400),

    /** SSDKTRA13006: Attestation Proof 서명 검증 실패. */
    ATTESTATION_VERIFICATION_FAILED("SSDKTRA013006", "Attestation signature verification failed.", 400),

    /** SSDKTRA13007: WalletId가 이미 존재함. */
    WALLET_ID_ALREADY_EXISTS("SSDKTRA013007", "WalletId already exists.", 400),

    /** SSDKTRA13008: DID Document 키 서명 검증 실패. */
    DID_DOCUMENT_KEY_VERIFICATION_FAILED("SSDKTRA013008", "DID Document key signature verification failed.", 400),

    /** SSDKTRA13009: InvokedDidDoc 생성 실패. */
    INVOKED_DID_DOC_CREATION_FAILED("SSDKTRA013009", "Failed to create InvokedDidDoc.", 500),

    /** SSDKTRA13010: DID Document 블록체인 등록 실패. */
    DID_DOCUMENT_REGISTRATION_FAILED("SSDKTRA013010", "Failed to register DID Document to blockchain.", 500),

    // ===== P132: 사용자 등록 프로토콜 에러 (SSDKTRA32xxx) =====

    /** SSDKTRA32001: walletInfo proof 서명 검증 실패. */
    WALLET_INFO_PROOF_VERIFICATION_FAILED("SSDKTRA032001", "WalletInfo proof signature verification failed.", 400),

    /** SSDKTRA32002: caAppInfo proof 서명 검증 실패. */
    CA_APP_INFO_PROOF_VERIFICATION_FAILED("SSDKTRA032002", "CaAppInfo proof signature verification failed.", 400),

    /** SSDKTRA32003: ServerTokenData 서명 실패. */
    SERVER_TOKEN_DATA_SIGNING_FAILED("SSDKTRA032003", "Failed to sign ServerTokenData.", 500),

    /** SSDKTRA32004: 서버 토큰 암호화 실패. */
    SERVER_TOKEN_ENCRYPTION_FAILED("SSDKTRA032004", "Failed to encrypt server token data.", 500),

    /** SSDKTRA32005: 서버 토큰 유효하지 않음 (검증 실패). */
    SERVER_TOKEN_INVALID("SSDKTRA032005", "Server token is invalid or expired.", 400),

    /** SSDKTRA32006: KYC 데이터 조회 실패. */
    KYC_DATA_RETRIEVAL_FAILED("SSDKTRA032006", "Failed to retrieve KYC data.", 500),

    /** SSDKTRA32007: kycToken과 kycTxId 모두 없음 (하나는 필수). */
    KYC_TOKEN_AND_TXID_BOTH_MISSING("SSDKTRA032007", "Either kycToken or kycTxId must be provided.", 400),

    /** SSDKTRA32008: 이미 등록된 사용자 DID. */
    USER_DID_ALREADY_EXISTS("SSDKTRA032008", "User DID already registered.", 400),

    /** SSDKTRA32009: 이미 등록된 사용자 PII (CI). */
    USER_PII_ALREADY_EXISTS("SSDKTRA032009", "User with same PII already registered.", 400),

    /** SSDKTRA32010: SignedDidDoc 월렛 서명 검증 실패. */
    SIGNED_DID_DOC_WALLET_PROOF_FAILED("SSDKTRA032010", "SignedDidDoc wallet proof verification failed.", 400),

    /** SSDKTRA32011: ownerDidDoc 키 서명 검증 실패. */
    OWNER_DID_DOC_KEY_PROOF_FAILED("SSDKTRA032011", "OwnerDidDoc key proof verification failed.", 400),

    /** SSDKTRA32012: User VC 생성 실패. */
    USER_VC_GENERATION_FAILED("SSDKTRA032012", "Failed to generate User VC.", 500),

    /** SSDKTRA32013: User VC VcMeta 블록체인 등록 실패. */
    USER_VC_META_REGISTRATION_FAILED("SSDKTRA032013", "Failed to register User VC metadata to blockchain.", 500),

    // ===== P141: DID Document 갱신 프로토콜 에러 (SSDKTRA17xxx) =====

    /** SSDKTRA17002: 사용자를 찾을 수 없습니다. */
    USER_NOT_FOUND("SSDKTRA017002", "User not found.", 404),

    /** SSDKTRA17003: 사용자 상태가 'Activated'가 아닙니다. */
    USER_NOT_ACTIVATED("SSDKTRA017003", "User status must be ACTIVATED.", 400),

    /** SSDKTRA17011: 'propose-update-diddoc' API 요청 처리에 실패했습니다. */
    PROPOSE_UPDATE_DIDDOC_FAILED("SSDKTRA017011", "Failed to propose update DID Document.", 500),

    /** SSDKTRA17012: 'request-update-diddoc' API 요청 처리에 실패했습니다. */
    REQUEST_UPDATE_DIDDOC_FAILED("SSDKTRA017012", "Failed to request update DID Document.", 500),

    /** SSDKTRA17013: 'confirm-update-diddoc' API 요청 처리에 실패했습니다. */
    CONFIRM_UPDATE_DIDDOC_FAILED("SSDKTRA017013", "Failed to confirm update DID Document.", 500),

    /** SSDKTRA16520: 'authNonce' 가 일치하지 않습니다. */
    AUTH_NONCE_MISMATCH("SSDKTRA016520", "AuthNonce mismatch.", 400),

    /** SSDKTRA16516: DID Document 키 서명 검증에 실패했습니다. */
    DID_DOC_KEY_VERIFICATION_FAILED("SSDKTRA016516", "DID Document key proof verification failed.", 400),

    /** SSDKTRA12006: DID Document를 파싱하는 데 실패했습니다. */
    DID_DOCUMENT_PARSING_FAILED("SSDKTRA012006", "Failed to parse DID Document.", 400),

    /** SSDKTRA12009: Invalid DID Document version. */
    INVALID_DID_DOC_VERSION("SSDKTRA012009", "Invalid DID Document version.", 400),

    /** SSDKTRA18006: 잘못된 updated 입니다. */
    INVALID_DID_DOC_UPDATED("SSDKTRA018006", "Invalid DID Document updated time.", 400),

    /** SSDKTRA18007: 잘못된 context 입니다. */
    INVALID_DID_DOC_CONTEXT("SSDKTRA018007", "Invalid DID Document context.", 400),

    /** SSDKTRA18008: 잘못된 id 입니다. */
    INVALID_DID_DOC_ID("SSDKTRA018008", "Invalid DID Document id.", 400),

    /** SSDKTRA18009: 잘못된 controller 입니다. */
    INVALID_DID_DOC_CONTROLLER("SSDKTRA018009", "Invalid DID Document controller.", 400),

    /** SSDKTRA18014: 잘못된 creation 입니다. */
    INVALID_DID_DOC_CREATED("SSDKTRA018014", "Invalid DID Document created time.", 400),

    /** SSDKTRA18015: 잘못된 deactivated 입니다. */
    INVALID_DID_DOC_DEACTIVATED("SSDKTRA018015", "Invalid DID Document deactivated status.", 400),

    /** Nonce 생성 실패. */
    NONCE_GENERATION_FAILED("SSDKTRA017020", "Failed to generate nonce.", 500),

    /** Multibase 디코딩 실패. */
    MULTIBASE_DECODE_FAILED("SSDKTRA017021", "Failed to decode multibase.", 400),

    /** Proof 누락. */
    PROOF_MISSING("SSDKTRA017022", "Proof is missing.", 400),

    /** 잘못된 verification method. */
    INVALID_VERIFICATION_METHOD("SSDKTRA017023", "Invalid verification method.", 400),

    /** 잘못된 proof purpose. */
    INVALID_PROOF_PURPOSE("SSDKTRA017024", "Invalid proof purpose.", 400),

    /** DID 불일치. */
    DID_MISMATCH("SSDKTRA017025", "DID mismatch.", 400),

    /** DID Document 없음. */
    DID_DOCUMENT_NOT_FOUND("SSDKTRA017026", "DID Document not found.", 404),

    /** Public key 없음. */
    PUBLIC_KEY_NOT_FOUND("SSDKTRA017027", "Public key not found.", 404),

    // ===== P142: DID Document 복구 프로토콜 에러 (SSDKTRA20xxx) =====

    /** SSDKTRA20002: 사용자를 찾을 수 없습니다. */
    USER_NOT_FOUND_FOR_RESTORE("SSDKTRA020002", "User not found for restore.", 404),

    /** SSDKTRA20003: 사용자 상태가 'Deactivated'가 아닙니다. */
    USER_NOT_DEACTIVATED("SSDKTRA020003", "User status must be DEACTIVATED.", 400),

    /** SSDKTRA20004: Transaction을 찾을 수 없습니다. */
    TRANSACTION_NOT_FOUND_FOR_RESTORE("SSDKTRA020004", "Transaction not found for restore.", 404),

    /** SSDKTRA20005: Offer 생성에 실패했습니다. */
    OFFER_CREATION_FAILED("SSDKTRA020005", "Failed to create offer.", 500),

    /** SSDKTRA20006: Push 전송에 실패했습니다. */
    PUSH_SEND_FAILED("SSDKTRA020006", "Failed to send push notification.", 500),

    /** SSDKTRA20007: Email 전송에 실패했습니다. */
    EMAIL_SEND_FAILED("SSDKTRA020007", "Failed to send email.", 500),

    /** SSDKTRA20008: Offer를 찾을 수 없습니다. */
    OFFER_NOT_FOUND("SSDKTRA020008", "Offer not found.", 404),

    /** SSDKTRA20009: Offer 코드가 일치하지 않습니다. */
    OFFER_CODE_MISMATCH("SSDKTRA020009", "Offer code mismatch.", 400),

    /** SSDKTRA20010: Offer가 만료되었습니다. */
    OFFER_EXPIRED("SSDKTRA020010", "Offer has expired.", 400),

    /** SSDKTRA20011: Offer가 이미 사용되었습니다. */
    OFFER_ALREADY_USED("SSDKTRA020011", "Offer has already been used.", 400),

    /** SSDKTRA20012: deactivated가 false가 아닙니다. */
    DEACTIVATED_FLAG_NOT_FALSE("SSDKTRA020012", "Deactivated flag must be false for restore.", 400),

    /** SSDKTRA20013: Offer txId가 일치하지 않습니다. */
    OFFER_TRANSACTION_MISMATCH("SSDKTRA020013", "Offer transaction ID mismatch.", 400),

    /** SSDKTRA20014: 잘못된 Offer 타입입니다. */
    INVALID_OFFER_TYPE("SSDKTRA020014", "Invalid offer type.", 400),

    /** SSDKTRA20015: Offer 전송에 실패했습니다. */
    OFFER_SEND_FAILED("SSDKTRA020015", "Failed to send offer.", 500),

    /** SSDKTRA20021: 'propose-restore-diddoc' API 요청 처리에 실패했습니다. */
    PROPOSE_RESTORE_DIDDOC_FAILED("SSDKTRA020021", "Failed to propose restore DID Document.", 500),

    /** SSDKTRA20022: 'request-issue-offer-restore-diddoc' API 요청 처리에 실패했습니다. */
    REQUEST_ISSUE_OFFER_RESTORE_DIDDOC_FAILED("SSDKTRA020022", "Failed to request issue offer restore DID Document.", 500),

    /** SSDKTRA20023: 'verify-offer-restore-diddoc' API 요청 처리에 실패했습니다. */
    VERIFY_OFFER_RESTORE_DIDDOC_FAILED("SSDKTRA020023", "Failed to verify offer restore DID Document.", 500),

    /** SSDKTRA20024: 'request-restore-diddoc' API 요청 처리에 실패했습니다. */
    REQUEST_RESTORE_DIDDOC_FAILED("SSDKTRA020024", "Failed to request restore DID Document.", 500),

    /** SSDKTRA20025: 'confirm-restore-diddoc' API 요청 처리에 실패했습니다. */
    CONFIRM_RESTORE_DIDDOC_FAILED("SSDKTRA020025", "Failed to confirm restore DID Document.", 500),

    // ===== P210: VC 발급 프로토콜 에러 (SSDKTRA21xxx) =====

    // Transaction 관련 (SSDKTRA21001 ~ SSDKTRA21004)
    /** SSDKTRA21001: Transaction을 찾을 수 없습니다. */
    VC_ISSUANCE_TRANSACTION_NOT_FOUND("SSDKTRA021001", "Transaction not found.", 404),

    /** SSDKTRA21002: Transaction 상태가 유효하지 않습니다. */
    VC_ISSUANCE_TRANSACTION_INVALID_STATE("SSDKTRA021002", "Transaction invalid state.", 400),

    /** SSDKTRA21003: Transaction이 만료되었습니다. */
    VC_ISSUANCE_TRANSACTION_EXPIRED("SSDKTRA021003", "Transaction expired.", 400),

    /** SSDKTRA21004: 이전 Step이 유효하지 않습니다. */
    VC_ISSUANCE_INVALID_PREVIOUS_STEP("SSDKTRA021004", "Invalid previous step.", 400),

    // VC Plan 관련 (SSDKTRA21005 ~ SSDKTRA21006)
    /** SSDKTRA21005: VC Plan을 찾을 수 없습니다. */
    VC_PLAN_NOT_FOUND("SSDKTRA021005", "VC Plan not found.", 404),

    /** SSDKTRA21006: VC Plan이 유효하지 않습니다. */
    VC_PLAN_INVALID("SSDKTRA021006", "VC Plan invalid.", 400),

    // Issuer 관련 (SSDKTRA21007, SSDKTRA21009)
    /** SSDKTRA21007: Issuer를 찾을 수 없습니다. */
    ISSUER_NOT_FOUND("SSDKTRA021007", "Issuer not found.", 404),

    /** SSDKTRA21009: Issuer와의 통신에 실패했습니다. */
    ISSUER_COMMUNICATION_FAILED("SSDKTRA021009", "Issuer communication failed.", 500),

    // Offer 관련 - P142 에러 코드 재사용 (OFFER_CREATION_FAILED, PUSH_SEND_FAILED, EMAIL_SEND_FAILED, OFFER_EXPIRED)

    // Server Token 관련 (SSDKTRA21015)
    /** SSDKTRA21015: Server Token 검증에 실패했습니다. */
    SERVER_TOKEN_VALIDATION_FAILED("SSDKTRA021015", "Server token validation failed.", 400),

    // Issue Profile 관련 (SSDKTRA21016 ~ SSDKTRA21017)
    /** SSDKTRA21016: Issue Profile 조회에 실패했습니다. */
    ISSUE_PROFILE_RETRIEVAL_FAILED("SSDKTRA021016", "Issue profile retrieval failed.", 500),

    /** SSDKTRA21017: VC Schema 검증에 실패했습니다. */
    VC_SCHEMA_VALIDATION_FAILED("SSDKTRA021017", "VC schema validation failed.", 400),

    // VP 관련 (SSDKTRA21018 ~ SSDKTRA21019)
    /** SSDKTRA21018: VP 복호화에 실패했습니다. */
    VP_DECRYPTION_FAILED("SSDKTRA021018", "VP decryption failed.", 500),

    /** SSDKTRA21019: VP 서명 검증에 실패했습니다. */
    VP_SIGNATURE_VERIFICATION_FAILED("SSDKTRA021019", "VP signature verification failed.", 400),

    // VC 발급 관련 (SSDKTRA21020 ~ SSDKTRA21021)
    /** SSDKTRA21020: VC 발급에 실패했습니다. */
    VC_ISSUANCE_FAILED("SSDKTRA021020", "VC issuance failed.", 500),

    /** SSDKTRA21021: VC 암호화에 실패했습니다. */
    VC_ENCRYPTION_FAILED("SSDKTRA021021", "VC encryption failed.", 500),

    // VC ID 관련 (SSDKTRA21022 ~ SSDKTRA21023)
    /** SSDKTRA21022: VC ID가 일치하지 않습니다. */
    VC_ID_MISMATCH("SSDKTRA021022", "VC ID mismatch.", 400),

    /** SSDKTRA21023: VC Metadata 등록에 실패했습니다. */
    VC_METADATA_REGISTRATION_FAILED("SSDKTRA021023", "VC metadata registration failed.", 500),

    // User/Holder 관련 (SSDKTRA21024 ~ SSDKTRA21025)
    /** SSDKTRA21024: Holder를 찾을 수 없습니다. */
    HOLDER_NOT_FOUND("SSDKTRA021024", "Holder not found.", 404),

    /** SSDKTRA21025: Push Token을 찾을 수 없습니다. */
    PUSH_TOKEN_NOT_FOUND("SSDKTRA021025", "Push token not found.", 404),

    // ===== P220 VC Revocation Protocol Errors (SSDKTRA22xxx) =====

    // VC Status 관련 (SSDKTRA22001 ~ SSDKTRA22003)
    /** SSDKTRA22001: VC가 이미 폐기되었습니다. */
    VC_ALREADY_REVOKED("SSDKTRA022001", "VC is already revoked.", 400),

    /** SSDKTRA22002: VC가 만료되었습니다. */
    VC_EXPIRED("SSDKTRA022002", "VC is expired.", 400),

    /** SSDKTRA22003: VC 상태가 유효하지 않습니다. */
    VC_STATUS_INVALID("SSDKTRA022003", "VC status is invalid.", 400),

    // VC Metadata 관련 (SSDKTRA22004)
    /** SSDKTRA22004: 블록체인에서 VC 메타데이터 조회에 실패했습니다. */
    VC_META_FETCH_FAILED("SSDKTRA022004", "Failed to fetch VC metadata from blockchain.", 500),

    // Issuer Nonce 관련 (SSDKTRA22005)
    /** SSDKTRA22005: Issuer Nonce가 일치하지 않습니다. */
    ISSUER_NONCE_MISMATCH("SSDKTRA022005", "Issuer nonce mismatch.", 400),

    // Signature 관련 (SSDKTRA22006)
    /** SSDKTRA22006: 서명이 유효하지 않습니다. */
    SIGNATURE_INVALID("SSDKTRA022006", "Signature is invalid.", 400),

    // Protocol Step 관련 (SSDKTRA22007 ~ SSDKTRA22009)
    /** SSDKTRA22007: 'propose-revoke-vc' API 요청 처리에 실패했습니다. */
    PROPOSE_REVOKE_VC_FAILED("SSDKTRA022007", "Failed to propose revoke VC.", 500),

    /** SSDKTRA22008: 'request-revoke-vc' API 요청 처리에 실패했습니다. */
    REQUEST_REVOKE_VC_FAILED("SSDKTRA022008", "Failed to request revoke VC.", 500),

    /** SSDKTRA22009: 'confirm-revoke-vc' API 요청 처리에 실패했습니다. */
    CONFIRM_REVOKE_VC_FAILED("SSDKTRA022009", "Failed to confirm revoke VC.", 500),

    // ===== SDK General Errors (SSDKTRA009xxx) =====

    SDK_NOT_INITIALIZED("SSDKTRA009001", "SDK not initialized.", 500),
    SDK_UNKNOWN_ERROR("SSDKTRA009999", "Unknown SDK error occurred.", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    /**
     * Constructor
     *
     * @param code       Error code
     * @param message    Error message
     * @param httpStatus HTTP status code
     */
    TaSdkErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Get error message by error code
     *
     * @param code Error code
     * @return Error message
     */
    public static String getMessageByCode(String code) {
        for (TaSdkErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode.getMessage();
            }
        }
        return "Unknown error code: " + code;
    }

    @Override
    public String toString() {
        return String.format(
            "TaSdkErrorCode{code='%s', message='%s', httpStatus=%d}",
            code,
            message,
            httpStatus
        );
    }
}
