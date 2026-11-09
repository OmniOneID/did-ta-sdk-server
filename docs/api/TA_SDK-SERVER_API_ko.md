---
puppeteer:
    pdf:
        format: A4
        displayHeaderFooter: true
        landscape: false
        scale: 0.8
        margin:
            top: 1.2cm
            right: 1cm
            bottom: 1cm
            left: 1cm
    image:
        quality: 100
        fullPage: false
---

# TA SDK Server API

- 주제: TA SDK Server API
- 작성: OpenDID
- 일자: 2026-11-09
- 버전: v3.0.0

| 버전   | 일자       | 변경 내용 |
| ------ | ---------- | --------- |
| v1.0.0 | 2026-03-04 | 초기 작성 |
| v3.0.0 | 2026-11-09 | 공개 API 전체 문서화, 시그니처 정정 |


<div style="page-break-after: always;"></div>

# 목차

- [1. APIs - Protocols](#1-apis---protocols)
    - [1.1 TasEnrollmentProtocol](#11-tasenrollmentprotocol)
    - [1.2 EntityEnrollmentProtocol](#12-entityenrollmentprotocol)
    - [1.3 WalletRegistrationProtocol](#13-walletregistrationprotocol)
    - [1.4 UserRegistrationProtocol](#14-userregistrationprotocol)
    - [1.5 DidDocUpdateProtocol](#15-diddocupdateprotocol)
    - [1.6 DidDocRestoreProtocol](#16-diddocrestoreprotocol)
    - [1.7 VcIssuanceProtocol](#17-vcissuanceprotocol)
    - [1.8 VcRevocationProtocol](#18-vcrevocationprotocol)
    - [1.9 Protocol](#19-protocol)
- [2. APIs - Providers](#2-apis---providers)
    - [2.1 CertificateProvider](#21-certificateprovider)
    - [2.2 DidDocumentProvider](#22-diddocumentprovider)
    - [2.3 EcdhSessionProvider](#23-ecdhsessionprovider)
    - [2.4 EntityProvider](#24-entityprovider)
    - [2.5 KycProvider](#25-kycprovider)
    - [2.6 StorageProvider](#26-storageprovider)
    - [2.7 TasProvider](#27-tasprovider)
    - [2.8 TokenProvider](#28-tokenprovider)
    - [2.9 TransactionProvider](#29-transactionprovider)
    - [2.10 UserProvider](#210-userprovider)
    - [2.11 VcIssuanceDataProvider](#211-vcissuancedataprovider)
    - [2.12 VcRevocationDataProvider](#212-vcrevocationdataprovider)
    - [2.13 VcSchemaProvider](#213-vcschemaprovider)
    - [2.14 VcStorageProvider](#214-vcstorageprovider)
    - [2.15 WalletProvider](#215-walletprovider)
    - [2.16 WalletRegistrationProvider](#216-walletregistrationprovider)
    - [2.17 IssuerApiClient](#217-issuerapiclient)
- [3. APIs - Config](#3-apis---config)
    - [3.1 TaSdkConfig](#31-tasdkconfig)
    - [3.2 WalletConfig](#32-walletconfig)
    - [3.3 CryptoConfig](#33-cryptoconfig)
    - [3.4 StorageConfig](#34-storageconfig)
- [4. Enumerator](#4-enumerator)
    - [4.1 OfferType](#41-offertype)
    - [4.2 ServerTokenPurpose](#42-servertokenpurpose)
    - [4.3 TasStatus](#43-tasstatus)
    - [4.4 UserStatus](#44-userstatus)
    - [4.5 StorageConfig.StorageType](#45-storageconfigstoragetype)
- [5. APIs - Exceptions](#5-apis---exceptions)
    - [5.1 TaSdkException](#51-tasdkexception)
    - [5.2 TaSdkErrorCode](#52-tasdkerrorcode)
    - [5.3 IssuerApiException](#53-issuerapiexception)
- [6. APIs - Core Components](#6-apis---core-components)
    - [6.1 FileWalletAdapter](#61-filewalletadapter)
    - [6.2 KeyManager](#62-keymanager)
    - [6.3 DidDocumentBuilder](#63-diddocumentbuilder)
    - [6.4 InvokedDidDocHandler](#64-invokeddiddochandler)
    - [6.5 BlockchainAdapter](#65-blockchainadapter)
    - [6.6 TaEcdhService](#66-taecdhservice)
    - [6.7 TaSignatureService](#67-tasignatureservice)
    - [6.8 TaVcService](#68-tavcservice)
    - [6.9 VcBuilder](#69-vcbuilder)
    - [6.10 VcSchemaProcessor](#610-vcschemaprocessor)
- [7. Data Models](#7-data-models)
    - [7.1 OfferIssueVcApiRequest](#71-offerissuevcapirequest)
    - [7.2 OfferIssueVcApiResponse](#72-offerissuevcapiresponse)
    - [7.3 InspectIssueProposeApiRequest](#73-inspectissueproposeapirequest)
    - [7.4 InspectIssueProposeApiResponse](#74-inspectissueproposeapiresponse)
    - [7.5 GenerateIssueProfileApiRequest](#75-generateissueprofileapirequest)
    - [7.6 GenerateIssueProfileApiRequest.ApiHolder](#76-generateissueprofileapirequestapiholder)
    - [7.7 GenerateIssueProfileApiResponse](#77-generateissueprofileapiresponse)
    - [7.8 IssueVcApiRequest](#78-issuevcapirequest)
    - [7.9 IssueVcApiResponse](#79-issuevcapiresponse)
    - [7.10 CompleteVcApiRequest](#710-completevcapirequest)
    - [7.11 CompleteVcApiResponse](#711-completevcapiresponse)
    - [7.12 InspectProposeRevokeApiRequest](#712-inspectproposerevokeapirequest)
    - [7.13 InspectProposeRevokeApiResponse](#713-inspectproposerevokeapiresponse)
    - [7.14 RevokeVcApiRequest](#714-revokevcapirequest)
    - [7.15 RevokeVcApiResponse](#715-revokevcapiresponse)
    - [7.16 CompleteRevokeApiRequest](#716-completerevokeapirequest)
    - [7.17 CompleteRevokeApiResponse](#717-completerevokeapiresponse)
    - [7.18 AccE2e](#718-acce2e)
    - [7.19 E2e](#719-e2e)
    - [7.20 ReqRevokeVc](#720-reqrevokevc)
    - [7.21 EnrollTasRequest](#721-enrolltasrequest)
    - [7.22 EnrollTasRequest.Request](#722-enrolltasrequestrequest)
    - [7.23 EnrollTasResponse](#723-enrolltasresponse)
    - [7.24 ProposeEnrollEntityRequest](#724-proposeenrollentityrequest)
    - [7.25 ProposeEnrollEntityResponse](#725-proposeenrollentityresponse)
    - [7.26 RequestEnrollEntityRequest](#726-requestenrollentityrequest)
    - [7.27 RequestEnrollEntityResponse](#727-requestenrollentityresponse)
    - [7.28 ConfirmEnrollEntityRequest](#728-confirmenrollentityrequest)
    - [7.29 ConfirmEnrollEntityResponse](#729-confirmenrollentityresponse)
    - [7.30 ProposeRegisterUserRequest](#730-proposeregisteruserrequest)
    - [7.31 ProposeRegisterUserResponse](#731-proposeregisteruserresponse)
    - [7.32 RetrieveKycRequest](#732-retrievekycrequest)
    - [7.33 RetrieveKycResponse](#733-retrievekycresponse)
    - [7.34 RequestRegisterUserRequest](#734-requestregisteruserrequest)
    - [7.35 RequestRegisterUserResponse](#735-requestregisteruserresponse)
    - [7.36 ConfirmRegisterUserRequest](#736-confirmregisteruserrequest)
    - [7.37 ConfirmRegisterUserResponse](#737-confirmregisteruserresponse)
    - [7.38 ProposeUpdateDidDocRequest](#738-proposeupdatediddocrequest)
    - [7.39 ProposeUpdateDidDocResponse](#739-proposeupdatediddocresponse)
    - [7.40 RequestUpdateDidDocRequest](#740-requestupdatediddocrequest)
    - [7.41 RequestUpdateDidDocResponse](#741-requestupdatediddocresponse)
    - [7.42 ConfirmUpdateDidDocRequest](#742-confirmupdatediddocrequest)
    - [7.43 ConfirmUpdateDidDocResponse](#743-confirmupdatediddocresponse)
    - [7.44 ProposeRestoreDidDocRequest](#744-proposerestorediddocrequest)
    - [7.45 ProposeRestoreDidDocResponse](#745-proposerestorediddocresponse)
    - [7.46 RequestRestoreDidDocRequest](#746-requestrestorediddocrequest)
    - [7.47 RequestRestoreDidDocResponse](#747-requestrestorediddocresponse)
    - [7.48 ConfirmRestoreDidDocRequest](#748-confirmrestorediddocrequest)
    - [7.49 ConfirmRestoreDidDocResponse](#749-confirmrestorediddocresponse)
    - [7.50 ProposeIssueVcRequest](#750-proposeissuevcrequest)
    - [7.51 ProposeIssueVcResult](#751-proposeissuevcresult)
    - [7.52 RequestIssueProfileRequest](#752-requestissueprofilerequest)
    - [7.53 RequestIssueProfileResult](#753-requestissueprofileresult)
    - [7.54 RequestIssueVcRequest](#754-requestissuevcrequest)
    - [7.55 RequestIssueVcResult](#755-requestissuevcresult)
    - [7.56 ConfirmIssueVcRequest](#756-confirmissuevcrequest)
    - [7.57 ConfirmIssueVcResult](#757-confirmissuevcresult)
    - [7.58 ProposeRevokeVcRequest](#758-proposerevokevcrequest)
    - [7.59 ProposeRevokeVcResult](#759-proposerevokevcresult)
    - [7.60 RequestRevokeVcRequest](#760-requestrevokevcrequest)
    - [7.61 RequestRevokeVcResult](#761-requestrevokevcresult)
    - [7.62 ConfirmRevokeVcRequest](#762-confirmrevokevcrequest)
    - [7.63 ConfirmRevokeVcResult](#763-confirmrevokevcresult)
    - [7.64 RegisterWalletRequest](#764-registerwalletrequest)
    - [7.65 RegisterWalletResponse](#765-registerwalletresponse)
    - [7.66 DidAuth](#766-didauth)
    - [7.67 SignedDidDoc](#767-signeddiddoc)
    - [7.68 Wallet](#768-wallet)
    - [7.69 ServerTokenSeed](#769-servertokenseed)
    - [7.70 SignedWalletInfo](#770-signedwalletinfo)
    - [7.71 AttestedAppInfo](#771-attestedappinfo)
    - [7.72 ServerTokenData](#772-servertokendata)
    - [7.73 Tas](#773-tas)
    - [7.74 Entity](#774-entity)
    - [7.75 Transaction](#775-transaction)
    - [7.76 User](#776-user)
    - [7.77 PiiData](#777-piidata)
    - [7.78 HolderInfo](#778-holderinfo)
    - [7.79 VcPlanInfo](#779-vcplaninfo)
    - [7.80 VcMeta](#780-vcmeta)
- [8. 보조 유틸리티](#8-보조-유틸리티)
    - [8.1 AuthNonceMatcher](#81-authnoncematcher)
    - [8.2 ClaimManager](#82-claimmanager)

<div style="page-break-after: always;"></div>

# 1. APIs - Protocols

Protocol 인터페이스는 TA SDK의 핵심 API입니다. 각 `*ProtocolImpl`을 해당 절에 적힌 생성자로 만들거나(또는 컨테이너에 빈으로 등록하여) 프로토콜 메서드를 호출합니다. 구현체는 Provider 구현체를 생성자로 받으며, `TasEnrollmentProtocolImpl`과 `EntityEnrollmentProtocolImpl`은 `VcBuilder`도 받고, `VcRevocationProtocolImpl`은 인자 없는 생성자를 씁니다. `VcIssuanceProtocol`과 `VcRevocationProtocol`은 `IssuerApiClient`와 데이터 Provider를 메서드 인자로 받습니다. SDK는 트랜잭션을 저장하지 않습니다. 메서드가 반환한 txId, authNonce 등은 호출자가 저장해 두었다가 이후 단계에서 `TransactionProvider`나 데이터 Provider로 다시 제공해야 합니다.

## 1.1 TasEnrollmentProtocol

- 구현 클래스: `TasEnrollmentProtocolImpl`는 `TasEnrollmentProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `TasEnrollmentProtocolImpl(TasProvider tasProvider, StorageProvider storageProvider, WalletProvider walletProvider, CertificateProvider certificateProvider, VcBuilder vcBuilder)`
- 공통 메서드: `TasEnrollmentProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.1.1 enrollTas

#### Class Name
`TasEnrollmentProtocol`

#### Function Name
`enrollTas`

#### Function Introduction
`TAS 자가 등록을 수행합니다 (P110). 요청 비밀번호를 TasProvider.getTasPassword()와 비교하고, 미리 등록되어 있어야 하는 TAS DID Document를 조회(StorageProvider.findDidDoc)한 뒤, VcBuilder로 Certificate VC를 만들어 WalletProvider.signVc로 TAS assert 키 서명을 하고, CertificateProvider로 게시한 다음 StorageProvider로 VcMeta를 등록합니다. 실패 시 TaSdkException(TAS_PASSWORD_NOT_CONFIGURED, PASSWORD_MISMATCH, TAS_DID_DOCUMENT_NOT_FOUND, CERTIFICATE_VC_GENERATION_FAILED, CERTIFICATE_VC_SIGNING_FAILED, CERTIFICATE_VC_PUBLISHING_FAILED, VC_META_REGISTRATION_FAILED, TAS_ENROLLMENT_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`EnrollTasRequest`](#721-enrolltasrequest) | TAS 등록 요청 데이터 | M | `request.password` 포함 (중첩 클래스 `EnrollTasRequest.Request`) |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`EnrollTasResponse`](#723-enrolltasresponse) | TAS 등록 응답 | M | certVcRef(Certificate VC URL) 포함. txId는 SDK가 설정하지 않음 |

#### Function Declaration
```java
EnrollTasResponse enrollTas(EnrollTasRequest request) throws TaSdkException
```

#### Function Usage
```java
TasEnrollmentProtocol protocol = new TasEnrollmentProtocolImpl(
    tasProvider, storageProvider, walletProvider, certificateProvider, vcBuilder
);
EnrollTasResponse response = protocol.enrollTas(request);
```
<br>

---

## 1.2 EntityEnrollmentProtocol

- 구현 클래스: `EntityEnrollmentProtocolImpl`는 `EntityEnrollmentProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `EntityEnrollmentProtocolImpl(TransactionProvider transactionProvider, EntityProvider entityProvider, EcdhSessionProvider ecdhSessionProvider, StorageProvider storageProvider, WalletProvider walletProvider, DidDocumentProvider didDocumentProvider, CertificateProvider certificateProvider, VcBuilder vcBuilder)`
- 공통 메서드: `EntityEnrollmentProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.2.1 proposeEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`proposeEnrollEntity`

#### Function Introduction
`Entity 등록 프로토콜 Step 1 (P120). UUID txId와 16바이트 authNonce(multibase 인코딩)를 생성합니다. SDK는 이 값을 저장하지 않으므로, Step 3에서 TransactionProvider.getTransaction(txId)가 authNonce를 반환하도록 호출자가 저장해야 합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeEnrollEntityRequest`](#724-proposeenrollentityrequest) | Entity 등록 제안 요청 | M | id만 있음. SDK는 읽지 않음 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeEnrollEntityResponse`](#725-proposeenrollentityresponse) | 등록 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeEnrollEntityResponse proposeEnrollEntity(ProposeEnrollEntityRequest request) throws TaSdkException
```

#### Function Usage
```java
EntityEnrollmentProtocol protocol = new EntityEnrollmentProtocolImpl(
    transactionProvider, entityProvider, ecdhSessionProvider, storageProvider,
    walletProvider, didDocumentProvider, certificateProvider, vcBuilder
);
ProposeEnrollEntityResponse response = protocol.proposeEnrollEntity(request);
```
<br>

### 1.2.2 requestEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`requestEnrollEntity`

#### Function Introduction
`Entity 등록 프로토콜 Step 3 (P120). 트랜잭션을 조회(TransactionProvider)하고 DID Auth(DID 형식, verificationMethod, authNonce, 서명)를 검증한 뒤, Entity를 조회(EntityProvider)하여 Certificate VC를 생성·서명하고 VcMeta 등록과 게시를 거쳐, txId로 조회한 ECDH 세션 키·암호 방식·패딩(EcdhSessionProvider)으로 VC를 암호화합니다. 실패 시 SDK_TRANSACTION_NOT_FOUND, SDK_INVALID_DID, SDK_INVALID_PROOF, SDK_SIGNATURE_VERIFICATION_FAILED, SDK_ENTITY_NOT_FOUND, CERTIFICATE_VC_SIGNING_FAILED, SDK_ENCRYPTION_FAILED, SDK_UNKNOWN_ERROR 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestEnrollEntityRequest`](#726-requestenrollentityrequest) | Entity 등록 요청 데이터 | M | txId, didAuth, dn(선택, 비어 있으면 Entity의 dn 사용) 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestEnrollEntityResponse`](#727-requestenrollentityresponse) | 등록 요청 응답 | M | txId, iv, encVc, vcUrl, vcId 포함 (vcId는 서버 내부용) |

#### Function Declaration
```java
RequestEnrollEntityResponse requestEnrollEntity(RequestEnrollEntityRequest request) throws TaSdkException
```

#### Function Usage
```java
RequestEnrollEntityResponse response = protocol.requestEnrollEntity(request);
```
<br>

### 1.2.3 confirmEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`confirmEnrollEntity`

#### Function Introduction
`Entity 등록 프로토콜 Step 4 (P120). txId만 반환합니다. SDK는 트랜잭션 상태와 vcId를 검사하지 않으므로 호출 전에 호출자가 검사해야 합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmEnrollEntityRequest`](#728-confirmenrollentityrequest) | Entity 등록 확인 요청 | M | txId, vcId 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmEnrollEntityResponse`](#729-confirmenrollentityresponse) | 등록 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmEnrollEntityResponse confirmEnrollEntity(ConfirmEnrollEntityRequest request) throws TaSdkException
```

#### Function Usage
```java
ConfirmEnrollEntityResponse response = protocol.confirmEnrollEntity(request);
```
<br>

---

## 1.3 WalletRegistrationProtocol

- 구현 클래스: `WalletRegistrationProtocolImpl`는 `WalletRegistrationProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `WalletRegistrationProtocolImpl(EntityProvider entityProvider, WalletRegistrationProvider walletRegistrationProvider, StorageProvider storageProvider, DidDocumentProvider didDocumentProvider, CertificateProvider certificateProvider, WalletProvider walletProvider)`
- 공통 메서드: `WalletRegistrationProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.3.1 registerWallet

#### Class Name
`WalletRegistrationProtocol`

#### Function Name
`registerWallet`

#### Function Introduction
`월렛 DID Document를 등록합니다 (P131). attestedDidDoc.provider의 Wallet Provider가 등록된 WALLET_PROVIDER Entity이고 유효한 상태인지 확인(EntityProvider)하고, certVcRef가 있으면 Certificate VC를 검증하고, AttestedDidDoc에 대한 Provider의 assertionMethod 서명을 검증하고, 중복 walletId를 거부하고, 소유자 DID Document의 키 서명을 검증한 뒤, TAS가 서명한 InvokedDidDoc을 RoleType.WALLET으로 등록합니다. 실패 시 SDK_FIELD_REQUIRED, WALLET_PROVIDER_NOT_REGISTERED, ATTESTATION_VERIFICATION_FAILED, WALLET_ID_ALREADY_EXISTS, DID_DOCUMENT_KEY_VERIFICATION_FAILED, INVOKED_DID_DOC_CREATION_FAILED, DID_DOCUMENT_REGISTRATION_FAILED 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RegisterWalletRequest`](#764-registerwalletrequest) | 월렛 등록 요청 데이터 | M | attestedDidDoc(`AttestedDidDoc`: walletId, ownerDidDoc, provider, nonce, proof) 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RegisterWalletResponse`](#765-registerwalletresponse) | 월렛 등록 응답 | M | walletDid 포함. txId는 SDK가 설정하지 않음 |

#### Function Declaration
```java
RegisterWalletResponse registerWallet(RegisterWalletRequest request) throws TaSdkException
```

#### Function Usage
```java
WalletRegistrationProtocol protocol = new WalletRegistrationProtocolImpl(
    entityProvider, walletRegistrationProvider, storageProvider,
    didDocumentProvider, certificateProvider, walletProvider
);
RegisterWalletResponse response = protocol.registerWallet(request);
```
<br>

---

## 1.4 UserRegistrationProtocol

- 구현 클래스: `UserRegistrationProtocolImpl`는 `UserRegistrationProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `UserRegistrationProtocolImpl(TokenProvider tokenProvider, KycProvider kycProvider, UserProvider userProvider, DidDocumentProvider didDocumentProvider, WalletProvider walletProvider, StorageProvider storageProvider, CertificateProvider certificateProvider)`
- 공통 메서드: `UserRegistrationProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.4.1 proposeRegisterUser

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`proposeRegisterUser`

#### Function Introduction
`사용자 등록 프로토콜 Step 1 (P132). UUID txId를 생성합니다. SDK는 트랜잭션을 저장하지 않으므로 호출자가 저장합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeRegisterUserRequest`](#730-proposeregisteruserrequest) | 사용자 등록 제안 요청 | M | SDK는 요청 값을 읽지 않음 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeRegisterUserResponse`](#731-proposeregisteruserresponse) | 등록 제안 응답 | M | txId 포함 |

#### Function Declaration
```java
ProposeRegisterUserResponse proposeRegisterUser(ProposeRegisterUserRequest request) throws TaSdkException
```

#### Function Usage
```java
UserRegistrationProtocol protocol = new UserRegistrationProtocolImpl(
    tokenProvider, kycProvider, userProvider, didDocumentProvider,
    walletProvider, storageProvider, certificateProvider
);
ProposeRegisterUserResponse response = protocol.proposeRegisterUser(request);
```
<br>

### 1.4.2 retrieveKyc

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`retrieveKyc`

#### Function Introduction
`사용자 등록 프로토콜 Step 4 (P132). serverToken을 검증(TokenProvider)하고, kycToken이 있으면 KycProvider.getPiiFromKycToken으로, 없으면 KycProvider.retrieveKycData(kycTxId)로 PII를 조회합니다. 둘 다 비어 있으면 KYC_TOKEN_AND_TXID_BOTH_MISSING, Provider가 null을 반환하면 KYC_DATA_RETRIEVAL_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RetrieveKycRequest`](#732-retrievekycrequest) | KYC 조회 요청 | M | txId, serverToken, kycToken 또는 kycTxId 포함 (kycToken 우선) |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RetrieveKycResponse`](#733-retrievekycresponse) | KYC 조회 응답 | M | txId, piiData 포함. piiData는 서버가 txId와 연결해 저장하는 내부용 값 |

#### Function Declaration
```java
RetrieveKycResponse retrieveKyc(RetrieveKycRequest request) throws TaSdkException
```

#### Function Usage
```java
RetrieveKycResponse response = protocol.retrieveKyc(request);
```
<br>

### 1.4.3 requestRegisterUser

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`requestRegisterUser`

#### Function Introduction
`사용자 등록 프로토콜 Step 5 (P132). serverToken을 검증하고, 소유자 DID Document를 디코딩해 키 서명을 검증하고, UserProvider.existsUserByDid가 true인 DID를 거부하고, SignedDidDoc에 대한 월렛의 assertionMethod 서명을 검증한 뒤, TAS가 서명한 InvokedDidDoc을 RoleType.ETC로 등록합니다. 실패 시 SDK_INVALID_MULTIBASE, OWNER_DID_DOC_KEY_PROOF_FAILED, USER_DID_ALREADY_EXISTS, SIGNED_DID_DOC_WALLET_PROOF_FAILED, INVOKED_DID_DOC_CREATION_FAILED, DID_DOCUMENT_REGISTRATION_FAILED 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestRegisterUserRequest`](#734-requestregisteruserrequest) | 사용자 등록 요청 데이터 | M | txId, serverToken, signedDidDoc 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestRegisterUserResponse`](#735-requestregisteruserresponse) | 등록 요청 응답 | M | txId 포함 |

#### Function Declaration
```java
RequestRegisterUserResponse requestRegisterUser(RequestRegisterUserRequest request) throws TaSdkException
```

#### Function Usage
```java
RequestRegisterUserResponse response = protocol.requestRegisterUser(request);
```
<br>

### 1.4.4 confirmRegisterUser

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`confirmRegisterUser`

#### Function Introduction
`사용자 등록 프로토콜 Step 6 (P132). serverToken을 검증하고 txId를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmRegisterUserRequest`](#736-confirmregisteruserrequest) | 사용자 등록 확인 요청 | M | txId, serverToken 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmRegisterUserResponse`](#737-confirmregisteruserresponse) | 등록 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmRegisterUserResponse confirmRegisterUser(ConfirmRegisterUserRequest request) throws TaSdkException
```

#### Function Usage
```java
ConfirmRegisterUserResponse response = protocol.confirmRegisterUser(request);
```
<br>

---

## 1.5 DidDocUpdateProtocol

- 구현 클래스: `DidDocUpdateProtocolImpl`는 `DidDocUpdateProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `DidDocUpdateProtocolImpl(TokenProvider tokenProvider, UserProvider userProvider, DidDocumentProvider didDocumentProvider, WalletProvider walletProvider, StorageProvider storageProvider, CertificateProvider certificateProvider, TransactionProvider transactionProvider)`
- 공통 메서드: `DidDocUpdateProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.5.1 proposeUpdateDidDoc

#### Class Name
`DidDocUpdateProtocol`

#### Function Name
`proposeUpdateDidDoc`

#### Function Introduction
`DID Document 갱신 프로토콜 Step 1 (P141). UUID txId와 16바이트 authNonce(multibase 인코딩)를 생성합니다. SDK는 사용자 상태를 검사하지 않고 트랜잭션도 저장하지 않으므로, 서버가 호출 전에 사용자가 ACTIVATED인지 확인하고 Step 4를 위해 txId, did, authNonce를 저장해야 합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeUpdateDidDocRequest`](#738-proposeupdatediddocrequest) | DID Document 갱신 제안 요청 | M | did 포함. SDK는 읽지 않음 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeUpdateDidDocResponse`](#739-proposeupdatediddocresponse) | 갱신 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeUpdateDidDocResponse proposeUpdateDidDoc(ProposeUpdateDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
DidDocUpdateProtocol protocol = new DidDocUpdateProtocolImpl(
    tokenProvider, userProvider, didDocumentProvider, walletProvider,
    storageProvider, certificateProvider, transactionProvider
);
ProposeUpdateDidDocResponse response = protocol.proposeUpdateDidDoc(request);
```
<br>

### 1.5.2 requestUpdateDidDoc

#### Class Name
`DidDocUpdateProtocol`

#### Function Name
`requestUpdateDidDoc`

#### Function Introduction
`DID Document 갱신 프로토콜 Step 4 (P141). serverToken을 검증하고, 갱신된 DID Document의 키 서명과 SignedDidDoc의 월렛 서명을 검증하고, txId에 저장된 authNonce와 DID(TransactionProvider)로 DID Auth를 검증한 뒤, versionId가 현재 버전 + 1인지, updated가 현재 값보다 나중인지, @context·id·controller·created·deactivated가 바뀌지 않았는지 확인하고, TAS가 서명한 InvokedDidDoc을 RoleType.ETC로 등록합니다. 실패 시 AUTH_NONCE_MISMATCH, DID_MISMATCH, INVALID_PROOF_PURPOSE, SDK_SIGNATURE_VERIFICATION_FAILED, INVALID_DID_DOC_VERSION, INVALID_DID_DOC_UPDATED, SDK_DID_DOCUMENT_REGISTRATION_FAILED 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestUpdateDidDocRequest`](#740-requestupdatediddocrequest) | DID Document 갱신 요청 데이터 | M | txId, serverToken, signedDidDoc, didAuth 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestUpdateDidDocResponse`](#741-requestupdatediddocresponse) | 갱신 요청 응답 | M | txId 포함 |

#### Function Declaration
```java
RequestUpdateDidDocResponse requestUpdateDidDoc(RequestUpdateDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
RequestUpdateDidDocResponse response = protocol.requestUpdateDidDoc(request);
```
<br>

### 1.5.3 confirmUpdateDidDoc

#### Class Name
`DidDocUpdateProtocol`

#### Function Name
`confirmUpdateDidDoc`

#### Function Introduction
`DID Document 갱신 프로토콜 Step 5 (P141). serverToken을 검증하고 txId를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmUpdateDidDocRequest`](#742-confirmupdatediddocrequest) | DID Document 갱신 확인 요청 | M | txId, serverToken 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmUpdateDidDocResponse`](#743-confirmupdatediddocresponse) | 갱신 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmUpdateDidDocResponse confirmUpdateDidDoc(ConfirmUpdateDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
ConfirmUpdateDidDocResponse response = protocol.confirmUpdateDidDoc(request);
```
<br>

---

## 1.6 DidDocRestoreProtocol

- 구현 클래스: `DidDocRestoreProtocolImpl`는 `DidDocRestoreProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `DidDocRestoreProtocolImpl(TokenProvider tokenProvider, TransactionProvider transactionProvider, DidDocumentProvider didDocumentProvider, StorageProvider storageProvider)`
- 공통 메서드: `DidDocRestoreProtocol`는 `Protocol`을 상속합니다. `getProtocolCode()`, `getProtocolName()`, `getVersion()`은 [1.9 Protocol](#19-protocol)을 참조하세요.

### 1.6.1 proposeRestoreDidDoc

#### Class Name
`DidDocRestoreProtocol`

#### Function Name
`proposeRestoreDidDoc`

#### Function Introduction
`DID Document 복구 프로토콜 Step 1 (P142). UUID txId와 16바이트 authNonce(multibase 인코딩)를 생성합니다. SDK는 사용자 상태를 검사하지 않고 트랜잭션도 저장하지 않으므로, 서버가 호출 전에 사용자가 DEACTIVATED인지 확인하고 Step 4를 위해 txId, did, authNonce를 저장해야 합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeRestoreDidDocRequest`](#744-proposerestorediddocrequest) | DID Document 복구 제안 요청 | M | did, offerId(선택) 포함. SDK는 읽지 않음 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeRestoreDidDocResponse`](#745-proposerestorediddocresponse) | 복구 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeRestoreDidDocResponse proposeRestoreDidDoc(ProposeRestoreDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
DidDocRestoreProtocol protocol = new DidDocRestoreProtocolImpl(
    tokenProvider, transactionProvider, didDocumentProvider, storageProvider
);
ProposeRestoreDidDocResponse response = protocol.proposeRestoreDidDoc(request);
```
<br>

### 1.6.2 requestRestoreDidDoc

#### Class Name
`DidDocRestoreProtocol`

#### Function Name
`requestRestoreDidDoc`

#### Function Introduction
`DID Document 복구 프로토콜 Step 4 (P142). serverToken을 검증하고, txId에 저장된 authNonce와 DID(TransactionProvider)로 DID Auth를 검증한 뒤 StorageProvider.updateDidDocumentStatus(did, "ACTIVATED")를 호출합니다. 실패 시 AUTH_NONCE_MISMATCH, DID_MISMATCH, INVALID_PROOF_PURPOSE, SDK_SIGNATURE_VERIFICATION_FAILED, REQUEST_RESTORE_DIDDOC_FAILED 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestRestoreDidDocRequest`](#746-requestrestorediddocrequest) | DID Document 복구 요청 데이터 | M | txId, serverToken, didAuth 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestRestoreDidDocResponse`](#747-requestrestorediddocresponse) | 복구 요청 응답 | M | txId 포함 |

#### Function Declaration
```java
RequestRestoreDidDocResponse requestRestoreDidDoc(RequestRestoreDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
RequestRestoreDidDocResponse response = protocol.requestRestoreDidDoc(request);
```
<br>

### 1.6.3 confirmRestoreDidDoc

#### Class Name
`DidDocRestoreProtocol`

#### Function Name
`confirmRestoreDidDoc`

#### Function Introduction
`DID Document 복구 프로토콜 Step 5 (P142). serverToken을 검증하고 txId를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmRestoreDidDocRequest`](#748-confirmrestorediddocrequest) | DID Document 복구 확인 요청 | M | txId, serverToken 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmRestoreDidDocResponse`](#749-confirmrestorediddocresponse) | 복구 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmRestoreDidDocResponse confirmRestoreDidDoc(ConfirmRestoreDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
ConfirmRestoreDidDocResponse response = protocol.confirmRestoreDidDoc(request);
```
<br>

---

## 1.7 VcIssuanceProtocol

- 구현 클래스: `VcIssuanceProtocolImpl`는 `VcIssuanceProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `VcIssuanceProtocolImpl(TokenProvider tokenProvider, DidDocumentProvider didDocumentProvider)`
- 공통 메서드: `VcIssuanceProtocol`는 `Protocol`을 상속하지 않으므로 `getProtocolCode()`, `getProtocolName()`, `getVersion()`이 없습니다.

### 1.7.1 proposeIssueVc

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`proposeIssueVc`

#### Function Introduction
`VC 발급 프로토콜 Step 2 (P210). VC Plan을 조회(VcIssuanceDataProvider.getVcPlan)하고, request.issuer가 없으면 Plan의 issuerDid를 사용해 IssuerApiClient.inspectProposeIssue를 호출한 뒤, 새 TAS txId와 Issuer txId(externalTxId), refId를 반환합니다. 호출자는 이후 단계를 위해 이 값을 저장합니다. 실패 시 VC_PLAN_NOT_FOUND, ISSUER_NOT_FOUND, ISSUER_COMMUNICATION_FAILED(IssuerApiException 또는 응답의 txId·refId 누락) TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeIssueVcRequest`](#750-proposeissuevcrequest) | VC 발급 제안 요청 | M | vcPlanId, issuer(선택), offerId(선택) 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeIssueVcResult`](#751-proposeissuevcresult) | VC 발급 제안 응답 | M | txId, externalTxId, refId 포함 |

#### Function Declaration
```java
ProposeIssueVcResult proposeIssueVc(ProposeIssueVcRequest request, IssuerApiClient issuerClient, VcIssuanceDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
VcIssuanceProtocol protocol = new VcIssuanceProtocolImpl(tokenProvider, didDocumentProvider);
ProposeIssueVcResult result = protocol.proposeIssueVc(request, issuerClient, dataProvider);
```
<br>

### 1.7.2 requestIssueProfile

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`requestIssueProfile`

#### Function Introduction
`VC 발급 프로토콜 Step 5 (P210). serverToken을 검증하고, VcIssuanceDataProvider에서 Issuer txId와 Holder 정보를 가져와 IssuerApiClient.generateIssueProfile을 호출한 뒤, 새 16바이트 authNonce를 생성합니다. Step 6에서 VcIssuanceDataProvider.getAuthNonce(txId)가 반환하도록 호출자가 authNonce를 저장합니다. 실패 시 HOLDER_NOT_FOUND, ISSUER_COMMUNICATION_FAILED, ISSUE_PROFILE_RETRIEVAL_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestIssueProfileRequest`](#752-requestissueprofilerequest) | Issue Profile 요청 | M | txId, serverToken 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestIssueProfileResult`](#753-requestissueprofileresult) | Issue Profile 응답 | M | txId, authNonce, profile 포함 |

#### Function Declaration
```java
RequestIssueProfileResult requestIssueProfile(RequestIssueProfileRequest request, IssuerApiClient issuerClient, VcIssuanceDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
RequestIssueProfileResult result = protocol.requestIssueProfile(request, issuerClient, dataProvider);
```
<br>

### 1.7.3 requestIssueVc

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`requestIssueVc`

#### Function Introduction
`VC 발급 프로토콜 Step 6 (P210). serverToken을 검증하고, VcIssuanceDataProvider.getAuthNonce(txId)로 DID Auth를 검증한 뒤, accE2e와 encReqVc를 Issuer txId와 함께 IssuerApiClient.issueVc로 전달합니다. Issuer가 E2E 암호화한 VC를 그대로 반환합니다. 실패 시 AUTH_NONCE_MISMATCH, SDK_SIGNATURE_VERIFICATION_FAILED, ISSUER_COMMUNICATION_FAILED, VC_ISSUANCE_FAILED(e2e 없음), VC_ENCRYPTION_FAILED(encVc 없음) 등의 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestIssueVcRequest`](#754-requestissuevcrequest) | VC 발급 요청 데이터 | M | txId, serverToken, didAuth, accE2e, encReqVc 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestIssueVcResult`](#755-requestissuevcresult) | VC 발급 요청 응답 | M | txId, e2e 포함 |

#### Function Declaration
```java
RequestIssueVcResult requestIssueVc(RequestIssueVcRequest request, IssuerApiClient issuerClient, VcIssuanceDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
RequestIssueVcResult result = protocol.requestIssueVc(request, issuerClient, dataProvider);
```
<br>

### 1.7.4 confirmIssueVc

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`confirmIssueVc`

#### Function Introduction
`VC 발급 프로토콜 Step 7 (P210). serverToken을 검증하고, vcId가 UUID인지 확인한 뒤 Issuer txId로 IssuerApiClient.completeVc를 호출합니다. 실패 시 VC_ID_MISMATCH, ISSUER_COMMUNICATION_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmIssueVcRequest`](#756-confirmissuevcrequest) | VC 발급 확인 요청 | M | txId, serverToken, vcId(UUID) 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmIssueVcResult`](#757-confirmissuevcresult) | VC 발급 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmIssueVcResult confirmIssueVc(ConfirmIssueVcRequest request, IssuerApiClient issuerClient, VcIssuanceDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
ConfirmIssueVcResult result = protocol.confirmIssueVc(request, issuerClient, dataProvider);
```
<br>

---

## 1.8 VcRevocationProtocol

- 구현 클래스: `VcRevocationProtocolImpl`는 `VcRevocationProtocol`를 구현합니다. 메서드는 이 절을 참조하세요.
- 생성자: `VcRevocationProtocolImpl()`
- 공통 메서드: `VcRevocationProtocol`는 `Protocol`을 상속하지 않으므로 `getProtocolCode()`, `getProtocolName()`, `getVersion()`이 없습니다.

### 1.8.1 proposeRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`proposeRevokeVc`

#### Function Introduction
`VC 폐기 프로토콜 Step 1 (P220). VC 메타데이터를 조회(VcRevocationDataProvider.getVcMeta)해 상태가 "ACTIVE"인지 확인하고, IssuerApiClient.inspectProposeRevoke를 호출한 뒤 새 txId와 issuerNonce, authType을 반환합니다. 호출자는 Step 4를 위해 txId, vcId, issuerNonce를 저장합니다. 실패 시 VC_META_FETCH_FAILED, VC_ALREADY_REVOKED, VC_EXPIRED, VC_STATUS_INVALID, ISSUER_NOT_FOUND, PROPOSE_REVOKE_VC_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ProposeRevokeVcRequest`](#758-proposerevokevcrequest) | VC 폐기 제안 요청 | M | vcId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ProposeRevokeVcResult`](#759-proposerevokevcresult) | VC 폐기 제안 응답 | M | txId, issuerNonce, authType 포함 |

#### Function Declaration
```java
ProposeRevokeVcResult proposeRevokeVc(ProposeRevokeVcRequest request, IssuerApiClient issuerClient, VcRevocationDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
VcRevocationProtocol protocol = new VcRevocationProtocolImpl();
ProposeRevokeVcResult result = protocol.proposeRevokeVc(request, issuerClient, dataProvider);
```
<br>

### 1.8.2 requestRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`requestRevokeVc`

#### Function Introduction
`VC 폐기 프로토콜 Step 4 (P220). request.vcId와 request.issuerNonce가 VcRevocationDataProvider.getVcId(txId), getIssuerNonce(txId)와 같은지 확인하고, request.proof의 필드가 모두 있고 목적이 assertionMethod인지 확인한 뒤 IssuerApiClient.revokeVc를 호출합니다. 이 단계에서 SDK는 proof 서명과 serverToken을 검증하지 않습니다. 실패 시 VC_ID_MISMATCH, ISSUER_NONCE_MISMATCH, SIGNATURE_INVALID, REQUEST_REVOKE_VC_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`RequestRevokeVcRequest`](#760-requestrevokevcrequest) | VC 폐기 요청 데이터 | M | txId, serverToken, request(`ReqRevokeVc`) 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`RequestRevokeVcResult`](#761-requestrevokevcresult) | VC 폐기 요청 응답 | M | txId 포함 |

#### Function Declaration
```java
RequestRevokeVcResult requestRevokeVc(RequestRevokeVcRequest request, IssuerApiClient issuerClient, VcRevocationDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
RequestRevokeVcResult result = protocol.requestRevokeVc(request, issuerClient, dataProvider);
```
<br>

### 1.8.3 confirmRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`confirmRevokeVc`

#### Function Introduction
`VC 폐기 프로토콜 Step 5 (P220). 요청 id와 TAS txId로 IssuerApiClient.completeRevoke를 호출하고 txId를 반환합니다. 이 단계에서 SDK는 serverToken을 검증하지 않습니다. 실패 시 CONFIRM_REVOKE_VC_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | [`ConfirmRevokeVcRequest`](#762-confirmrevokevcrequest) | VC 폐기 확인 요청 | M | txId, serverToken(SDK가 검증하지 않음) 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| [`ConfirmRevokeVcResult`](#763-confirmrevokevcresult) | VC 폐기 확인 응답 | M | txId 포함 |

#### Function Declaration
```java
ConfirmRevokeVcResult confirmRevokeVc(ConfirmRevokeVcRequest request, IssuerApiClient issuerClient, VcRevocationDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
ConfirmRevokeVcResult result = protocol.confirmRevokeVc(request, issuerClient, dataProvider);
```
<br>

---

## 1.9 Protocol

`Protocol`은 `TasEnrollmentProtocol`, `EntityEnrollmentProtocol`, `WalletRegistrationProtocol`, `UserRegistrationProtocol`, `DidDocUpdateProtocol`, `DidDocRestoreProtocol`의 공통 상위 인터페이스입니다. 각 인터페이스가 아래 세 메서드를 default 메서드로 구현하므로 구현 클래스에서 재정의할 필요가 없습니다. `VcIssuanceProtocol`과 `VcRevocationProtocol`은 `Protocol`을 상속하지 않습니다.

**패키지**: `org.omnione.did.tas.protocol`

| 인터페이스 | getProtocolCode() | getProtocolName() | getVersion() |
|---|---|---|---|
| `TasEnrollmentProtocol` | `P110` | `TAS Registration Protocol` | `1.0` |
| `EntityEnrollmentProtocol` | `P120` | `Entity Registration Protocol` | `1.0` |
| `WalletRegistrationProtocol` | `P131` | `Wallet Registration Protocol` | `1.0` |
| `UserRegistrationProtocol` | `P132` | `User Registration Protocol` | `1.0` |
| `DidDocUpdateProtocol` | `P141` | `DID Document Update Protocol` | `1.0` |
| `DidDocRestoreProtocol` | `P142` | `DID Document Restore Protocol` | `1.0` |

### 1.9.1 getProtocolCode

#### Class Name
`Protocol`

#### Function Name
`getProtocolCode`

#### Function Introduction
`프로토콜 코드(예: "P110")를 반환합니다. 프로토콜별 값은 위 표를 참조하세요.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 프로토콜 코드 | M | |

#### Function Declaration
```java
String getProtocolCode()
```

#### Function Usage
```java
String code = protocol.getProtocolCode(); // TasEnrollmentProtocol이면 "P110"
```
<br>

### 1.9.2 getProtocolName

#### Class Name
`Protocol`

#### Function Name
`getProtocolName`

#### Function Introduction
`프로토콜 이름(예: "TAS Registration Protocol")을 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 프로토콜 이름 | M | |

#### Function Declaration
```java
String getProtocolName()
```

#### Function Usage
```java
String name = protocol.getProtocolName();
```
<br>

### 1.9.3 getVersion

#### Class Name
`Protocol`

#### Function Name
`getVersion`

#### Function Introduction
`프로토콜 버전을 반환합니다. 모든 프로토콜이 "1.0"을 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 프로토콜 버전 | M | |

#### Function Declaration
```java
String getVersion()
```

#### Function Usage
```java
String version = protocol.getVersion(); // "1.0"
```
<br>

---

<div style="page-break-after: always;"></div>

# 2. APIs - Providers

Provider 인터페이스는 SDK와 서버 사이의 계약입니다. 서버는 각 Provider 인터페이스를 구현하여 Protocol 구현체에 주입합니다. Provider를 통해 SDK는 DB, 블록체인, 외부 시스템에 대한 의존성 없이 동작합니다.

## 2.1 CertificateProvider

### 2.1.1 publishCertificateVc

#### Class Name
`CertificateProvider`

#### Function Name
`publishCertificateVc`

#### Function Introduction
`Certificate VC를 데이터베이스에 저장하고 공개 접근 가능한 URL을 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| certificateVc | `VerifiableCredential` | 저장할 Certificate VC | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Certificate VC에 접근 가능한 공개 URL | M | |

#### Function Declaration
```java
String publishCertificateVc(VerifiableCredential certificateVc)
```
<br>

### 2.1.2 generateCertificateVcUrl

#### Class Name
`CertificateProvider`

#### Function Name
`generateCertificateVcUrl`

#### Function Introduction
`특정 DID에 대한 Certificate VC URL을 생성합니다. 실제 저장 없이 URL 형식만 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | Certificate VC URL을 생성할 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 생성된 Certificate VC URL | M | |

#### Function Declaration
```java
String generateCertificateVcUrl(String did)
```
<br>

### 2.1.3 getCertificateVc

#### Class Name
`CertificateProvider`

#### Function Name
`getCertificateVc`

#### Function Introduction
`VC ID로 Certificate VC를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | 조회할 VC ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VerifiableCredential` | 조회된 Certificate VC | M | |

#### Function Declaration
```java
VerifiableCredential getCertificateVc(String vcId)
```
<br>

### 2.1.4 validateCertificateVc

#### Class Name
`CertificateProvider`

#### Function Name
`validateCertificateVc`

#### Function Introduction
`주어진 URL의 Certificate VC를 조회하여 제공자 DID와 일치하는지 검증합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| certVcRef | `String` | Certificate VC 참조 URL | M | |
| providerDid | `String` | 검증할 제공자 DID | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void validateCertificateVc(String certVcRef, String providerDid)
```
<br>

---

## 2.2 DidDocumentProvider

### 2.2.1 getDidDocument

#### Class Name
`DidDocumentProvider`

#### Function Name
`getDidDocument`

#### Function Introduction
`DID 식별자로 DID Document를 조회합니다. 캐시 또는 블록체인에서 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 DID 식별자 | M | DID Auth 등 서명 검증 시 SDK가 `#keyId`와 경우에 따라 `?versionId=`가 붙은 verificationMethod DID URL을 넘기므로, 해당 DID의 DID Document를 반환해야 함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | 조회된 DID Document | M | |

#### Function Declaration
```java
DidDocument getDidDocument(String did)
```
<br>

### 2.2.2 getTasDidDocument

#### Class Name
`DidDocumentProvider`

#### Function Name
`getTasDidDocument`

#### Function Introduction
`TAS 자신의 DID Document를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | TAS DID Document | M | |

#### Function Declaration
```java
DidDocument getTasDidDocument()
```
<br>

---

## 2.3 EcdhSessionProvider

### 2.3.1 saveEcdhSession

#### Class Name
`EcdhSessionProvider`

#### Function Name
`saveEcdhSession`

#### Function Introduction
`ECDH 키 협의 완료 후 세션 데이터(세션 키, 병합 Nonce)를 저장합니다. TaEcdhService가 클라이언트 DID로 호출하므로, 아래 txId 기반 메서드가 찾을 수 있도록 서버가 세션을 현재 트랜잭션과 연결해야 합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| clientDid | `String` | 클라이언트 DID | M | |
| sessionKey | `String` | ECDH 협의된 세션 키 (multibase 인코딩) | M | |
| mergedNonce | `String` | 병합된 Nonce (clientNonce + serverNonce, multibase 인코딩) | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void saveEcdhSession(String clientDid, String sessionKey, String mergedNonce)
```
<br>

### 2.3.2 getSessionKey

#### Class Name
`EcdhSessionProvider`

#### Function Name
`getSessionKey`

#### Function Introduction
`트랜잭션 ID로 ECDH 세션 키를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | ECDH 세션 키 (multibase 인코딩) | M | |

#### Function Declaration
```java
String getSessionKey(String txId)
```
<br>

### 2.3.3 getCipher

#### Class Name
`EcdhSessionProvider`

#### Function Name
`getCipher`

#### Function Introduction
`트랜잭션 ID로 협의된 암호화 알고리즘을 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 협의된 암호화 알고리즘 (예: "AES-256-CBC") | M | |

#### Function Declaration
```java
String getCipher(String txId)
```
<br>

### 2.3.4 getPadding

#### Class Name
`EcdhSessionProvider`

#### Function Name
`getPadding`

#### Function Introduction
`트랜잭션 ID로 협의된 패딩 방식을 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 협의된 패딩 방식 (예: "PKCS5") | M | |

#### Function Declaration
```java
String getPadding(String txId)
```
<br>

### 2.3.5 getClientDid

#### Class Name
`EcdhSessionProvider`

#### Function Name
`getClientDid`

#### Function Introduction
`트랜잭션 ID로 클라이언트 DID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 클라이언트 DID | M | |

#### Function Declaration
```java
String getClientDid(String txId)
```
<br>

### 2.3.6 existsEcdhSession

#### Class Name
`EcdhSessionProvider`

#### Function Name
`existsEcdhSession`

#### Function Introduction
`트랜잭션 ID에 해당하는 ECDH 세션이 존재하는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | ECDH 세션 존재 여부 | M | |

#### Function Declaration
```java
boolean existsEcdhSession(String txId)
```
<br>

---

## 2.4 EntityProvider

### 2.4.1 getEntityByDid

#### Class Name
`EntityProvider`

#### Function Name
`getEntityByDid`

#### Function Introduction
`DID 식별자로 Entity 정보를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 Entity DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Entity` | 조회된 Entity 정보 | M | |

#### Function Declaration
```java
Entity getEntityByDid(String did)
```
<br>

### 2.4.2 getEntityByRole

#### Class Name
`EntityProvider`

#### Function Name
`getEntityByRole`

#### Function Introduction
`역할 타입으로 Entity를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| roleType | `RoleType` | 조회할 역할 타입 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Entity` | 조회된 Entity 정보, 없으면 null | O | |

#### Function Declaration
```java
Entity getEntityByRole(RoleType roleType)
```
<br>

### 2.4.3 existsEntityByDid

#### Class Name
`EntityProvider`

#### Function Name
`existsEntityByDid`

#### Function Introduction
`특정 DID의 Entity가 존재하는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 확인할 Entity DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | Entity 존재 여부 | M | |

#### Function Declaration
```java
boolean existsEntityByDid(String did)
```
<br>

### 2.4.4 validateEntityStatus

#### Class Name
`EntityProvider`

#### Function Name
`validateEntityStatus`

#### Function Introduction
`Entity가 프로토콜 작업을 수행할 수 있는 상태인지 검증합니다. 어떤 상태가 유효한지는 서버가 정하며, SDK는 특정 상태값을 가정하지 않습니다. 유효하지 않으면 TaSdkException(예: ENTITY_NOT_COMPLETED)을 던집니다. P131에서 사용합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 검증할 Entity DID | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void validateEntityStatus(String did)
```
<br>

---

## 2.5 KycProvider

### 2.5.1 getPiiFromKycToken

#### Class Name
`KycProvider`

#### Function Name
`getPiiFromKycToken`

#### Function Introduction
`KYC 시스템 토큰으로 PII(개인식별정보)를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| kycToken | `String` | KYC 시스템 접근 토큰 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `PiiData` | 조회된 PII 데이터 | M | pii(CI), name 등 포함 |

#### Function Declaration
```java
PiiData getPiiFromKycToken(String kycToken)
```
<br>

### 2.5.2 retrieveKycData

#### Class Name
`KycProvider`

#### Function Name
`retrieveKycData`

#### Function Introduction
`KYC 트랜잭션 ID로 외부 KYC 서버 API를 호출하여 PII를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| kycTxId | `String` | KYC 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `PiiData` | 조회된 PII 데이터 | M | |

#### Function Declaration
```java
PiiData retrieveKycData(String kycTxId)
```
<br>

---

## 2.6 StorageProvider

### 2.6.1 registerDidDocument

#### Class Name
`StorageProvider`

#### Function Name
`registerDidDocument`

#### Function Introduction
`DID Document를 블록체인 또는 LSS에 등록합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| invokedDidDoc | `InvokedDidDoc` | 등록할 InvokedDidDoc | M | TAS 서명 포함 |
| roleType | `RoleType` | DID Document 역할 타입 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void registerDidDocument(InvokedDidDoc invokedDidDoc, RoleType roleType)
```
<br>

### 2.6.2 getDidDocument

#### Class Name
`StorageProvider`

#### Function Name
`getDidDocument`

#### Function Introduction
`블록체인 또는 LSS에서 DID Document를 조회합니다. 존재하지 않으면 예외를 발생시킵니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 DID 식별자 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | 조회된 DID Document | M | |

#### Function Declaration
```java
DidDocument getDidDocument(String did)
```
<br>

### 2.6.3 updateDidDocumentStatus

#### Class Name
`StorageProvider`

#### Function Name
`updateDidDocumentStatus`

#### Function Introduction
`블록체인 또는 LSS에서 DID Document의 상태를 변경합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 상태를 변경할 DID | M | |
| status | `String` | 변경할 상태 값 | M | 예: "ACTIVATED", "DEACTIVATED" |

#### Output Parameters
n/a

#### Function Declaration
```java
void updateDidDocumentStatus(String did, String status)
```
<br>

### 2.6.4 existsDidDocument

#### Class Name
`StorageProvider`

#### Function Name
`existsDidDocument`

#### Function Introduction
`블록체인 또는 LSS에 DID Document가 존재하는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 확인할 DID 식별자 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | DID Document 존재 여부 | M | |

#### Function Declaration
```java
boolean existsDidDocument(String did)
```
<br>

### 2.6.5 getDidDocumentVersionId

#### Class Name
`StorageProvider`

#### Function Name
`getDidDocumentVersionId`

#### Function Introduction
`블록체인 또는 LSS에서 DID Document 버전 ID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 DID 식별자 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | DID Document 버전 ID | M | |

#### Function Declaration
```java
String getDidDocumentVersionId(String did)
```
<br>

### 2.6.6 findDidDoc

#### Class Name
`StorageProvider`

#### Function Name
`findDidDoc`

#### Function Introduction
`블록체인 또는 LSS에서 DID Document를 조회합니다. 존재하지 않으면 null을 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 DID 식별자 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | 조회된 DID Document, 없으면 null | O | |

#### Function Declaration
```java
DidDocument findDidDoc(String did)
```
<br>

### 2.6.7 registerVcMeta

#### Class Name
`StorageProvider`

#### Function Name
`registerVcMeta`

#### Function Introduction
`VC 메타데이터를 블록체인 또는 LSS에 등록합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcMeta | `VcMeta` | 등록할 VC 메타데이터 | M | `org.omnione.did.data.model.vc.VcMeta`. id, issuer, subject, status 등 포함 |

#### Output Parameters
n/a

#### Function Declaration
```java
void registerVcMeta(VcMeta vcMeta)
```
<br>

### 2.6.8 findVcMeta

#### Class Name
`StorageProvider`

#### Function Name
`findVcMeta`

#### Function Introduction
`VC ID로 블록체인 또는 LSS에서 VC 메타데이터를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | 조회할 VC ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcMeta` | 조회된 VC 메타데이터 | M | `org.omnione.did.data.model.vc.VcMeta`. 없으면 TaSdkException |

#### Function Declaration
```java
VcMeta findVcMeta(String vcId)
```
<br>

---

## 2.7 TasProvider

### 2.7.1 getTasInfo

#### Class Name
`TasProvider`

#### Function Name
`getTasInfo`

#### Function Introduction
`TAS 엔티티 정보를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Tas` | TAS 엔티티 정보 | M | |

#### Function Declaration
```java
Tas getTasInfo()
```
<br>

### 2.7.2 getTasPassword

#### Class Name
`TasProvider`

#### Function Name
`getTasPassword`

#### Function Introduction
`TAS 등록에 사용되는 비밀번호를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | TAS 등록 비밀번호, 설정되지 않았으면 null | O | null 또는 빈 값이면 enrollTas가 TAS_PASSWORD_NOT_CONFIGURED를 던짐 |

#### Function Declaration
```java
String getTasPassword()
```
<br>

### 2.7.3 getTasStatus

#### Class Name
`TasProvider`

#### Function Name
`getTasStatus`

#### Function Introduction
`TAS의 현재 등록 상태를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `TasStatus` | TAS 등록 상태 | M | PENDING 또는 COMPLETED |

#### Function Declaration
```java
TasStatus getTasStatus()
```
<br>

### 2.7.4 getTasDid

#### Class Name
`TasProvider`

#### Function Name
`getTasDid`

#### Function Introduction
`TAS의 DID 식별자를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | TAS DID | M | |

#### Function Declaration
```java
String getTasDid()
```
<br>

---

## 2.8 TokenProvider

### 2.8.1 validateServerToken

#### Class Name
`TokenProvider`

#### Function Name
`validateServerToken`

#### Function Introduction
`서버 토큰을 트랜잭션 ID에 매핑하여 유효성을 검증합니다. 유효하지 않으면 예외를 발생시킵니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| serverToken | `String` | 검증할 서버 토큰 | M | |
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void validateServerToken(String serverToken, String txId)
```
<br>

---

## 2.9 TransactionProvider

### 2.9.1 getTransaction

#### Class Name
`TransactionProvider`

#### Function Name
`getTransaction`

#### Function Introduction
`외부 트랜잭션 ID(txId)로 Transaction 객체를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 조회할 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Transaction` | 조회된 트랜잭션 | M | |

#### Function Declaration
```java
Transaction getTransaction(String txId)
```
<br>

### 2.9.2 existsTransaction

#### Class Name
`TransactionProvider`

#### Function Name
`existsTransaction`

#### Function Introduction
`특정 txId의 트랜잭션이 존재하는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 확인할 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | 트랜잭션 존재 여부 | M | |

#### Function Declaration
```java
boolean existsTransaction(String txId)
```
<br>

---

## 2.10 UserProvider

### 2.10.1 findByDid

#### Class Name
`UserProvider`

#### Function Name
`findByDid`

#### Function Introduction
`DID로 사용자를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 사용자 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `User` | 조회된 사용자 | M | |

#### Function Declaration
```java
User findByDid(String did) throws TaSdkException
```
<br>

### 2.10.2 existsUserByDid

#### Class Name
`UserProvider`

#### Function Name
`existsUserByDid`

#### Function Introduction
`특정 DID의 사용자가 이미 등록되어 있는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 확인할 사용자 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | 사용자 존재 여부 | M | |

#### Function Declaration
```java
boolean existsUserByDid(String did)
```
<br>

### 2.10.3 existsUserByPii

#### Class Name
`UserProvider`

#### Function Name
`existsUserByPii`

#### Function Introduction
`특정 PII(CI)를 가진 사용자가 이미 등록되어 있는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| pii | `String` | 확인할 PII(CI) 값 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | 사용자 존재 여부 | M | |

#### Function Declaration
```java
boolean existsUserByPii(String pii)
```
<br>

---

## 2.11 VcIssuanceDataProvider

### 2.11.1 getAuthNonce

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getAuthNonce`

#### Function Introduction
`트랜잭션에서 authNonce를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | authNonce 값 | M | |

#### Function Declaration
```java
String getAuthNonce(String txId) throws TaSdkException
```
<br>

### 2.11.2 getExternalTxId

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getExternalTxId`

#### Function Introduction
`트랜잭션에서 Issuer의 외부 트랜잭션 ID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer 트랜잭션 ID | M | |

#### Function Declaration
```java
String getExternalTxId(String txId) throws TaSdkException
```
<br>

### 2.11.3 getRefId

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getRefId`

#### Function Introduction
`트랜잭션에서 refId를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | refId 값 | M | |

#### Function Declaration
```java
String getRefId(String txId) throws TaSdkException
```
<br>

### 2.11.4 getIssuerDid

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getIssuerDid`

#### Function Introduction
`트랜잭션에서 Issuer DID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer DID | M | |

#### Function Declaration
```java
String getIssuerDid(String txId) throws TaSdkException
```
<br>

### 2.11.5 getHolderInfo

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getHolderInfo`

#### Function Introduction
`트랜잭션에서 Issue Profile 생성에 필요한 Holder 정보를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `HolderInfo` | Holder 정보 | M | did, pii 포함. null이거나 did가 null이면 requestIssueProfile이 HOLDER_NOT_FOUND를 던짐 |

#### Function Declaration
```java
HolderInfo getHolderInfo(String txId) throws TaSdkException
```
<br>

### 2.11.6 getPushToken

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getPushToken`

#### Function Introduction
`Holder DID로 FCM Push Token을 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| holderDid | `String` | Holder DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | FCM Push Token | M | |

#### Function Declaration
```java
String getPushToken(String holderDid) throws TaSdkException
```
<br>

### 2.11.7 getVcPlan

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getVcPlan`

#### Function Introduction
`VC Plan ID로 VC Plan 정보를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcPlanId | `String` | VC Plan ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcPlanInfo` | VC Plan 정보 | M | vcPlanId, name, issuerDid, vcSchemaId 포함. null이면 proposeIssueVc가 VC_PLAN_NOT_FOUND를 던짐 |

#### Function Declaration
```java
VcPlanInfo getVcPlan(String vcPlanId) throws TaSdkException
```
<br>

### 2.11.8 getVcPlanIssuerDid

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getVcPlanIssuerDid`

#### Function Introduction
`VC Plan에서 Issuer DID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcPlanId | `String` | VC Plan ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer DID | M | |

#### Function Declaration
```java
String getVcPlanIssuerDid(String vcPlanId) throws TaSdkException
```
<br>

### 2.11.9 getIssuerServerUrl

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getIssuerServerUrl`

#### Function Introduction
`Deprecated. Issuer DID에 해당하는 Issuer 서버 기본 URL을 조회합니다. SDK 프로토콜은 이 메서드를 호출하지 않으므로 IssuerApiClient.getIssuerServerUrl()을 사용하세요.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| issuerDid | `String` | Issuer DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer 서버 기본 URL | M | |

#### Function Declaration
```java
@Deprecated
String getIssuerServerUrl(String issuerDid) throws TaSdkException
```

#### Function Usage
```java
// Deprecated: issuerClient.getIssuerServerUrl() 사용 권장
String issuerUrl = dataProvider.getIssuerServerUrl("did:omn:issuer");
```
<br>

---

## 2.12 VcRevocationDataProvider

### 2.12.1 getVcMeta

#### Class Name
`VcRevocationDataProvider`

#### Function Name
`getVcMeta`

#### Function Introduction
`VC ID로 블록체인에서 VC 메타데이터를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | VC ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcMeta` | VC 메타데이터 | M | `org.omnione.did.tas.model.domain.VcMeta` (vcId, issuer, subject, status, issuedDate, expirationDate). proposeRevokeVc는 status가 "ACTIVE"여야 함 |

#### Function Declaration
```java
VcMeta getVcMeta(String vcId) throws TaSdkException
```
<br>

### 2.12.2 getIssuerDid

#### Class Name
`VcRevocationDataProvider`

#### Function Name
`getIssuerDid`

#### Function Introduction
`트랜잭션에서 Issuer DID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer DID | M | |

#### Function Declaration
```java
String getIssuerDid(String txId) throws TaSdkException
```
<br>

### 2.12.3 getIssuerNonce

#### Class Name
`VcRevocationDataProvider`

#### Function Name
`getIssuerNonce`

#### Function Introduction
`트랜잭션에서 Issuer Nonce를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer Nonce | M | |

#### Function Declaration
```java
String getIssuerNonce(String txId) throws TaSdkException
```
<br>

### 2.12.4 getVcId

#### Class Name
`VcRevocationDataProvider`

#### Function Name
`getVcId`

#### Function Introduction
`트랜잭션에서 VC ID를 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| txId | `String` | 트랜잭션 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | VC ID | M | |

#### Function Declaration
```java
String getVcId(String txId) throws TaSdkException
```
<br>

---

## 2.13 VcSchemaProvider

### 2.13.1 getVcSchemaJson

#### Class Name
`VcSchemaProvider`

#### Function Name
`getVcSchemaJson`

#### Function Introduction
`스키마 ID로 VC 스키마 JSON을 조회합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| schemaId | `String` | VC 스키마 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | VC 스키마 JSON 문자열 | M | |

#### Function Declaration
```java
String getVcSchemaJson(String schemaId)
```
<br>

---

## 2.14 VcStorageProvider

### 2.14.1 saveVc

#### Class Name
`VcStorageProvider`

#### Function Name
`saveVc`

#### Function Introduction
`새로 발급된 VC를 저장합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vc | `VerifiableCredential` | 저장할 VC | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void saveVc(VerifiableCredential vc)
```
<br>

### 2.14.2 checkRevocationStatus

#### Class Name
`VcStorageProvider`

#### Function Name
`checkRevocationStatus`

#### Function Introduction
`VC가 폐기 상태인지 확인합니다. 폐기된 경우 예외를 발생시킵니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | 확인할 VC ID | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void checkRevocationStatus(String vcId)
```
<br>

### 2.14.3 revokeVc

#### Class Name
`VcStorageProvider`

#### Function Name
`revokeVc`

#### Function Introduction
`VC를 폐기 처리합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | 폐기할 VC ID | M | |
| reason | `String` | 폐기 사유 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void revokeVc(String vcId, String reason)
```
<br>

---

## 2.15 WalletProvider

### 2.15.1 signVc

#### Class Name
`WalletProvider`

#### Function Name
`signVc`

#### Function Introduction
`TAS 개인키로 VC에 서명합니다. VC 객체 자체가 수정되며, 호출 후 vc.getProof()가 proof를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vc | `VerifiableCredential` | 서명할 VC | M | |
| didDoc | `DidDocument` | TAS DID Document | M | |
| keyPurpose | `String` | 서명에 사용할 키 목적 | M | 월렛 키 ID. 예: "assert" |

#### Output Parameters
n/a

#### Function Declaration
```java
void signVc(VerifiableCredential vc, DidDocument didDoc, String keyPurpose)
```
<br>

### 2.15.2 signInvokedDidDoc

#### Class Name
`WalletProvider`

#### Function Name
`signInvokedDidDoc`

#### Function Introduction
`TAS 개인키로 InvokedDidDoc에 서명합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| unsignedInvokedDidDoc | `InvokedDidDoc` | 서명되지 않은 InvokedDidDoc | M | |
| keyId | `String` | 서명에 사용할 키 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `InvokedDidDoc` | TAS 서명이 추가된 InvokedDidDoc | M | |

#### Function Declaration
```java
InvokedDidDoc signInvokedDidDoc(InvokedDidDoc unsignedInvokedDidDoc, String keyId)
```
<br>

### 2.15.3 sign

#### Class Name
`WalletProvider`

#### Function Name
`sign`

#### Function Introduction
`TAS 개인키로 임의 데이터에 서명합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| message | `byte[]` | 서명할 데이터 | M | |
| keyId | `String` | 서명에 사용할 키 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | multibase 인코딩된 서명값 (proofValue) | M | |

#### Function Declaration
```java
String sign(byte[] message, String keyId)
```
<br>

### 2.15.4 isWalletConnected

#### Class Name
`WalletProvider`

#### Function Name
`isWalletConnected`

#### Function Introduction
`월렛 연결 상태를 확인합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | 월렛 연결 여부 | M | |

#### Function Declaration
```java
boolean isWalletConnected()
```
<br>

### 2.15.5 ensureWalletConnection

#### Class Name
`WalletProvider`

#### Function Name
`ensureWalletConnection`

#### Function Introduction
`월렛 연결을 보장합니다. 연결되어 있지 않으면 재연결을 시도하고, 재연결에 실패하면 TaSdkException을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
n/a

#### Function Declaration
```java
void ensureWalletConnection()
```
<br>

---

## 2.16 WalletRegistrationProvider

### 2.16.1 checkWalletIdExists

#### Class Name
`WalletRegistrationProvider`

#### Function Name
`checkWalletIdExists`

#### Function Introduction
`특정 walletId를 가진 월렛이 이미 등록되어 있는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletId | `String` | 확인할 월렛 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | walletId 존재 여부 | M | |

#### Function Declaration
```java
boolean checkWalletIdExists(String walletId)
```
<br>

### 2.16.2 checkWalletDidExists

#### Class Name
`WalletRegistrationProvider`

#### Function Name
`checkWalletDidExists`

#### Function Introduction
`특정 DID를 가진 월렛이 이미 등록되어 있는지 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 확인할 월렛 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | DID 존재 여부 | M | |

#### Function Declaration
```java
boolean checkWalletDidExists(String did)
```
<br>

---

## 2.17 IssuerApiClient

`IssuerApiClient`는 TA SDK가 P210(VC 발급)과 P220(VC 폐기)에서 Issuer 서버를 호출할 때 쓰는 인터페이스입니다. SDK는 인터페이스만 정의하고, 서버가 HTTP 호출(예: RestTemplate, WebClient, OpenFeign)을 구현해 `VcIssuanceProtocol`, `VcRevocationProtocol` 메서드에 넘깁니다. 호출이 실패하거나 Issuer가 오류를 반환하면 `IssuerApiException`을 던지며, 프로토콜은 이를 `TaSdkException`으로 바꿉니다. 요청·응답 타입은 [7. Data Models](#7-data-models)를 참조하세요.

**패키지**: `org.omnione.did.tas.client`

### 2.17.1 offerIssueVc

#### Class Name
`IssuerApiClient`

#### Function Name
`offerIssueVc`

#### Function Introduction
`Issuer에 VC 발급 Offer를 요청합니다 (P210 Step 1a QR, 1b 푸시, 1c 이메일). 이 버전의 SDK 프로토콜은 이 메서드를 호출하지 않으므로, Offer를 구현할 때 직접 호출합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `OfferIssueVcApiRequest` | POST /issuer/api/v1/offer-issue-vc 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `OfferIssueVcApiResponse` | POST /issuer/api/v1/offer-issue-vc 응답 본문 | M | offerId, validUntil 포함 |

#### Function Declaration
```java
OfferIssueVcApiResponse offerIssueVc(OfferIssueVcApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
OfferIssueVcApiResponse response = issuerClient.offerIssueVc(request);
```
<br>

### 2.17.2 inspectProposeIssue

#### Class Name
`IssuerApiClient`

#### Function Name
`inspectProposeIssue`

#### Function Introduction
`Issuer에 VC 발급 제안 검사를 요청합니다. VcIssuanceProtocol.proposeIssueVc(P210 Step 2)가 호출합니다. 응답에 txId나 refId가 없으면 프로토콜이 ISSUER_COMMUNICATION_FAILED를 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `InspectIssueProposeApiRequest` | POST /issuer/api/v1/inspect-propose-issue 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `InspectIssueProposeApiResponse` | POST /issuer/api/v1/inspect-propose-issue 응답 본문 | M | txId, refId 필수 |

#### Function Declaration
```java
InspectIssueProposeApiResponse inspectProposeIssue(InspectIssueProposeApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
@Override
public InspectIssueProposeApiResponse inspectProposeIssue(InspectIssueProposeApiRequest request)
        throws IssuerApiException {
    try {
        return restTemplate.postForObject(
            issuerServerUrl + "/issuer/api/v1/inspect-propose-issue",
            request, InspectIssueProposeApiResponse.class);
    } catch (RestClientException e) {
        throw new IssuerApiException("Issuer communication failed", e);
    }
}
```
<br>

### 2.17.3 generateIssueProfile

#### Class Name
`IssuerApiClient`

#### Function Name
`generateIssueProfile`

#### Function Introduction
`Issuer에 Issue Profile을 요청합니다. VcIssuanceProtocol.requestIssueProfile(P210 Step 5)이 호출합니다. profile이 null이면 프로토콜이 ISSUE_PROFILE_RETRIEVAL_FAILED를 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `GenerateIssueProfileApiRequest` | POST /issuer/api/v1/generate-issue-profile 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `GenerateIssueProfileApiResponse` | POST /issuer/api/v1/generate-issue-profile 응답 본문 | M | profile 필수 |

#### Function Declaration
```java
GenerateIssueProfileApiResponse generateIssueProfile(GenerateIssueProfileApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
GenerateIssueProfileApiResponse response = issuerClient.generateIssueProfile(request);
```
<br>

### 2.17.4 issueVc

#### Class Name
`IssuerApiClient`

#### Function Name
`issueVc`

#### Function Introduction
`Issuer에 VC 발급을 요청합니다. VcIssuanceProtocol.requestIssueVc(P210 Step 6)가 호출합니다. e2e가 null이면 VC_ISSUANCE_FAILED, e2e.encVc가 비어 있으면 VC_ENCRYPTION_FAILED를 프로토콜이 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `IssueVcApiRequest` | POST /issuer/api/v1/issue-vc 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `IssueVcApiResponse` | POST /issuer/api/v1/issue-vc 응답 본문 | M | e2e, e2e.encVc 필수 |

#### Function Declaration
```java
IssueVcApiResponse issueVc(IssueVcApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
IssueVcApiResponse response = issuerClient.issueVc(request);
```
<br>

### 2.17.5 completeVc

#### Class Name
`IssuerApiClient`

#### Function Name
`completeVc`

#### Function Introduction
`Issuer에 VC 발급 완료를 통보합니다. VcIssuanceProtocol.confirmIssueVc(P210 Step 7)가 호출합니다. 프로토콜은 반환 객체를 사용하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `CompleteVcApiRequest` | POST /issuer/api/v1/complete-vc 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `CompleteVcApiResponse` | POST /issuer/api/v1/complete-vc 응답 본문 | O | 필드 없음. SDK가 사용하지 않으므로 null 가능 |

#### Function Declaration
```java
CompleteVcApiResponse completeVc(CompleteVcApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
CompleteVcApiResponse response = issuerClient.completeVc(request);
```
<br>

### 2.17.6 inspectProposeRevoke

#### Class Name
`IssuerApiClient`

#### Function Name
`inspectProposeRevoke`

#### Function Introduction
`Issuer에 VC 폐기 제안 검사를 요청합니다. VcRevocationProtocol.proposeRevokeVc(P220 Step 1)가 호출합니다. issuerNonce가 비어 있으면 프로토콜이 PROPOSE_REVOKE_VC_FAILED를 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `InspectProposeRevokeApiRequest` | POST /issuer/api/v1/inspect-propose-revoke 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `InspectProposeRevokeApiResponse` | POST /issuer/api/v1/inspect-propose-revoke 응답 본문 | M | issuerNonce 필수 |

#### Function Declaration
```java
InspectProposeRevokeApiResponse inspectProposeRevoke(InspectProposeRevokeApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
InspectProposeRevokeApiResponse response = issuerClient.inspectProposeRevoke(request);
```
<br>

### 2.17.7 revokeVc

#### Class Name
`IssuerApiClient`

#### Function Name
`revokeVc`

#### Function Introduction
`Issuer에 VC 폐기를 요청합니다. VcRevocationProtocol.requestRevokeVc(P220 Step 4)가 호출합니다. 프로토콜은 반환 객체를 사용하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RevokeVcApiRequest` | POST /issuer/api/v1/revoke-vc 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RevokeVcApiResponse` | POST /issuer/api/v1/revoke-vc 응답 본문 | O | 필드 없음. SDK가 사용하지 않으므로 null 가능 |

#### Function Declaration
```java
RevokeVcApiResponse revokeVc(RevokeVcApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
RevokeVcApiResponse response = issuerClient.revokeVc(request);
```
<br>

### 2.17.8 completeRevoke

#### Class Name
`IssuerApiClient`

#### Function Name
`completeRevoke`

#### Function Introduction
`Issuer에 VC 폐기 완료를 통보합니다. VcRevocationProtocol.confirmRevokeVc(P220 Step 5)가 TAS txId로 호출합니다. 프로토콜은 반환 객체를 사용하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `CompleteRevokeApiRequest` | POST /issuer/api/v1/complete-revoke 요청 본문 | M | 7장 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `CompleteRevokeApiResponse` | POST /issuer/api/v1/complete-revoke 응답 본문 | O | 필드 없음. SDK가 사용하지 않으므로 null 가능 |

#### Function Declaration
```java
CompleteRevokeApiResponse completeRevoke(CompleteRevokeApiRequest request) throws IssuerApiException
```

#### Function Usage
```java
CompleteRevokeApiResponse response = issuerClient.completeRevoke(request);
```
<br>

### 2.17.9 getIssuerServerUrl

#### Class Name
`IssuerApiClient`

#### Function Name
`getIssuerServerUrl`

#### Function Introduction
`Issuer 서버 기본 URL(예: "https://issuer.example.org")을 반환합니다. SDK 프로토콜은 이 메서드를 호출하지 않습니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | Issuer 서버 기본 URL | M | |

#### Function Declaration
```java
String getIssuerServerUrl()
```

#### Function Usage
```java
String issuerUrl = issuerClient.getIssuerServerUrl();
```
<br>

---

<div style="page-break-after: always;"></div>

# 3. APIs - Config

## 3.1 TaSdkConfig

TaSdkConfig는 TA SDK 초기화에 필요한 설정을 묶은 컨테이너입니다. WalletConfig, CryptoConfig, StorageConfig를 포함합니다. SDK 구성 요소가 TaSdkConfig를 직접 읽지는 않으므로 `getWalletConfig()`는 `FileWalletAdapter`에, `getStorageConfig()`는 `BlockchainAdapter`에 넘깁니다.

필드:

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| walletConfig | `WalletConfig` | 월렛 설정. `walletConfig(WalletConfig walletConfig)`로 설정하고 `getWalletConfig()`로 읽음. null이면 IllegalArgumentException | M |
| cryptoConfig | `CryptoConfig` | 암호 설정. `cryptoConfig(CryptoConfig cryptoConfig)`로 설정하고 `getCryptoConfig()`로 읽음. null이면 `new CryptoConfig()`(기본값) 사용 | O |
| storageConfig | `StorageConfig` | 스토리지 설정. `storageConfig(StorageConfig storageConfig)`로 설정하고 `getStorageConfig()`로 읽음. null이면 IllegalArgumentException | M |

### 3.1.1 builder

#### Class Name
`TaSdkConfig`

#### Function Name
`builder`

#### Function Introduction
`TaSdkConfig 빌더를 생성합니다. Builder.build()는 3.1.2의 생성자를 호출하며, 검증에 실패하면 IllegalArgumentException을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `TaSdkConfig.Builder` | TaSdkConfig 빌더 인스턴스 | M | |

#### Function Declaration
```java
static TaSdkConfig.Builder builder()
```

#### Function Usage
```java
TaSdkConfig config = TaSdkConfig.builder()
    .walletConfig(walletConfig)
    .cryptoConfig(new CryptoConfig())
    .storageConfig(storageConfig)
    .build();
```
<br>

### 3.1.2 TaSdkConfig(WalletConfig, CryptoConfig, StorageConfig)

#### Class Name
`TaSdkConfig`

#### Function Name
`TaSdkConfig`

#### Function Introduction
`TA SDK 설정을 직접 생성합니다. walletConfig나 storageConfig가 null이면 IllegalArgumentException을 던지고, cryptoConfig가 null이면 new CryptoConfig()를 사용합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletConfig | `WalletConfig` | 월렛 설정 | M | |
| cryptoConfig | `CryptoConfig` | 암호 설정 | O | null이면 기본값 |
| storageConfig | `StorageConfig` | 스토리지 설정 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkConfig(WalletConfig walletConfig, CryptoConfig cryptoConfig, StorageConfig storageConfig)
```

#### Function Usage
```java
TaSdkConfig config = new TaSdkConfig(walletConfig, null, storageConfig); // CryptoConfig 기본값
```
<br>

---

## 3.2 WalletConfig

WalletConfig는 파일 기반 월렛의 경로와 비밀번호를 관리합니다. `FileWalletAdapter`에 넘겨 사용합니다.

필드:

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| filePath | `String` | 월렛 파일 경로. `filePath(String filePath)`로 설정하고 `getFilePath()`로 읽음. null이거나 공백뿐이면 IllegalArgumentException | M |
| password | `String` | 월렛 비밀번호. `password(String password)`로 설정하고 `getPassword()`로 읽음. null이거나 빈 문자열이면 IllegalArgumentException | M |

### 3.2.1 builder

#### Class Name
`WalletConfig`

#### Function Name
`builder`

#### Function Introduction
`WalletConfig 빌더를 생성합니다. Builder.build()는 3.2.2의 생성자를 호출하며, 검증에 실패하면 IllegalArgumentException을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `WalletConfig.Builder` | WalletConfig 빌더 인스턴스 | M | |

#### Function Declaration
```java
static WalletConfig.Builder builder()
```

#### Function Usage
```java
WalletConfig config = WalletConfig.builder()
    .filePath("/path/to/wallet.dat")
    .password("wallet-password")
    .build();
```
<br>

### 3.2.2 WalletConfig(String, String)

#### Class Name
`WalletConfig`

#### Function Name
`WalletConfig`

#### Function Introduction
`월렛 설정을 직접 생성합니다. filePath가 null이거나 공백뿐이면, 또는 password가 null이거나 빈 문자열이면 IllegalArgumentException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| filePath | `String` | 월렛 파일 경로 | M | |
| password | `String` | 월렛 비밀번호 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
WalletConfig(String filePath, String password)
```

#### Function Usage
```java
WalletConfig config = new WalletConfig("/path/to/wallet.dat", "wallet-password");
```
<br>

---

## 3.3 CryptoConfig

CryptoConfig는 서명 알고리즘, 해시 알고리즘, 키 크기를 설정합니다. 기본값으로 사용 가능합니다. 값은 비어 있지 않은지, 양수인지만 검사하며 지원 알고리즘인지는 검사하지 않습니다. 이 SDK의 구성 요소는 CryptoConfig를 읽지 않으며, 월렛 키는 항상 secp256r1로 생성됩니다.

필드:

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| signatureAlgorithm | `String` | 서명 알고리즘. 기본값 `"Secp256r1"`(`DEFAULT_SIGNATURE_ALGORITHM`). `signatureAlgorithm(String signatureAlgorithm)`로 설정하고 `getSignatureAlgorithm()`으로 읽음. null이거나 공백뿐이면 IllegalArgumentException | O |
| hashAlgorithm | `String` | 해시 알고리즘. 기본값 `"SHA-256"`(`DEFAULT_HASH_ALGORITHM`). `hashAlgorithm(String hashAlgorithm)`으로 설정하고 `getHashAlgorithm()`으로 읽음. null이거나 공백뿐이면 IllegalArgumentException | O |
| keySize | `int` | 키 크기(bit). 기본값 `256`(`DEFAULT_KEY_SIZE`). `keySize(int keySize)`로 설정하고 `getKeySize()`로 읽음. 양수가 아니면 IllegalArgumentException | O |

### 3.3.1 builder

#### Class Name
`CryptoConfig`

#### Function Name
`builder`

#### Function Introduction
`필드가 기본값으로 채워진 CryptoConfig 빌더를 생성합니다. Builder.build()는 3.3.3의 생성자를 호출하며, 검증에 실패하면 IllegalArgumentException을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `CryptoConfig.Builder` | CryptoConfig 빌더 인스턴스 | M | |

#### Function Declaration
```java
static CryptoConfig.Builder builder()
```

#### Function Usage
```java
// 기본값 사용 (Secp256r1, SHA-256, 256bit)
CryptoConfig config = new CryptoConfig();

// 커스텀 설정
CryptoConfig custom = CryptoConfig.builder()
    .signatureAlgorithm("Secp256r1")
    .hashAlgorithm("SHA-256")
    .keySize(256)
    .build();
```
<br>

### 3.3.2 CryptoConfig()

#### Class Name
`CryptoConfig`

#### Function Name
`CryptoConfig`

#### Function Introduction
`기본값("Secp256r1", "SHA-256", 256)으로 암호 설정을 생성합니다.`

#### Input Parameters
n/a

#### Output Parameters
n/a

#### Function Declaration
```java
CryptoConfig()
```

#### Function Usage
```java
CryptoConfig config = new CryptoConfig();
```
<br>

### 3.3.3 CryptoConfig(String, String, int)

#### Class Name
`CryptoConfig`

#### Function Name
`CryptoConfig`

#### Function Introduction
`지정한 값으로 암호 설정을 생성합니다. signatureAlgorithm이나 hashAlgorithm이 null이거나 공백뿐이면, 또는 keySize가 양수가 아니면 IllegalArgumentException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| signatureAlgorithm | `String` | 서명 알고리즘 (예: "Secp256r1") | M | |
| hashAlgorithm | `String` | 해시 알고리즘 (예: "SHA-256") | M | |
| keySize | `int` | 키 크기(bit) | M | 양수 |

#### Output Parameters
n/a

#### Function Declaration
```java
CryptoConfig(String signatureAlgorithm, String hashAlgorithm, int keySize)
```

#### Function Usage
```java
CryptoConfig config = new CryptoConfig("Secp256r1", "SHA-256", 256);
```
<br>

---

## 3.4 StorageConfig

StorageConfig는 DID Document와 VC 메타데이터를 저장할 스토리지 타입(블록체인 또는 LSS)을 설정합니다.

필드:

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| storageType | `StorageConfig.StorageType` | 팩토리 메서드가 정하는 `BLOCKCHAIN` 또는 `LSS`. `getStorageType()`으로 읽음 | M |
| blockChainConfig | `BlockChainConfig` | `forBlockchain`일 때만 설정되고 그 외에는 null. `getBlockChainConfig()`로 읽음 | O |
| lssConfig | `LssConfig` | `forLss`일 때만 설정되고 그 외에는 null. `getLssConfig()`로 읽음 | O |

### 3.4.1 forBlockchain

#### Class Name
`StorageConfig`

#### Function Name
`forBlockchain`

#### Function Introduction
`블록체인 스토리지 설정을 생성합니다. 생성자가 private이므로 이 메서드나 forLss를 사용합니다. blockChainConfig가 null이면 IllegalArgumentException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| blockChainConfig | `BlockChainConfig` | 블록체인 연결 설정 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `StorageConfig` | 블록체인 스토리지 설정 인스턴스 | M | |

#### Function Declaration
```java
static StorageConfig forBlockchain(BlockChainConfig blockChainConfig)
```

#### Function Usage
```java
BlockChainConfig bcConfig = new BlockChainConfig(
    "/path/to/blockchain.properties",
    RoleType.TAS,
    BlockChainConfig.BlockChainType.FABRIC
);
StorageConfig storageConfig = StorageConfig.forBlockchain(bcConfig);
```
<br>

### 3.4.2 forLss

#### Class Name
`StorageConfig`

#### Function Name
`forLss`

#### Function Introduction
`LSS(Ledger Service Server) 스토리지 설정을 생성합니다. lssConfig가 null이면 IllegalArgumentException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| lssConfig | `LssConfig` | LSS 연결 설정 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `StorageConfig` | LSS 스토리지 설정 인스턴스 | M | |

#### Function Declaration
```java
static StorageConfig forLss(LssConfig lssConfig)
```

#### Function Usage
```java
LssConfig lssConfig = new LssConfig("https://lss.example.com", "api-key");
StorageConfig storageConfig = StorageConfig.forLss(lssConfig);
```
<br>

### 3.4.3 isBlockchain / isLss

#### Class Name
`StorageConfig`

#### Function Name
`isBlockchain`, `isLss`

#### Function Introduction
`forBlockchain 또는 forLss로 정해진 스토리지 타입을 확인합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | 스토리지 타입 일치 여부 | M | |

#### Function Declaration
```java
boolean isBlockchain()
boolean isLss()
```
<br>

---

<div style="page-break-after: always;"></div>

# 4. Enumerator

## 4.1 OfferType

P142 DID Document 복구 프로토콜의 Offer 전송 방식(푸시 또는 이메일)을 정의합니다. 현재 이 enum을 인자나 반환값으로 쓰는 SDK 메서드는 없으며, `ProposeRestoreDidDocRequest`에는 `offerId`만 있습니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | 설명 |
|---------|------|
| `PUSH` | 푸시 알림으로 Offer를 전송합니다 |
| `EMAIL` | 이메일로 Offer를 전송합니다 |

**사용 예시**:
```java
OfferType offerType = OfferType.valueOf("PUSH");
```
<br>

## 4.2 ServerTokenPurpose

서버 토큰의 사용 목적을 정의합니다. 숫자 purpose 값으로 구분합니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | purpose 값 | 설명 |
|---------|-----------|------|
| `CREATE_DID` | 5 | DID 생성 |
| `UPDATE_DID` | 6 | DID 갱신 |
| `RESTORE_DID` | 7 | DID 복구 |
| `ISSUE_VC` | 8 | VC 발급 |
| `REVOKE_VC` | 9 | VC 폐기 |
| `PRESENT_VP` | 10 | VP 제출 |
| `CREATE_DID_AND_ISSUE_VC` | 13 | DID 생성 + VC 발급 통합 |

**메서드**:
- `Integer getPurpose()` — 숫자 purpose 값 반환 (`@JsonValue`로 JSON에 이 숫자로 직렬화됨)
<br>

## 4.3 TasStatus

TAS의 등록 상태를 나타냅니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | 설명 |
|---------|------|
| `PENDING` | TAS가 설정되었지만 아직 등록되지 않은 상태 |
| `COMPLETED` | TAS 등록이 완료된 상태 |

**상태 흐름**: `PENDING` → (enrollTas 성공) → `COMPLETED`. SDK는 이 값을 바꾸지 않으며, `enrollTas`가 성공하면 서버가 갱신합니다.
<br>

## 4.4 UserStatus

사용자의 현재 상태를 나타냅니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | 설명 |
|---------|------|
| `ACTIVATED` | 정상 활성화된 사용자 |
| `DEACTIVATED` | 일시적으로 비활성화된 사용자 (복구 가능) |
| `REVOKED` | 영구적으로 폐기된 사용자 |
| `TERMINATED` | 탈퇴 처리된 사용자 |

**참고**: SDK는 사용자 상태를 검사하지 않습니다. P141(DID Doc 갱신)을 호출하기 전에 서버가 사용자가 `ACTIVATED`인지, P142(DID Doc 복구)를 호출하기 전에 `DEACTIVATED`인지 확인합니다.
<br>

## 4.5 StorageConfig.StorageType

`StorageConfig`의 스토리지 타입입니다. `StorageConfig.forBlockchain` 또는 `StorageConfig.forLss`가 정합니다.

**패키지**: `org.omnione.did.tas.config` (`StorageConfig`의 중첩 enum)

| Enum 값 | 설명 |
|---------|------|
| `BLOCKCHAIN` | 블록체인 스토리지 (Hyperledger Fabric 또는 EVM 호환) |
| `LSS` | LSS(Ledger Service Server) 스토리지 |

<br>

---

<div style="page-break-after: always;"></div>

# 5. APIs - Exceptions

## 5.1 TaSdkException

`TaSdkException`은 `RuntimeException`을 상속하므로 unchecked 예외입니다. 절에 따로 적지 않은 한 SDK 메서드는 실패를 이 예외로 알립니다. `getErrorCode()`는 에러 코드를, `getMessage()`는 커스텀 메시지가 있으면 그 메시지를, 없으면 에러 코드의 메시지를 반환합니다.

**패키지**: `org.omnione.did.tas.exception`

### 5.1.1 TaSdkException(TaSdkErrorCode)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`TaSdkErrorCode로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode)
```

#### Function Usage
```java
throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID);
```
<br>

### 5.1.2 TaSdkException(TaSdkErrorCode, Throwable)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`TaSdkErrorCode와 원인 예외로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| cause | `Throwable` | 원인 예외 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, Throwable cause)
```

#### Function Usage
```java
try {
    // ...
} catch (Exception e) {
    throw new TaSdkException(TaSdkErrorCode.SDK_SIGNATURE_VERIFICATION_FAILED, e);
}
```
<br>

### 5.1.3 TaSdkException(TaSdkErrorCode, String)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`TaSdkErrorCode와 커스텀 메시지로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| message | `String` | 추가 메시지 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, String message)
```
<br>

### 5.1.4 TaSdkException(TaSdkErrorCode, String, Throwable)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`TaSdkErrorCode, 커스텀 메시지, 원인 예외로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| message | `String` | 추가 메시지 | M | |
| cause | `Throwable` | 원인 예외 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, String message, Throwable cause)
```
<br>

### 5.1.5 getErrorCode

#### Class Name
`TaSdkException`

#### Function Name
`getErrorCode`

#### Function Introduction
`예외의 TaSdkErrorCode를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `TaSdkErrorCode` | 에러 코드 | M | |

#### Function Declaration
```java
TaSdkErrorCode getErrorCode()
```
<br>

### 5.1.6 TaSdkException(TaSdkErrorCode, Object[])

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`에러 코드와 메시지 인자로 SDK 예외를 생성합니다. 예외 메시지는 에러 코드의 메시지입니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| args | `Object[]` | 메시지 인자 | O | getArgs(), argsToString()용으로 보관 |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, Object[] args)
```

#### Function Usage
```java
throw new TaSdkException(TaSdkErrorCode.SDK_USER_NOT_FOUND, new Object[]{"did:omn:user"});
```
<br>

### 5.1.7 TaSdkException(TaSdkErrorCode, Object[], Throwable)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`에러 코드, 메시지 인자, 원인 예외로 SDK 예외를 생성합니다. 예외 메시지는 에러 코드의 메시지입니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| args | `Object[]` | 메시지 인자 | O | getArgs(), argsToString()용으로 보관 |
| cause | `Throwable` | 원인 예외 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, Object[] args, Throwable cause)
```

#### Function Usage
```java
throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND, new Object[]{did}, e);
```
<br>

### 5.1.8 TaSdkException(TaSdkErrorCode, Object[], String)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`에러 코드, 메시지 인자, 커스텀 메시지로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| args | `Object[]` | 메시지 인자 | O | getArgs(), argsToString()용으로 보관 |
| message | `String` | 예외 메시지 | M | 에러 코드 메시지 대신 사용 |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, Object[] args, String message)
```

#### Function Usage
```java
throw new TaSdkException(TaSdkErrorCode.SDK_ENTITY_NOT_FOUND, new Object[]{did}, "Entity not found: " + did);
```
<br>

### 5.1.9 TaSdkException(TaSdkErrorCode, Object[], String, Throwable)

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`에러 코드, 메시지 인자, 커스텀 메시지, 원인 예외로 SDK 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | 에러 코드 | M | |
| args | `Object[]` | 메시지 인자 | O | getArgs(), argsToString()용으로 보관 |
| message | `String` | 예외 메시지 | M | 에러 코드 메시지 대신 사용 |
| cause | `Throwable` | 원인 예외 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkException(TaSdkErrorCode errorCode, Object[] args, String message, Throwable cause)
```

#### Function Usage
```java
throw new TaSdkException(TaSdkErrorCode.SDK_KEY_NOT_FOUND, new Object[]{keyId}, "Key not found: " + keyId, e);
```
<br>

### 5.1.10 getArgs

#### Class Name
`TaSdkException`

#### Function Name
`getArgs`

#### Function Introduction
`메시지 인자를 반환합니다. 인자 없이 생성된 예외는 null을 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Object[]` | 메시지 인자 | O | 없으면 null |

#### Function Declaration
```java
Object[] getArgs()
```

#### Function Usage
```java
Object[] args = e.getArgs();
```
<br>

### 5.1.11 argsToString

#### Class Name
`TaSdkException`

#### Function Name
`argsToString`

#### Function Introduction
`메시지 인자를 ", "로 이어 붙인 문자열을 반환합니다. 인자가 없으면 null을 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 이어 붙인 인자 | O | args가 null이거나 비어 있으면 null |

#### Function Declaration
```java
String argsToString()
```

#### Function Usage
```java
log.error("{} [{}]", e.getErrorCode().getCode(), e.argsToString());
```
<br>

---

## 5.2 TaSdkErrorCode

TA SDK의 모든 에러 코드를 정의합니다. 에러 코드 형식: `SSDKTRA{category}{number}`

**패키지**: `org.omnione.did.tas.exception`

### getCode / getMessage / getHttpStatus

#### Function Declaration
```java
String getCode()       // 에러 코드 문자열 반환 (예: "SSDKTRA001001")
String getMessage()    // 에러 메시지 반환
int getHttpStatus()    // HTTP 상태 코드 반환
```

#### Function Usage
```java
TaSdkErrorCode errorCode = TaSdkErrorCode.SDK_INVALID_DID;
String code = errorCode.getCode();        // "SSDKTRA001001"
String message = errorCode.getMessage();  // "Invalid DID format."
int status = errorCode.getHttpStatus();   // 400
```
<br>

### getMessageByCode

#### Function Declaration
```java
static String getMessageByCode(String code)
```

#### Function Usage
```java
String message = TaSdkErrorCode.getMessageByCode("SSDKTRA001001");
// → "Invalid DID format."
String unknown = TaSdkErrorCode.getMessageByCode("SSDKTRA999999");
// → "Unknown error code: SSDKTRA999999"
```
<br>

### 에러 코드 목록

#### SDK 공통 검증 오류 (SSDKTRA001xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_INVALID_DID` | SSDKTRA001001 | 400 | DID 형식이 올바르지 않습니다 |
| `SDK_INVALID_PROOF` | SSDKTRA001002 | 400 | Proof가 올바르지 않습니다 |
| `SDK_INVALID_SIGNATURE` | SSDKTRA001003 | 400 | 서명이 올바르지 않습니다 |
| `SDK_INVALID_MULTIBASE` | SSDKTRA001004 | 400 | Multibase 인코딩이 올바르지 않습니다 |
| `SDK_FIELD_REQUIRED` | SSDKTRA001005 | 400 | 필수 필드가 누락되었습니다 |
| `SDK_INVALID_TRANSACTION` | SSDKTRA001006 | 400 | 트랜잭션이 올바르지 않습니다 |
| `SDK_INVALID_PARAMETER` | SSDKTRA001007 | 400 | 파라미터가 올바르지 않습니다 |
| `SDK_INVALID_CONFIGURATION` | SSDKTRA001008 | 500 | 설정이 올바르지 않습니다 |

#### SDK 프로토콜 오류 (SSDKTRA002xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_INVALID_PROTOCOL_STEP` | SSDKTRA002001 | 400 | 프로토콜 단계가 올바르지 않습니다 |
| `SDK_INVALID_TRANSACTION_STATUS` | SSDKTRA002002 | 400 | 트랜잭션 상태가 올바르지 않습니다 |
| `SDK_INVALID_SUB_TRANSACTION_STATUS` | SSDKTRA002003 | 400 | 서브 트랜잭션 상태가 올바르지 않습니다 |
| `SDK_TRANSACTION_EXPIRED` | SSDKTRA002004 | 400 | 트랜잭션이 만료되었습니다 |
| `SDK_TRANSACTION_NOT_FOUND` | SSDKTRA002005 | 404 | 트랜잭션을 찾을 수 없습니다 |

#### SDK 암호화 오류 (SSDKTRA003xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_SIGNATURE_GENERATION_FAILED` | SSDKTRA003001 | 500 | 서명 생성에 실패했습니다 |
| `SDK_SIGNATURE_VERIFICATION_FAILED` | SSDKTRA003002 | 400 | 서명 검증에 실패했습니다 |
| `SDK_ECDH_KEY_AGREEMENT_FAILED` | SSDKTRA003003 | 500 | ECDH 키 협의에 실패했습니다 |
| `SDK_ENCRYPTION_FAILED` | SSDKTRA003004 | 500 | 암호화에 실패했습니다 |
| `SDK_DECRYPTION_FAILED` | SSDKTRA003005 | 500 | 복호화에 실패했습니다 |
| `SDK_VC_ISSUANCE_FAILED` | SSDKTRA003006 | 500 | VC 발급에 실패했습니다 |

#### SDK 블록체인 오류 (SSDKTRA004xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_DID_DOCUMENT_NOT_FOUND` | SSDKTRA004001 | 404 | DID Document를 찾을 수 없습니다 |
| `SDK_DID_DOCUMENT_REGISTRATION_FAILED` | SSDKTRA004002 | 500 | DID Document 등록에 실패했습니다 |
| `SDK_DID_DOCUMENT_UPDATE_FAILED` | SSDKTRA004003 | 500 | DID Document 업데이트에 실패했습니다 |
| `SDK_VC_META_NOT_FOUND` | SSDKTRA004004 | 404 | VC 메타데이터를 찾을 수 없습니다 |
| `SDK_BLOCKCHAIN_CONNECTION_FAILED` | SSDKTRA004005 | 500 | 블록체인 연결에 실패했습니다 |
| `SDK_VC_REVOKED` | SSDKTRA004006 | 400 | VC가 폐기되었습니다 |

#### SDK 월렛 오류 (SSDKTRA005xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_WALLET_CONNECTION_FAILED` | SSDKTRA005001 | 500 | 월렛 연결에 실패했습니다 |
| `SDK_WALLET_NOT_CONNECTED` | SSDKTRA005002 | 500 | 월렛이 연결되어 있지 않습니다 |
| `SDK_WALLET_CREATION_FAILED` | SSDKTRA005003 | 500 | 월렛 생성에 실패했습니다 |
| `SDK_KEY_NOT_FOUND` | SSDKTRA005004 | 404 | 월렛에서 키를 찾을 수 없습니다 |
| `SDK_KEY_GENERATION_FAILED` | SSDKTRA005005 | 500 | 키 생성에 실패했습니다 |

#### SDK Provider 오류 (SSDKTRA006xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_USER_NOT_FOUND` | SSDKTRA006001 | 404 | 사용자를 찾을 수 없습니다 |
| `SDK_ENTITY_NOT_FOUND` | SSDKTRA006002 | 404 | Entity를 찾을 수 없습니다 |
| `SDK_WALLET_INFO_NOT_FOUND` | SSDKTRA006003 | 404 | 월렛 정보를 찾을 수 없습니다 |
| `SDK_ECDH_SESSION_NOT_FOUND` | SSDKTRA006004 | 404 | ECDH 세션을 찾을 수 없습니다 |
| `SDK_TOKEN_NOT_FOUND` | SSDKTRA006005 | 404 | 토큰을 찾을 수 없습니다 |
| `SDK_VC_SCHEMA_NOT_FOUND` | SSDKTRA006006 | 404 | VC 스키마를 찾을 수 없습니다 |

#### P110 TAS 등록 프로토콜 오류 (SSDKTRA011xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `PASSWORD_MISMATCH` | SSDKTRA011001 | 400 | 비밀번호가 일치하지 않습니다 |
| `TAS_DID_DOCUMENT_NOT_FOUND` | SSDKTRA011003 | 404 | TAS DID Document를 찾을 수 없습니다 |
| `CERTIFICATE_VC_GENERATION_FAILED` | SSDKTRA011004 | 500 | Certificate VC 생성에 실패했습니다 |
| `CERTIFICATE_VC_SIGNING_FAILED` | SSDKTRA011005 | 500 | Certificate VC 서명에 실패했습니다 |
| `CERTIFICATE_VC_PUBLISHING_FAILED` | SSDKTRA011006 | 500 | Certificate VC 게시에 실패했습니다 |
| `VC_META_REGISTRATION_FAILED` | SSDKTRA011007 | 500 | VcMeta 블록체인 등록에 실패했습니다 |
| `TAS_ENROLLMENT_FAILED` | SSDKTRA011008 | 500 | TAS 등록에 실패했습니다 |
| `TAS_PASSWORD_NOT_CONFIGURED` | SSDKTRA011009 | 500 | TAS 비밀번호가 설정되지 않았습니다 |

#### P131 월렛 등록 프로토콜 오류 (SSDKTRA013xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `WALLET_PROVIDER_NOT_REGISTERED` | SSDKTRA013001 | 400 | Wallet Provider가 등록되지 않았습니다 |
| `ENTITY_NOT_COMPLETED` | SSDKTRA013002 | 400 | Entity가 COMPLETED 상태가 아닙니다 |
| `CERTIFICATE_VC_NOT_FOUND` | SSDKTRA013003 | 404 | Certificate VC를 찾을 수 없습니다 |
| `PROVIDER_DID_MISMATCH` | SSDKTRA013004 | 400 | Provider DID와 Certificate VC subject DID가 일치하지 않습니다 |
| `INVALID_CERTIFICATE_VC_ISSUER` | SSDKTRA013005 | 400 | Certificate VC의 Issuer가 유효하지 않거나 만료되었습니다 |
| `ATTESTATION_VERIFICATION_FAILED` | SSDKTRA013006 | 400 | Attestation 서명 검증에 실패했습니다 |
| `WALLET_ID_ALREADY_EXISTS` | SSDKTRA013007 | 400 | WalletId가 이미 존재합니다 |
| `DID_DOCUMENT_KEY_VERIFICATION_FAILED` | SSDKTRA013008 | 400 | DID Document 키 서명 검증에 실패했습니다 |
| `INVOKED_DID_DOC_CREATION_FAILED` | SSDKTRA013009 | 500 | InvokedDidDoc 생성에 실패했습니다 |
| `DID_DOCUMENT_REGISTRATION_FAILED` | SSDKTRA013010 | 500 | DID Document 블록체인 등록에 실패했습니다 |

#### P132 사용자 등록 프로토콜 오류 (SSDKTRA032xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `WALLET_INFO_PROOF_VERIFICATION_FAILED` | SSDKTRA032001 | 400 | WalletInfo proof 서명 검증에 실패했습니다 |
| `CA_APP_INFO_PROOF_VERIFICATION_FAILED` | SSDKTRA032002 | 400 | CaAppInfo proof 서명 검증에 실패했습니다 |
| `SERVER_TOKEN_DATA_SIGNING_FAILED` | SSDKTRA032003 | 500 | ServerTokenData 서명에 실패했습니다 |
| `SERVER_TOKEN_ENCRYPTION_FAILED` | SSDKTRA032004 | 500 | 서버 토큰 암호화에 실패했습니다 |
| `SERVER_TOKEN_INVALID` | SSDKTRA032005 | 400 | 서버 토큰이 유효하지 않거나 만료되었습니다 |
| `KYC_DATA_RETRIEVAL_FAILED` | SSDKTRA032006 | 500 | KYC 데이터 조회에 실패했습니다 |
| `KYC_TOKEN_AND_TXID_BOTH_MISSING` | SSDKTRA032007 | 400 | kycToken 또는 kycTxId 중 하나가 필요합니다 |
| `USER_DID_ALREADY_EXISTS` | SSDKTRA032008 | 400 | 이미 등록된 사용자 DID입니다 |
| `USER_PII_ALREADY_EXISTS` | SSDKTRA032009 | 400 | 동일한 PII의 사용자가 이미 등록되어 있습니다 |
| `SIGNED_DID_DOC_WALLET_PROOF_FAILED` | SSDKTRA032010 | 400 | SignedDidDoc 월렛 서명 검증에 실패했습니다 |
| `OWNER_DID_DOC_KEY_PROOF_FAILED` | SSDKTRA032011 | 400 | ownerDidDoc 키 서명 검증에 실패했습니다 |
| `USER_VC_GENERATION_FAILED` | SSDKTRA032012 | 500 | User VC 생성에 실패했습니다 |
| `USER_VC_META_REGISTRATION_FAILED` | SSDKTRA032013 | 500 | User VC 메타데이터 블록체인 등록에 실패했습니다 |

#### P141 DID Document 갱신 프로토콜 오류 (SSDKTRA017xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `USER_NOT_FOUND` | SSDKTRA017002 | 404 | 사용자를 찾을 수 없습니다 |
| `USER_NOT_ACTIVATED` | SSDKTRA017003 | 400 | 사용자 상태가 ACTIVATED가 아닙니다 |
| `PROPOSE_UPDATE_DIDDOC_FAILED` | SSDKTRA017011 | 500 | DID Document 갱신 제안 처리에 실패했습니다 |
| `REQUEST_UPDATE_DIDDOC_FAILED` | SSDKTRA017012 | 500 | DID Document 갱신 요청 처리에 실패했습니다 |
| `CONFIRM_UPDATE_DIDDOC_FAILED` | SSDKTRA017013 | 500 | DID Document 갱신 확인 처리에 실패했습니다 |
| `AUTH_NONCE_MISMATCH` | SSDKTRA016520 | 400 | authNonce가 일치하지 않습니다 |
| `DID_DOC_KEY_VERIFICATION_FAILED` | SSDKTRA016516 | 400 | DID Document 키 서명 검증에 실패했습니다 |
| `DID_DOCUMENT_PARSING_FAILED` | SSDKTRA012006 | 400 | DID Document 파싱에 실패했습니다 |
| `INVALID_DID_DOC_VERSION` | SSDKTRA012009 | 400 | DID Document 버전이 올바르지 않습니다 |
| `INVALID_DID_DOC_UPDATED` | SSDKTRA018006 | 400 | DID Document updated 시각이 올바르지 않습니다 |
| `INVALID_DID_DOC_CONTEXT` | SSDKTRA018007 | 400 | DID Document @context가 올바르지 않습니다 |
| `INVALID_DID_DOC_ID` | SSDKTRA018008 | 400 | DID Document id가 올바르지 않습니다 |
| `INVALID_DID_DOC_CONTROLLER` | SSDKTRA018009 | 400 | DID Document controller가 올바르지 않습니다 |
| `INVALID_DID_DOC_CREATED` | SSDKTRA018014 | 400 | DID Document created 시각이 올바르지 않습니다 |
| `INVALID_DID_DOC_DEACTIVATED` | SSDKTRA018015 | 400 | DID Document deactivated 상태가 올바르지 않습니다 |
| `NONCE_GENERATION_FAILED` | SSDKTRA017020 | 500 | Nonce 생성에 실패했습니다 |
| `MULTIBASE_DECODE_FAILED` | SSDKTRA017021 | 400 | Multibase 디코딩에 실패했습니다 |
| `PROOF_MISSING` | SSDKTRA017022 | 400 | Proof가 누락되었습니다 |
| `INVALID_VERIFICATION_METHOD` | SSDKTRA017023 | 400 | verificationMethod가 올바르지 않습니다 |
| `INVALID_PROOF_PURPOSE` | SSDKTRA017024 | 400 | proof purpose가 올바르지 않습니다 |
| `DID_MISMATCH` | SSDKTRA017025 | 400 | DID가 일치하지 않습니다 |
| `DID_DOCUMENT_NOT_FOUND` | SSDKTRA017026 | 404 | DID Document를 찾을 수 없습니다 |
| `PUBLIC_KEY_NOT_FOUND` | SSDKTRA017027 | 404 | 공개키를 찾을 수 없습니다 |

#### P142 DID Document 복구 프로토콜 오류 (SSDKTRA020xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `USER_NOT_FOUND_FOR_RESTORE` | SSDKTRA020002 | 404 | 복구할 사용자를 찾을 수 없습니다 |
| `USER_NOT_DEACTIVATED` | SSDKTRA020003 | 400 | 사용자 상태가 DEACTIVATED가 아닙니다 |
| `TRANSACTION_NOT_FOUND_FOR_RESTORE` | SSDKTRA020004 | 404 | 트랜잭션을 찾을 수 없습니다 |
| `OFFER_CREATION_FAILED` | SSDKTRA020005 | 500 | Offer 생성에 실패했습니다 |
| `PUSH_SEND_FAILED` | SSDKTRA020006 | 500 | 푸시 알림 전송에 실패했습니다 |
| `EMAIL_SEND_FAILED` | SSDKTRA020007 | 500 | 이메일 전송에 실패했습니다 |
| `OFFER_NOT_FOUND` | SSDKTRA020008 | 404 | Offer를 찾을 수 없습니다 |
| `OFFER_CODE_MISMATCH` | SSDKTRA020009 | 400 | Offer 코드가 일치하지 않습니다 |
| `OFFER_EXPIRED` | SSDKTRA020010 | 400 | Offer가 만료되었습니다 |
| `OFFER_ALREADY_USED` | SSDKTRA020011 | 400 | Offer가 이미 사용되었습니다 |
| `DEACTIVATED_FLAG_NOT_FALSE` | SSDKTRA020012 | 400 | deactivated 플래그가 false가 아닙니다 |
| `OFFER_TRANSACTION_MISMATCH` | SSDKTRA020013 | 400 | Offer txId가 일치하지 않습니다 |
| `INVALID_OFFER_TYPE` | SSDKTRA020014 | 400 | Offer 타입이 올바르지 않습니다 |
| `OFFER_SEND_FAILED` | SSDKTRA020015 | 500 | Offer 전송에 실패했습니다 |
| `PROPOSE_RESTORE_DIDDOC_FAILED` | SSDKTRA020021 | 500 | DID Document 복구 제안 처리에 실패했습니다 |
| `REQUEST_ISSUE_OFFER_RESTORE_DIDDOC_FAILED` | SSDKTRA020022 | 500 | DID Document 복구 Offer 발급 요청 처리에 실패했습니다 |
| `VERIFY_OFFER_RESTORE_DIDDOC_FAILED` | SSDKTRA020023 | 500 | DID Document 복구 Offer 검증에 실패했습니다 |
| `REQUEST_RESTORE_DIDDOC_FAILED` | SSDKTRA020024 | 500 | DID Document 복구 요청 처리에 실패했습니다 |
| `CONFIRM_RESTORE_DIDDOC_FAILED` | SSDKTRA020025 | 500 | DID Document 복구 확인 처리에 실패했습니다 |

#### P210 VC 발급 프로토콜 오류 (SSDKTRA021xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `VC_ISSUANCE_TRANSACTION_NOT_FOUND` | SSDKTRA021001 | 404 | 트랜잭션을 찾을 수 없습니다 |
| `VC_ISSUANCE_TRANSACTION_INVALID_STATE` | SSDKTRA021002 | 400 | 트랜잭션 상태가 유효하지 않습니다 |
| `VC_ISSUANCE_TRANSACTION_EXPIRED` | SSDKTRA021003 | 400 | 트랜잭션이 만료되었습니다 |
| `VC_ISSUANCE_INVALID_PREVIOUS_STEP` | SSDKTRA021004 | 400 | 이전 단계가 유효하지 않습니다 |
| `VC_PLAN_NOT_FOUND` | SSDKTRA021005 | 404 | VC Plan을 찾을 수 없습니다 |
| `VC_PLAN_INVALID` | SSDKTRA021006 | 400 | VC Plan이 유효하지 않습니다 |
| `ISSUER_NOT_FOUND` | SSDKTRA021007 | 404 | Issuer를 찾을 수 없습니다 |
| `ISSUER_COMMUNICATION_FAILED` | SSDKTRA021009 | 500 | Issuer와의 통신에 실패했습니다 |
| `SERVER_TOKEN_VALIDATION_FAILED` | SSDKTRA021015 | 400 | 서버 토큰 검증에 실패했습니다 |
| `ISSUE_PROFILE_RETRIEVAL_FAILED` | SSDKTRA021016 | 500 | Issue Profile 조회에 실패했습니다 |
| `VC_SCHEMA_VALIDATION_FAILED` | SSDKTRA021017 | 400 | VC 스키마 검증에 실패했습니다 |
| `VP_DECRYPTION_FAILED` | SSDKTRA021018 | 500 | VP 복호화에 실패했습니다 |
| `VP_SIGNATURE_VERIFICATION_FAILED` | SSDKTRA021019 | 400 | VP 서명 검증에 실패했습니다 |
| `VC_ISSUANCE_FAILED` | SSDKTRA021020 | 500 | VC 발급에 실패했습니다 |
| `VC_ENCRYPTION_FAILED` | SSDKTRA021021 | 500 | VC 암호화에 실패했습니다 |
| `VC_ID_MISMATCH` | SSDKTRA021022 | 400 | VC ID가 일치하지 않습니다 |
| `VC_METADATA_REGISTRATION_FAILED` | SSDKTRA021023 | 500 | VC 메타데이터 등록에 실패했습니다 |
| `HOLDER_NOT_FOUND` | SSDKTRA021024 | 404 | Holder를 찾을 수 없습니다 |
| `PUSH_TOKEN_NOT_FOUND` | SSDKTRA021025 | 404 | Push Token을 찾을 수 없습니다 |

#### P220 VC 폐기 프로토콜 오류 (SSDKTRA022xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `VC_ALREADY_REVOKED` | SSDKTRA022001 | 400 | VC가 이미 폐기되었습니다 |
| `VC_EXPIRED` | SSDKTRA022002 | 400 | VC가 만료되었습니다 |
| `VC_STATUS_INVALID` | SSDKTRA022003 | 400 | VC 상태가 유효하지 않습니다 |
| `VC_META_FETCH_FAILED` | SSDKTRA022004 | 500 | 블록체인에서 VC 메타데이터 조회에 실패했습니다 |
| `ISSUER_NONCE_MISMATCH` | SSDKTRA022005 | 400 | Issuer Nonce가 일치하지 않습니다 |
| `SIGNATURE_INVALID` | SSDKTRA022006 | 400 | 서명이 유효하지 않습니다 |
| `PROPOSE_REVOKE_VC_FAILED` | SSDKTRA022007 | 500 | VC 폐기 제안 처리에 실패했습니다 |
| `REQUEST_REVOKE_VC_FAILED` | SSDKTRA022008 | 500 | VC 폐기 요청 처리에 실패했습니다 |
| `CONFIRM_REVOKE_VC_FAILED` | SSDKTRA022009 | 500 | VC 폐기 확인 처리에 실패했습니다 |

#### SDK 일반 오류 (SSDKTRA009xxx)

| 에러 코드 | 코드값 | HTTP | 설명 |
|-----------|--------|------|------|
| `SDK_NOT_INITIALIZED` | SSDKTRA009001 | 500 | SDK가 초기화되지 않았습니다 |
| `SDK_UNKNOWN_ERROR` | SSDKTRA009999 | 500 | 알 수 없는 SDK 오류가 발생했습니다 |

<br>

---

## 5.3 IssuerApiException

`IssuerApiException`은 Issuer 호출이 실패하거나 Issuer가 오류를 반환할 때 `IssuerApiClient` 구현체가 던지는 checked 예외(`extends Exception`)입니다. 프로토콜은 이를 `TaSdkException`으로 바꿉니다. P210에서는 ISSUER_COMMUNICATION_FAILED, P220에서는 PROPOSE_REVOKE_VC_FAILED, REQUEST_REVOKE_VC_FAILED, CONFIRM_REVOKE_VC_FAILED입니다.

**패키지**: `org.omnione.did.tas.exception`

### 5.3.1 IssuerApiException(String)

#### Class Name
`IssuerApiException`

#### Function Name
`IssuerApiException`

#### Function Introduction
`상세 메시지로 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| message | `String` | 상세 메시지 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
IssuerApiException(String message)
```

#### Function Usage
```java
throw new IssuerApiException("Issuer returned HTTP 500");
```
<br>

### 5.3.2 IssuerApiException(String, Throwable)

#### Class Name
`IssuerApiException`

#### Function Name
`IssuerApiException`

#### Function Introduction
`상세 메시지와 원인 예외로 예외를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| message | `String` | 상세 메시지 | M | |
| cause | `Throwable` | 원인 예외 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
IssuerApiException(String message, Throwable cause)
```

#### Function Usage
```java
throw new IssuerApiException("Issuer communication failed", e);
```
<br>

---

<div style="page-break-after: always;"></div>

# 6. APIs - Core Components

Core 구성 요소는 월렛 접근, DID Document, 블록체인 접근, ECDH, 서명, VC 발급을 위한 구체 클래스입니다. `TasEnrollmentProtocolImpl`과 `EntityEnrollmentProtocolImpl`이 생성자로 받는 `VcBuilder`를 제외하면 1장의 프로토콜 구현체는 이 클래스들을 쓰지 않고 Provider를 통해 월렛, 스토리지, DID Document에 접근합니다. 이 클래스들은 `WalletProvider`, `StorageProvider` 같은 Provider를 구현할 때 쓰거나 직접 호출합니다.

## 6.1 FileWalletAdapter

`FileWalletAdapter`는 TAS 키를 담는 파일 기반 월렛입니다. did-common-sdk-server의 `BaseWalletServiceImpl`을 상속하며, `createWallet`, `loadWallet`, `generateKey`, `sign`은 상위 클래스가 제공합니다. `createWallet`으로 새 월렛을 만들면 ID가 `assert`, `auth`, `keyagree`, `invoke`인 secp256r1 키 4개가 생성됩니다. `KeyManager`, `DidDocumentBuilder`, `InvokedDidDocHandler`, `TaEcdhService`, `TaSignatureService`, `TaVcService`는 월렛을 열거나 만든 어댑터를 받습니다.

**패키지**: `org.omnione.did.tas.core`

### 6.1.1 FileWalletAdapter(WalletConfig)

#### Class Name
`FileWalletAdapter`

#### Function Name
`FileWalletAdapter`

#### Function Introduction
`지정한 설정으로 월렛 어댑터를 생성합니다. initialize(), loadWallet(), createWallet()을 호출하기 전에는 월렛 파일을 열지 않습니다. 인자의 null 여부를 검사하지 않으며, null이면 initialize()에서 NullPointerException이 발생합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletConfig | `WalletConfig` | 월렛 파일 경로와 비밀번호 | M | 3.2 참조 |

#### Output Parameters
n/a

#### Function Declaration
```java
FileWalletAdapter(WalletConfig walletConfig)
```

#### Function Usage
```java
WalletConfig walletConfig = WalletConfig.builder()
    .filePath("/path/to/wallet.dat")
    .password("wallet-password")
    .build();
FileWalletAdapter walletAdapter = new FileWalletAdapter(walletConfig);
```
<br>

### 6.1.2 initialize

#### Class Name
`FileWalletAdapter`

#### Function Name
`initialize`

#### Function Introduction
`WalletConfig.getFilePath()의 기존 월렛 파일을 WalletConfig.getPassword()로 엽니다. 월렛을 새로 만들지는 않으므로 새 월렛은 createWallet으로 만듭니다. 파일을 열 수 없으면 TaSdkException(SDK_WALLET_CONNECTION_FAILED)을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
n/a

#### Function Declaration
```java
void initialize()
```

#### Function Usage
```java
walletAdapter.initialize();
```
<br>

### 6.1.3 createWallet

#### Class Name
`FileWalletAdapter`

#### Function Name
`createWallet`

#### Function Introduction
`새 월렛 파일을 만들고 기본 TAS 키 4개(assert, auth, keyagree, invoke)를 secp256r1로 생성한 뒤, 어댑터가 새 월렛을 사용하게 합니다. BaseWalletServiceImpl에서 상속합니다. WalletConfig가 아니라 인자를 사용합니다. 생성이나 키 생성에 실패하면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletFilePath | `String` | 생성할 월렛 파일 경로 | M | |
| password | `String` | 월렛 비밀번호 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `WalletManagerInterface` | 생성된 월렛의 월렛 매니저 | M | |

#### Function Declaration
```java
WalletManagerInterface createWallet(String walletFilePath, String password) throws CommonSdkException
```

#### Function Usage
```java
walletAdapter.createWallet(walletConfig.getFilePath(), walletConfig.getPassword());
```
<br>

### 6.1.4 loadWallet

#### Class Name
`FileWalletAdapter`

#### Function Name
`loadWallet`

#### Function Introduction
`기존 월렛 파일을 열고 어댑터가 사용하게 합니다. BaseWalletServiceImpl에서 상속하며, initialize()가 WalletConfig 값으로 이 메서드를 호출합니다. 파일을 열 수 없으면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletFilePath | `String` | 월렛 파일 경로 | M | |
| password | `String` | 월렛 비밀번호 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `WalletManagerInterface` | 연 월렛의 월렛 매니저 | M | |

#### Function Declaration
```java
WalletManagerInterface loadWallet(String walletFilePath, String password) throws CommonSdkException
```

#### Function Usage
```java
walletAdapter.loadWallet("/path/to/wallet.dat", "wallet-password");
```
<br>

### 6.1.5 generateKey

#### Class Name
`FileWalletAdapter`

#### Function Name
`generateKey`

#### Function Introduction
`월렛에 지정한 ID로 키를 생성하고 그 keyId를 그대로 반환합니다. SECP256R1이면 secp256r1, null이나 그 밖의 값이면 secp256k1을 씁니다. BaseWalletServiceImpl에서 상속합니다. 열린 월렛이 없거나 생성에 실패하면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| keyId | `String` | 키 ID | M | |
| curveType | `EccCurveType` | 곡선 | O | null이면 secp256k1 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 인자로 받은 keyId | M | |

#### Function Declaration
```java
String generateKey(String keyId, EccCurveType curveType) throws CommonSdkException
```

#### Function Usage
```java
String keyId = walletAdapter.generateKey("assert", EccCurveType.SECP256R1);
```
<br>

### 6.1.6 sign

#### Class Name
`FileWalletAdapter`

#### Function Name
`sign`

#### Function Introduction
`data를 SHA-256으로 해시하고 keyId 키로 서명해 compact 서명을 반환합니다. BaseWalletServiceImpl에서 상속합니다. 열린 월렛이 없거나 키가 없거나 서명에 실패하면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| keyId | `String` | 키 ID (예: "assert") | M | |
| data | `byte[]` | 서명할 데이터 | M | 서명 전에 해시함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `byte[]` | compact 서명 | M | |

#### Function Declaration
```java
byte[] sign(String keyId, byte[] data) throws CommonSdkException
```

#### Function Usage
```java
byte[] signature = walletAdapter.sign("assert", message);
```
<br>

### 6.1.7 getWalletManager

#### Class Name
`FileWalletAdapter`

#### Function Name
`getWalletManager`

#### Function Introduction
`저수준 월렛 작업을 위한 WalletManagerInterface를 반환합니다. 월렛을 열거나 만들지 않았으면 TaSdkException(SDK_NOT_INITIALIZED)을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `WalletManagerInterface` | 월렛 매니저 | M | |

#### Function Declaration
```java
WalletManagerInterface getWalletManager()
```

#### Function Usage
```java
WalletManagerInterface walletManager = walletAdapter.getWalletManager();
```
<br>

### 6.1.8 getWalletConfig

#### Class Name
`FileWalletAdapter`

#### Function Name
`getWalletConfig`

#### Function Introduction
`생성자에 넘긴 WalletConfig를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `WalletConfig` | 월렛 설정 | O | null을 넘겼으면 null |

#### Function Declaration
```java
WalletConfig getWalletConfig()
```

#### Function Usage
```java
String walletPath = walletAdapter.getWalletConfig().getFilePath();
```
<br>

---

## 6.2 KeyManager

`KeyManager`는 `FileWalletAdapter`를 감싸 TAS 키 생성과 데이터 서명을 수행하며, `CommonSdkException`을 `TaSdkException`으로 바꿉니다. 사용자와 Entity의 키는 TA SDK가 관리하지 않습니다.

**패키지**: `org.omnione.did.tas.core`

### 6.2.1 KeyManager(FileWalletAdapter)

#### Class Name
`KeyManager`

#### Function Name
`KeyManager`

#### Function Introduction
`지정한 월렛 어댑터를 사용하는 KeyManager를 생성합니다. Lombok @RequiredArgsConstructor가 만든 생성자이며 null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| walletAdapter | `FileWalletAdapter` | 월렛이 열린 월렛 어댑터 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
KeyManager(FileWalletAdapter walletAdapter)
```

#### Function Usage
```java
KeyManager keyManager = new KeyManager(walletAdapter);
```
<br>

### 6.2.2 generateTasKey

#### Class Name
`KeyManager`

#### Function Name
`generateTasKey`

#### Function Introduction
`ID가 purpose(예: "assert", "auth", "keyagree", "invoke")인 secp256r1 키를 생성하고 그 키 ID를 반환합니다. null이나 공백만 거부하며 그 밖의 문자열은 그대로 키 ID로 씁니다. purpose가 null이거나 공백이면 SDK_INVALID_PARAMETER, 생성에 실패하면 SDK_KEY_GENERATION_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| purpose | `String` | 키 목적. 키 ID로 사용 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 생성된 키 ID (purpose와 같음) | M | |

#### Function Declaration
```java
String generateTasKey(String purpose)
```

#### Function Usage
```java
String keyId = keyManager.generateTasKey("assert");
```
<br>

### 6.2.3 sign(String, byte[])

#### Class Name
`KeyManager`

#### Function Name
`sign`

#### Function Introduction
`FileWalletAdapter.sign(SHA-256 해시, compact 서명)으로 keyId 키를 사용해 data에 서명합니다. 서명에 실패하면 TaSdkException(SDK_SIGNATURE_GENERATION_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| keyId | `String` | 키 ID | M | |
| data | `byte[]` | 서명할 데이터 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `byte[]` | compact 서명 | M | |

#### Function Declaration
```java
byte[] sign(String keyId, byte[] data)
```

#### Function Usage
```java
byte[] signature = keyManager.sign("assert", message);
```
<br>

### 6.2.4 sign(String, String)

#### Class Name
`KeyManager`

#### Function Name
`sign`

#### Function Introduction
`data를 UTF-8로 인코딩해 sign(String, byte[])를 호출합니다. data가 null이면 NullPointerException이 발생합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| keyId | `String` | 키 ID | M | |
| data | `String` | 서명할 문자열 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `byte[]` | compact 서명 | M | |

#### Function Declaration
```java
byte[] sign(String keyId, String data)
```

#### Function Usage
```java
byte[] signature = keyManager.sign("auth", "challenge-text");
```
<br>

### 6.2.5 getWalletAdapter

#### Class Name
`KeyManager`

#### Function Name
`getWalletAdapter`

#### Function Introduction
`생성자에 넘긴 FileWalletAdapter를 반환합니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `FileWalletAdapter` | 월렛 어댑터 | M | |

#### Function Declaration
```java
FileWalletAdapter getWalletAdapter()
```

#### Function Usage
```java
FileWalletAdapter adapter = keyManager.getWalletAdapter();
```
<br>

---

## 6.3 DidDocumentBuilder

`DidDocumentBuilder`는 did-common-sdk-server의 `BaseCoreDidUtil`로 DID Document를 생성, 파싱, 검증합니다. 월렛 어댑터는 `createAndSign`에서만 사용합니다.

**패키지**: `org.omnione.did.tas.core`

### 6.3.1 DidDocumentBuilder(FileWalletAdapter)

#### Class Name
`DidDocumentBuilder`

#### Function Name
`DidDocumentBuilder`

#### Function Introduction
`createAndSign에서 사용할 월렛 어댑터로 빌더를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | 월렛 어댑터 | M | createAndSign에만 필요 |

#### Output Parameters
n/a

#### Function Declaration
```java
DidDocumentBuilder(FileWalletAdapter fileWalletAdapter)
```

#### Function Usage
```java
DidDocumentBuilder didDocumentBuilder = new DidDocumentBuilder(walletAdapter);
```
<br>

### 6.3.2 createAndSign

#### Class Name
`DidDocumentBuilder`

#### Function Name
`createAndSign`

#### Function Introduction
`월렛의 모든 키로 DID Document를 만들고 서명합니다. 이름이 auth, assert, keyagree, invoke인 키는 각각 authentication, assertionMethod, keyAgreement, capabilityInvocation 목적을 받고, 그 밖의 키는 목적이 없습니다. 서명에는 auth, assert, invoke 키를 쓰고 keyagree는 쓰지 않습니다. 열린 월렛이 없으면 SDK_NOT_INITIALIZED, 그 밖의 실패는 SDK_UNKNOWN_ERROR TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 문서의 DID | M | 예: "did:omn:tas" |
| controller | `String` | Controller DID | M | 보통 did와 같음 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | 서명된 DID Document | M | |

#### Function Declaration
```java
DidDocument createAndSign(String did, String controller)
```

#### Function Usage
```java
DidDocument tasDidDoc = didDocumentBuilder.createAndSign("did:omn:tas", "did:omn:tas");
```
<br>

### 6.3.3 parse

#### Class Name
`DidDocumentBuilder`

#### Function Name
`parse`

#### Function Introduction
`DID Document JSON 문자열을 파싱합니다. 파싱에 실패하면 TaSdkException(SDK_INVALID_PARAMETER)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocJson | `String` | DID Document JSON | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | 파싱된 DID Document | M | |

#### Function Declaration
```java
DidDocument parse(String didDocJson)
```

#### Function Usage
```java
DidDocument didDoc = didDocumentBuilder.parse(didDocJson);
```
<br>

### 6.3.4 verifyKeyProofs

#### Class Name
`DidDocumentBuilder`

#### Function Name
`verifyKeyProofs`

#### Function Introduction
`DID Document에 있는 모든 키의 proof를 검증합니다. 모두 유효하면 정상 반환하고, 아니면 TaSdkException(SDK_INVALID_PROOF)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocument | `DidDocument` | 검증할 DID Document | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void verifyKeyProofs(DidDocument didDocument)
```

#### Function Usage
```java
didDocumentBuilder.verifyKeyProofs(didDoc);
```
<br>

### 6.3.5 getVerificationMethodId

#### Class Name
`DidDocumentBuilder`

#### Function Name
`getVerificationMethodId`

#### Function Introduction
`검증 메서드 ID {id}?versionId={versionId}#{keyId}를 만듭니다. keyId는 proof 목적에 따라 assert, auth, keyagree, invoke입니다. 문서에 versionId가 없으면 "1"을 씁니다. 해당 키가 문서에 있는지는 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| proofPurpose | `ProofPurpose` | Proof 목적 | M | `org.omnione.did.common.datamodel.enums.did.ProofPurpose` |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | 검증 메서드 ID | M | 예: "did:omn:tas?versionId=1#assert" |

#### Function Declaration
```java
String getVerificationMethodId(DidDocument didDocument, ProofPurpose proofPurpose)
```

#### Function Usage
```java
String vmId = didDocumentBuilder.getVerificationMethodId(tasDidDoc, ProofPurpose.ASSERTION_METHOD);
```
<br>

### 6.3.6 getVerificationMethod

#### Class Name
`DidDocumentBuilder`

#### Function Name
`getVerificationMethod`

#### Function Introduction
`id가 keyId와 같거나 "#" + keyId로 끝나는 검증 메서드를 반환합니다. 없으면 TaSdkException(SDK_INVALID_PROOF)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| keyId | `String` | 키 ID 또는 전체 검증 메서드 ID | M | 예: "assert" |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VerificationMethod` | 일치하는 검증 메서드 | M | |

#### Function Declaration
```java
VerificationMethod getVerificationMethod(DidDocument didDocument, String keyId)
```

#### Function Usage
```java
VerificationMethod vm = didDocumentBuilder.getVerificationMethod(tasDidDoc, "assert");
```
<br>

### 6.3.7 getPublicKey

#### Class Name
`DidDocumentBuilder`

#### Function Name
`getPublicKey`

#### Function Introduction
`getVerificationMethod와 같은 방식으로 찾은 검증 메서드의 publicKeyMultibase를 반환합니다. 검증 메서드나 공개키가 없으면 TaSdkException(SDK_INVALID_PROOF)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| keyId | `String` | 키 ID 또는 전체 검증 메서드 ID | M | 예: "keyagree" |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `String` | multibase 인코딩된 공개키 | M | |

#### Function Declaration
```java
String getPublicKey(DidDocument didDocument, String keyId)
```

#### Function Usage
```java
String publicKey = didDocumentBuilder.getPublicKey(clientDidDoc, "keyagree");
```
<br>

---

## 6.4 InvokedDidDocHandler

`InvokedDidDocHandler`는 DID Document를 등록하거나 갱신할 때 블록체인에 제출하는 `InvokedDidDoc`을 만듭니다. TAS가 invoker이며 capabilityInvocation(`invoke`) 키로 서명합니다.

**패키지**: `org.omnione.did.tas.core`

### 6.4.1 InvokedDidDocHandler(FileWalletAdapter, DidDocumentProvider)

#### Class Name
`InvokedDidDocHandler`

#### Function Name
`InvokedDidDocHandler`

#### Function Introduction
`지정한 월렛 어댑터로 서명하고 Provider에서 TAS DID Document를 가져오는 핸들러를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | TAS invoke 키가 있는 월렛 어댑터 | M | |
| didDocumentProvider | `DidDocumentProvider` | TAS DID Document Provider | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
InvokedDidDocHandler(FileWalletAdapter fileWalletAdapter, DidDocumentProvider didDocumentProvider)
```

#### Function Usage
```java
InvokedDidDocHandler invokedDidDocHandler = new InvokedDidDocHandler(walletAdapter, didDocumentProvider);
```
<br>

### 6.4.2 sign

#### Class Name
`InvokedDidDocHandler`

#### Function Name
`sign`

#### Function Introduction
`DidDocumentProvider.getTasDidDocument()로 TAS DID Document를 가져오고, ownerDidDoc 복사본에서 proof를 제거한 뒤, TAS를 invoker로, certificateUrl을 Certificate VC 참조로 하는 InvokedDidDoc(proof 타입 Secp256r1Signature2018)을 만들어 TAS invoke 검증 메서드의 id를 키 ID로 하는 월렛 키로 서명합니다. ownerDidDoc 자체는 바뀌지 않습니다. Provider가 던진 TaSdkException은 그대로 전달하고, 그 밖의 실패는 TaSdkException(SDK_SIGNATURE_GENERATION_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| ownerDidDoc | `DidDocument` | 등록할 DID Document (TAS, Entity, 사용자) | M | |
| certificateUrl | `String` | TAS Certificate VC URL | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `InvokedDidDoc` | 블록체인에 제출할 서명된 InvokedDidDoc | M | |

#### Function Declaration
```java
InvokedDidDoc sign(DidDocument ownerDidDoc, String certificateUrl)
```

#### Function Usage
```java
InvokedDidDoc invokedDidDoc = invokedDidDocHandler.sign(
    ownerDidDoc, "https://tas.example.org/api/v1/certificate-vc");
blockchainAdapter.registerDidDocument(invokedDidDoc, RoleType.WALLET);
```
<br>

---

## 6.5 BlockchainAdapter

`BlockchainAdapter`는 did-blockchain-sdk-server의 `ContractApi`로 블록체인(Hyperledger Fabric 또는 EVM)에 DID Document를 등록하고 조회합니다. 다른 메서드보다 먼저 `initialize()`를 호출해야 합니다. 블록체인 스토리지용 `StorageProvider`를 구현할 때 사용할 수 있습니다.

**패키지**: `org.omnione.did.tas.core`

### 6.5.1 BlockchainAdapter(StorageConfig)

#### Class Name
`BlockchainAdapter`

#### Function Name
`BlockchainAdapter`

#### Function Introduction
`블록체인 StorageConfig로 어댑터를 생성합니다. StorageConfig.forLss로 만든 설정이면 TaSdkException(SDK_INVALID_CONFIGURATION)을, null이면 NullPointerException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| storageConfig | `StorageConfig` | StorageConfig.forBlockchain으로 만든 스토리지 설정 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
BlockchainAdapter(StorageConfig storageConfig)
```

#### Function Usage
```java
BlockchainAdapter blockchainAdapter = new BlockchainAdapter(StorageConfig.forBlockchain(bcConfig));
```
<br>

### 6.5.2 initialize

#### Class Name
`BlockchainAdapter`

#### Function Name
`initialize`

#### Function Introduction
`StorageConfig.getBlockChainConfig()로 ContractApi를 생성합니다. 두 번째 호출부터는 경고 로그만 남깁니다. 연결을 만들 수 없으면 TaSdkException(SDK_BLOCKCHAIN_CONNECTION_FAILED)을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
n/a

#### Function Declaration
```java
void initialize()
```

#### Function Usage
```java
blockchainAdapter.initialize();
```
<br>

### 6.5.3 registerDidDocument

#### Class Name
`BlockchainAdapter`

#### Function Name
`registerDidDocument`

#### Function Introduction
`지정한 역할로 InvokedDidDoc을 블록체인에 등록합니다. initialize() 전이면 SDK_NOT_INITIALIZED, 블록체인 호출이 실패하면 SDK_DID_DOCUMENT_REGISTRATION_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| invokedDidDoc | `InvokedDidDoc` | TAS가 서명한 DID Document | M | 6.4 참조 |
| roleType | `RoleType` | DID 소유자 역할 | M | 예: TAS, WALLET, ETC |

#### Output Parameters
n/a

#### Function Declaration
```java
void registerDidDocument(InvokedDidDoc invokedDidDoc, RoleType roleType)
```

#### Function Usage
```java
blockchainAdapter.registerDidDocument(invokedDidDoc, RoleType.WALLET);
```
<br>

### 6.5.4 getDidDocument

#### Class Name
`BlockchainAdapter`

#### Function Name
`getDidDocument`

#### Function Introduction
`블록체인에서 did의 DID Document를 조회합니다. initialize() 전이면 SDK_NOT_INITIALIZED, 없거나 블록체인 호출이 실패하면 SDK_DID_DOCUMENT_NOT_FOUND TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 조회할 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `DidDocument` | DID Document | M | |

#### Function Declaration
```java
DidDocument getDidDocument(String did)
```

#### Function Usage
```java
DidDocument didDoc = blockchainAdapter.getDidDocument("did:omn:tas");
```
<br>

### 6.5.5 updateDidDocumentStatus

#### Class Name
`BlockchainAdapter`

#### Function Name
`updateDidDocumentStatus`

#### Function Introduction
`블록체인에서 DID Document의 상태를 바꿉니다. StorageProvider.updateDidDocumentStatus와 달리 상태는 DidDocStatus enum입니다. initialize() 전이면 SDK_NOT_INITIALIZED, 블록체인 호출이 실패하면 SDK_DID_DOCUMENT_UPDATE_FAILED TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 상태를 바꿀 DID | M | |
| status | `DidDocStatus` | 새 상태 | M | ACTIVATED, DEACTIVATED, REVOKED, TERMINATED |

#### Output Parameters
n/a

#### Function Declaration
```java
void updateDidDocumentStatus(String did, DidDocStatus status)
```

#### Function Usage
```java
blockchainAdapter.updateDidDocumentStatus("did:omn:user", DidDocStatus.ACTIVATED);
```
<br>

### 6.5.6 existsDidDocument

#### Class Name
`BlockchainAdapter`

#### Function Name
`existsDidDocument`

#### Function Introduction
`getDidDocument(did)가 성공하면 true, TaSdkException을 던지면 false를 반환합니다. initialize() 전이면 TaSdkException(SDK_NOT_INITIALIZED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 확인할 DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `boolean` | DID Document 존재 여부 | M | |

#### Function Declaration
```java
boolean existsDidDocument(String did)
```

#### Function Usage
```java
boolean exists = blockchainAdapter.existsDidDocument("did:omn:tas");
```
<br>

### 6.5.7 getContractApi

#### Class Name
`BlockchainAdapter`

#### Function Name
`getContractApi`

#### Function Introduction
`이 클래스가 제공하지 않는 작업을 위한 ContractApi를 반환합니다. initialize() 전이면 TaSdkException(SDK_NOT_INITIALIZED)을 던집니다.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ContractApi` | 블록체인 컨트랙트 API | M | |

#### Function Declaration
```java
ContractApi getContractApi()
```

#### Function Usage
```java
ContractApi contractApi = blockchainAdapter.getContractApi();
```
<br>

---

## 6.6 TaEcdhService

`TaEcdhService`는 TAS의 ECDH 키 협의를 수행하고 응답에 서명합니다. did-common-sdk-server의 `BaseEcdhServiceImpl`을 상속합니다. 클라이언트의 `keyagree` 공개키는 `DidDocumentProvider.getDidDocument(clientDid)`로 가져오고, 세션은 `EcdhSessionProvider.saveEcdhSession`으로 저장합니다. 상속한 `processEcdhRequest(EcdhReqData, SymmetricCipherType, SymmetricPaddingType)`, `verifyEcdhRequest(EcdhReqData, String)`, `generateSessionKey(EcdhReqData, byte[], byte[], SymmetricCipherType)`도 public이지만, `processEcdhRequest`는 서버 DID와 proof가 없는 `AccEcdh`를 반환하므로 `processAndSign`을 사용하세요.

**패키지**: `org.omnione.did.tas.core`

### 6.6.1 TaEcdhService(FileWalletAdapter, DidDocumentBuilder, DidDocumentProvider, EcdhSessionProvider)

#### Class Name
`TaEcdhService`

#### Function Name
`TaEcdhService`

#### Function Introduction
`ECDH 서비스를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | TAS keyagree 키가 있는 월렛 어댑터 | M | |
| didDocumentBuilder | `DidDocumentBuilder` | 클라이언트 공개키 추출용 | M | |
| didDocumentProvider | `DidDocumentProvider` | 클라이언트·TAS DID Document Provider | M | |
| ecdhSessionProvider | `EcdhSessionProvider` | 세션 저장 Provider | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaEcdhService(FileWalletAdapter fileWalletAdapter, DidDocumentBuilder didDocumentBuilder, DidDocumentProvider didDocumentProvider, EcdhSessionProvider ecdhSessionProvider)
```

#### Function Usage
```java
TaEcdhService ecdhService = new TaEcdhService(
    walletAdapter, didDocumentBuilder, didDocumentProvider, ecdhSessionProvider);
```
<br>

### 6.6.2 processAndSign

#### Class Name
`TaEcdhService`

#### Function Name
`processAndSign`

#### Function Introduction
`클라이언트의 ECDH 요청(클라이언트 DID 형식, keyAgreement proof 목적, 클라이언트 keyagree 키 서명)을 검증하고, 요청한 곡선으로 서버 키 쌍과 16바이트 서버 nonce를 만들고, 클라이언트 nonce가 16바이트인지 확인한 뒤 세션 키를 도출해 EcdhSessionProvider.saveEcdhSession(clientDid, sessionKey, mergedNonce)으로 저장합니다. server가 TAS DID이고 TAS keyagree 키({did}?versionId={versionId}#keyagree)로 서명한 keyAgreement proof가 있는 AccEcdh를 반환합니다. EcdhSessionProvider.saveEcdhSession이나 DidDocumentProvider.getTasDidDocument가 던진 TaSdkException은 그대로 전달하고, 클라이언트 DID Document를 가져오지 못한 경우를 포함한 그 밖의 실패는 TaSdkException(SDK_ECDH_KEY_AGREEMENT_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| ecdhReqData | `EcdhReqData` | 클라이언트 ECDH 요청 | M | client, clientNonce, curve, publicKey, candidate, proof |
| serverCipher | `SymmetricCipherType` | 서버 암호 방식 | M | 예: AES_256_CBC |
| serverPadding | `SymmetricPaddingType` | 서버 패딩 | M | PKCS5 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `AccEcdh` | 서명된 ECDH 수락 응답 | M | server, serverNonce, publicKey, cipher, padding, proof |

#### Function Declaration
```java
AccEcdh processAndSign(EcdhReqData ecdhReqData, SymmetricCipherType serverCipher, SymmetricPaddingType serverPadding)
```

#### Function Usage
```java
AccEcdh accEcdh = ecdhService.processAndSign(
    ecdhReqData, SymmetricCipherType.AES_256_CBC, SymmetricPaddingType.PKCS5);
```
<br>

---

## 6.7 TaSignatureService

`TaSignatureService`는 TAS 월렛으로 proof를 생성하고 검증합니다. did-common-sdk-server의 `BaseSignatureServiceImpl`을 상속합니다. 아래 public 메서드는 상속한 것이며, TA SDK는 proof 목적별 서명 키(ASSERTION_METHOD → `assert`, AUTHENTICATION → `auth`, CAPABILITY_INVOCATION → `invoke`)와 생성 시각을 제공합니다.

**패키지**: `org.omnione.did.tas.core`

### 6.7.1 TaSignatureService(FileWalletAdapter)

#### Class Name
`TaSignatureService`

#### Function Name
`TaSignatureService`

#### Function Introduction
`서명 서비스를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | TAS 키가 있는 월렛 어댑터 | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSignatureService(FileWalletAdapter fileWalletAdapter)
```

#### Function Usage
```java
TaSignatureService signatureService = new TaSignatureService(walletAdapter);
```
<br>

### 6.7.2 generateProof

#### Class Name
`TaSignatureService`

#### Function Name
`generateProof`

#### Function Introduction
`data를 정렬된 JSON으로 직렬화해 해시하고, proofPurpose에 해당하는 월렛 키로 서명한 뒤, multibase 인코딩된 서명과 현재 UTC 시각을 담은 Proof를 반환합니다. proof를 data에 붙이지는 않습니다. KEY_AGREEMENT이면 TaSdkException(SDK_INVALID_PARAMETER)을, 서명에 실패하면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| data | `Object` | 서명할 객체 | M | |
| proofPurpose | `ProofPurpose` | Proof 목적 | M | KEY_AGREEMENT 불가 |
| proofType | `ProofType` | Proof 타입 | M | 예: SECP_256R1_SIGNATURE_2018 |
| verificationMethod | `String` | proof에 넣을 검증 메서드 ID | M | 6.3.5 참조 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `Proof` | 생성된 proof | M | `org.omnione.did.common.datamodel.data.signature.Proof` |

#### Function Declaration
```java
Proof generateProof(Object data, ProofPurpose proofPurpose, ProofType proofType, String verificationMethod) throws CommonSdkException
```

#### Function Usage
```java
Proof proof = signatureService.generateProof(
    data, ProofPurpose.ASSERTION_METHOD, ProofType.SECP_256R1_SIGNATURE_2018,
    "did:omn:tas?versionId=1#assert");
```
<br>

### 6.7.3 verifyProof

#### Class Name
`TaSignatureService`

#### Function Name
`verifyProof`

#### Function Introduction
`data에서 proof 값을 제거하고 정렬된 JSON을 해시한 뒤, proof.verificationMethod가 가리키는 didDocument의 공개키(전체 id 또는 "#" + 키 ID로 찾음)로 proof.proofValue를 검증합니다. 키가 없거나 서명이 유효하지 않으면 CommonSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| data | `Object` | 서명된 객체 | M | |
| proof | `Proof` | 검증할 proof | M | `org.omnione.did.common.datamodel.data.signature.Proof` |
| didDocument | `DidDocument` | 서명자의 DID Document | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void verifyProof(Object data, Proof proof, DidDocument didDocument) throws CommonSdkException
```

#### Function Usage
```java
signatureService.verifyProof(data, proof, signerDidDoc);
```
<br>

### 6.7.4 verifyDidDocumentProof

#### Class Name
`TaSignatureService`

#### Function Name
`verifyDidDocumentProof`

#### Function Introduction
`DID Document에 있는 모든 키의 proof를 검증합니다. 유효하지 않은 proof가 있으면 CommonSdkException을 던집니다. DidDocumentBuilder.verifyKeyProofs와 같은 검사이며, 그쪽은 TaSdkException을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| didDocument | `DidDocument` | 검증할 DID Document | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void verifyDidDocumentProof(DidDocument didDocument) throws CommonSdkException
```

#### Function Usage
```java
signatureService.verifyDidDocumentProof(ownerDidDoc);
```
<br>

---

## 6.8 TaVcService

`TaVcService`는 TAS를 발급자로 하여 VC를 발급, 검증, 폐기합니다. `VcService`를 구현한 did-common-sdk-server의 `BaseVcServiceImpl`을 상속합니다. VC는 `DidDocumentProvider.getTasDidDocument()`의 TAS DID Document를 사용해 TAS `assert` 키로 서명하고, `VcStorageProvider.saveVc`로 저장하며, `VcStorageProvider.revokeVc`로 폐기합니다. `issueCredential`과 `verifyCredential`은 상속한 메서드이고, `revokeCredential`은 `VcService.revokeCredential`을 재정의하며, `issueVc`는 TA SDK가 추가한 메서드입니다.

**패키지**: `org.omnione.did.tas.core`

### 6.8.1 TaVcService(FileWalletAdapter, DidDocumentProvider, VcStorageProvider)

#### Class Name
`TaVcService`

#### Function Name
`TaVcService`

#### Function Introduction
`VC 서비스를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | TAS assert 키가 있는 월렛 어댑터 | M | |
| didDocumentProvider | `DidDocumentProvider` | TAS DID Document Provider | M | |
| vcStorageProvider | `VcStorageProvider` | VC 저장·폐기 확인·폐기 Provider | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaVcService(FileWalletAdapter fileWalletAdapter, DidDocumentProvider didDocumentProvider, VcStorageProvider vcStorageProvider)
```

#### Function Usage
```java
TaVcService vcService = new TaVcService(walletAdapter, didDocumentProvider, vcStorageProvider);
```
<br>

### 6.8.2 issueVc

#### Class Name
`TaVcService`

#### Function Name
`issueVc`

#### Function Introduction
`미리 설정한 IssueVcParam(스키마, 발급자, 클레임, 선택적으로 타입, evidence, 유효기간)으로 VC를 생성해 TAS assert 키로 서명하고, VcStorageProvider.saveVc로 저장한 뒤 반환합니다. Provider와 월렛 어댑터가 던진 TaSdkException은 그대로 전달하고, 그 밖의 실패는 TaSdkException(SDK_VC_ISSUANCE_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| issueVcParam | `IssueVcParam` | VC 발급 파라미터 | M | 스키마·발급자·클레임을 미리 설정 (VcSchemaProcessor.configure, ClaimManager.configure) |
| holderDid | `String` | VC 주체(holder) DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VerifiableCredential` | 서명된 VC | M | |

#### Function Declaration
```java
VerifiableCredential issueVc(IssueVcParam issueVcParam, String holderDid)
```

#### Function Usage
```java
IssueVcParam param = new IssueVcParam();
vcSchemaProcessor.configure(param, "certificate");
BaseCoreVcUtil.setIssuer(param, "did:omn:tas", "TAS", "https://tas.example.org/api/v1/certificate-vc");
ClaimManager.configure(param, ClaimManager.build("org.opendid.v1.role", "Issuer"));
VerifiableCredential vc = vcService.issueVc(param, "did:omn:issuer");
```
<br>

### 6.8.3 issueCredential

#### Class Name
`TaVcService`

#### Function Name
`issueCredential`

#### Function Introduction
`IssueProfile과 CredentialSubject로 VC를 발급합니다. 스키마 JSON은 issueProfile.profile.credentialSchema.value, 발급자는 issueProfile.profile.issuer(id, name, certVcRef), 클레임과 holder DID는 credentialSubject에서 가져옵니다. VC는 TAS assert 키로 서명하고 VcStorageProvider.saveVc로 저장합니다. BaseVcServiceImpl에서 상속합니다. 실패 시 CommonSdkException(예: VC_GENERATION_FAILED, VC_SIGNING_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| issueProfile | `IssueProfile` | 스키마와 발급자가 있는 Issue Profile | M | |
| credentialSubject | `CredentialSubject` | holder DID와 클레임 | M | 클레임은 null 가능 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VerifiableCredential` | 서명된 VC | M | |

#### Function Declaration
```java
VerifiableCredential issueCredential(IssueProfile issueProfile, CredentialSubject credentialSubject) throws CommonSdkException
```

#### Function Usage
```java
CredentialSubject subject = new CredentialSubject(
    "did:omn:issuer", ClaimManager.build("org.opendid.v1.role", "Issuer"));
VerifiableCredential vc = vcService.issueCredential(issueProfile, subject);
```
<br>

### 6.8.4 verifyCredential

#### Class Name
`TaVcService`

#### Function Name
`verifyCredential`

#### Function Introduction
`VC 구조를 검증하고, issuerDidDoc으로 proof를 검증하고, validUntil(ISO 8601)이 지난 VC를 거부한 뒤 VcStorageProvider.checkRevocationStatus를 호출합니다. BaseVcServiceImpl에서 상속합니다. 검사 중 하나라도 실패하면 CommonSdkException을 던집니다. 참고: 상속한 proof 검사가 proof.type을 ProofType.valueOf로 변환하므로, 현재 did-common-sdk-server에서는 proof 타입이 Secp256r1Signature2018 같은 표시 이름인 VC가 SIGNATURE_VERIFICATION_FAILED로 실패합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vc | `VerifiableCredential` | 검증할 VC | M | |
| issuerDidDoc | `DidDocument` | 발급자 DID Document | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void verifyCredential(VerifiableCredential vc, DidDocument issuerDidDoc) throws CommonSdkException
```

#### Function Usage
```java
vcService.verifyCredential(vc, tasDidDoc);
```
<br>

### 6.8.5 revokeCredential

#### Class Name
`TaVcService`

#### Function Name
`revokeCredential`

#### Function Introduction
`VcStorageProvider.revokeVc(vcId, reason)를 호출해 VC를 폐기합니다. VcService.revokeCredential을 재정의합니다. Provider가 TaSdkException을 던지면 CommonSdkException(VC_GENERATION_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcId | `String` | 폐기할 VC ID | M | |
| reason | `String` | 폐기 사유 | M | 예: "VOLUNTARY" |

#### Output Parameters
n/a

#### Function Declaration
```java
void revokeCredential(String vcId, String reason) throws CommonSdkException
```

#### Function Usage
```java
vcService.revokeCredential(vcId, "VOLUNTARY");
```
<br>

---

## 6.9 VcBuilder

`VcBuilder`는 P110, P120의 Certificate VC 같은 서명 전 VC를 fluent API로 만듭니다. `setIssuer`가 스키마를 읽고 새 `VcBuilder.BuildContext`를 반환하며, 나머지 메서드는 `BuildContext`의 메서드입니다. `VcBuilder`는 스레드 간에 공유할 수 있지만 `BuildContext`는 공유할 수 없습니다. `TasEnrollmentProtocolImpl`과 `EntityEnrollmentProtocolImpl`이 생성자로 `VcBuilder`를 받습니다.

**패키지**: `org.omnione.did.tas.core.vc`

### 6.9.1 VcBuilder(VcSchemaProvider, String)

#### Class Name
`VcBuilder`

#### Function Name
`VcBuilder`

#### Function Introduction
`setIssuer를 호출할 때마다 vcSchemaProvider로 VC 스키마 schemaId를 읽는 빌더를 생성합니다. 인자를 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcSchemaProvider | `VcSchemaProvider` | 스키마 JSON Provider | M | |
| schemaId | `String` | Provider에 넘길 스키마 ID | M | 예: "certificate" |

#### Output Parameters
n/a

#### Function Declaration
```java
VcBuilder(VcSchemaProvider vcSchemaProvider, String schemaId)
```

#### Function Usage
```java
VcBuilder vcBuilder = new VcBuilder(vcSchemaProvider, "certificate");
```
<br>

### 6.9.2 setIssuer

#### Class Name
`VcBuilder`

#### Function Name
`setIssuer`

#### Function Introduction
`새 빌드를 시작합니다. VcSchemaProvider.getVcSchemaJson(schemaId)로 스키마 JSON을 가져와 발급자(DID, 이름, Certificate VC URL)와 함께 새 IssueVcParam에 설정하고 새 BuildContext를 반환합니다. Provider가 던진 TaSdkException은 그대로 전달하고, 스키마를 파싱할 수 없으면 TaSdkException(CERTIFICATE_VC_GENERATION_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| did | `String` | 발급자 DID | M | |
| name | `String` | 발급자 이름 | M | |
| certVcRef | `String` | 발급자 Certificate VC URL | O | null 가능 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 새 빌드 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext setIssuer(String did, String name, String certVcRef)
```

#### Function Usage
```java
VerifiableCredential vc = vcBuilder
    .setIssuer("did:omn:tas", "TAS", "https://tas.example.org/api/v1/certificate-vc")
    .setType(List.of(VcType.VERIFIABLE_CREDENTIAL, VcType.CERTIFICATE_VC))
    .addClaim("org.opendid.v1.role", "Tas".getBytes(StandardCharsets.UTF_8))
    .build("did:omn:tas");
```
<br>

### 6.9.3 setType

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`setType`

#### Function Introduction
`VC 타입을 설정합니다. 이 컨텍스트를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| types | `List<VcType>` | VC 타입 | M | 예: VERIFIABLE_CREDENTIAL, CERTIFICATE_VC |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 이 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext setType(List<VcType> types)
```

#### Function Usage
```java
VcBuilder.BuildContext context = vcBuilder.setIssuer("did:omn:tas", "TAS", null);
context.setType(List.of(VcType.VERIFIABLE_CREDENTIAL, VcType.CERTIFICATE_VC));
```
<br>

### 6.9.4 addClaim

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`addClaim`

#### Function Introduction
`원시 값(보통 UTF-8 바이트)으로 클레임을 추가합니다. 같은 code의 클레임은 앞의 것을 대체합니다. 이 컨텍스트를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| code | `String` | 클레임 코드 | M | 예: "org.opendid.v1.subject" |
| value | `byte[]` | 클레임 값 | M | 대부분 UTF-8 바이트 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 이 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext addClaim(String code, byte[] value)
```

#### Function Usage
```java
context.addClaim("org.opendid.v1.subject", "o=Example".getBytes(StandardCharsets.UTF_8));
```
<br>

### 6.9.5 setValidityPeriod(String, String)

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`setValidityPeriod`

#### Function Introduction
`ZonedDateTime.parse로 파싱한 ISO 8601 문자열로 validFrom과 validUntil을 설정합니다. 형식이 잘못되면 DateTimeParseException, null이면 NullPointerException이 발생합니다. 이 컨텍스트를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| validFrom | `String` | 유효기간 시작 (ISO 8601) | M | 예: "2026-01-01T00:00:00Z" |
| validUntil | `String` | 유효기간 끝 (ISO 8601) | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 이 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext setValidityPeriod(String validFrom, String validUntil)
```

#### Function Usage
```java
context.setValidityPeriod("2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z");
```
<br>

### 6.9.6 setValidityPeriod(ZonedDateTime, ZonedDateTime)

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`setValidityPeriod`

#### Function Introduction
`validFrom과 validUntil을 설정합니다. 값을 검사하지 않습니다. 이 컨텍스트를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| validFrom | `ZonedDateTime` | 유효기간 시작 | M | |
| validUntil | `ZonedDateTime` | 유효기간 끝 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 이 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext setValidityPeriod(ZonedDateTime validFrom, ZonedDateTime validUntil)
```

#### Function Usage
```java
ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
context.setValidityPeriod(now, now.plusYears(1));
```
<br>

### 6.9.7 setEvidence

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`setEvidence`

#### Function Introduction
`evidence 목록을 이 evidence 하나로 설정합니다. null이면 NullPointerException이 발생합니다. 이 컨텍스트를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| evidence | `DocumentVerificationEvidence` | 문서 검증 evidence | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcBuilder.BuildContext` | 이 컨텍스트 | M | |

#### Function Declaration
```java
VcBuilder.BuildContext setEvidence(DocumentVerificationEvidence evidence)
```

#### Function Usage
```java
context.setEvidence(evidence);
```
<br>

### 6.9.8 build

#### Class Name
`VcBuilder.BuildContext`

#### Function Name
`build`

#### Function Introduction
`모은 클레임을 파라미터에 설정하고 holderDid용 서명 전 VC를 생성합니다. 결과는 WalletProvider.signVc 등으로 따로 서명합니다. 생성에 실패하면 TaSdkException(CERTIFICATE_VC_GENERATION_FAILED)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| holderDid | `String` | VC 주체(holder) DID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VerifiableCredential` | 서명 전 VC | M | |

#### Function Declaration
```java
VerifiableCredential build(String holderDid)
```

#### Function Usage
```java
VerifiableCredential unsignedVc = context.build("did:omn:issuer");
```
<br>

---

## 6.10 VcSchemaProcessor

`VcSchemaProcessor`는 `VcSchemaProvider`로 VC 스키마를 읽고 did-common-sdk-server의 `BaseCoreVcUtil`로 파싱합니다.

**패키지**: `org.omnione.did.tas.core`

### 6.10.1 VcSchemaProcessor(VcSchemaProvider)

#### Class Name
`VcSchemaProcessor`

#### Function Name
`VcSchemaProcessor`

#### Function Introduction
`스키마 처리기를 생성합니다. null을 검사하지 않습니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcSchemaProvider | `VcSchemaProvider` | 스키마 JSON Provider | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
VcSchemaProcessor(VcSchemaProvider vcSchemaProvider)
```

#### Function Usage
```java
VcSchemaProcessor vcSchemaProcessor = new VcSchemaProcessor(vcSchemaProvider);
```
<br>

### 6.10.2 parse

#### Class Name
`VcSchemaProcessor`

#### Function Name
`parse`

#### Function Introduction
`VC 스키마 JSON 문자열을 파싱합니다. 파싱에 실패하면 TaSdkException(SDK_INVALID_PARAMETER)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vcSchemaJson | `String` | VC 스키마 JSON | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcSchema` | 파싱된 스키마 | M | |

#### Function Declaration
```java
VcSchema parse(String vcSchemaJson)
```

#### Function Usage
```java
VcSchema schema = vcSchemaProcessor.parse(schemaJson);
```
<br>

### 6.10.3 load

#### Class Name
`VcSchemaProcessor`

#### Function Name
`load`

#### Function Introduction
`VcSchemaProvider.getVcSchemaJson(schemaId)로 스키마 JSON을 가져와 파싱합니다. Provider가 던진 TaSdkException은 그대로 전달하고, 파싱에 실패하면 TaSdkException(SDK_INVALID_PARAMETER)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| schemaId | `String` | 스키마 ID | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `VcSchema` | 파싱된 스키마 | M | |

#### Function Declaration
```java
VcSchema load(String schemaId)
```

#### Function Usage
```java
VcSchema schema = vcSchemaProcessor.load("certificate");
```
<br>

### 6.10.4 configure

#### Class Name
`VcSchemaProcessor`

#### Function Name
`configure`

#### Function Introduction
`VcSchemaProvider.getVcSchemaJson(schemaId)로 스키마 JSON을 가져와 파싱하고 param에 설정합니다. Provider가 던진 TaSdkException은 그대로 전달하고, 파싱에 실패하면 TaSdkException(SDK_INVALID_PARAMETER)을 던집니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| param | `IssueVcParam` | 설정할 발급 파라미터 | M | |
| schemaId | `String` | 스키마 ID | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
void configure(IssueVcParam param, String schemaId)
```

#### Function Usage
```java
IssueVcParam param = new IssueVcParam();
vcSchemaProcessor.configure(param, "certificate");
```
<br>

---

<div style="page-break-after: always;"></div>

# 7. Data Models

`IssuerApiClient`, 프로토콜, Provider 메서드 시그니처에 쓰이는 데이터 모델과 그 중첩 타입입니다. 따로 적지 않은 한 Lombok `@Getter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`를 사용합니다. setter가 없으므로 빌더나 전체 인자 생성자로 만듭니다. 필수 여부는 SDK가 요구하거나 항상 채우는 값을 기준으로 합니다.

## 7.1 OfferIssueVcApiRequest

`IssuerApiClient.offerIssueVc`(POST /issuer/api/v1/offer-issue-vc)의 요청입니다. SDK 프로토콜은 이 요청을 만들지 않습니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID | O |
| vcPlanId | `String` | VC Plan ID | M |
| issuer | `String` | Issuer DID | M |
| holder | `String` | Holder DID (푸시·이메일 Offer용) | O |
| email | `String` | 이메일 주소 (이메일 Offer용) | O |

<br>

## 7.2 OfferIssueVcApiResponse

`IssuerApiClient.offerIssueVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| offerId | `String` | Offer ID | M |
| validUntil | `String` | Offer 만료 시각 | M |

<br>

## 7.3 InspectIssueProposeApiRequest

`IssuerApiClient.inspectProposeIssue`(POST /issuer/api/v1/inspect-propose-issue)의 요청이며 `VcIssuanceProtocol.proposeIssueVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| vcPlanId | `String` | VC Plan ID | M |
| issuer | `String` | Issuer DID. ProposeIssueVcRequest.issuer, 비어 있으면 VcPlanInfo.issuerDid | M |
| offerId | `String` | Offer ID (사용자가 Offer에 응답한 경우) | O |

<br>

## 7.4 InspectIssueProposeApiResponse

`IssuerApiClient.inspectProposeIssue`의 응답입니다. txId나 refId가 비어 있으면 `proposeIssueVc`가 ISSUER_COMMUNICATION_FAILED를 던집니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | Issuer 트랜잭션 ID. ProposeIssueVcResult.externalTxId로 반환됨 | M |
| refId | `String` | Issuer 참조 번호 | M |

<br>

## 7.5 GenerateIssueProfileApiRequest

`IssuerApiClient.generateIssueProfile`(POST /issuer/api/v1/generate-issue-profile)의 요청이며 `VcIssuanceProtocol.requestIssueProfile`이 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| txId | `String` | VcIssuanceDataProvider.getExternalTxId의 Issuer 트랜잭션 ID | M |
| holder | `GenerateIssueProfileApiRequest.ApiHolder` | Holder 정보. 7.6 참조 | M |

<br>

## 7.6 GenerateIssueProfileApiRequest.ApiHolder

`GenerateIssueProfileApiRequest`의 Holder 정보이며 `HolderInfo`에서 복사합니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | Holder DID | M |
| pii | `String` | Holder PII(CI) | O |

<br>

## 7.7 GenerateIssueProfileApiResponse

`IssuerApiClient.generateIssueProfile`의 응답입니다. profile이 null이면 `requestIssueProfile`이 ISSUE_PROFILE_RETRIEVAL_FAILED를 던집니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| profile | `Object` | Issue Profile. RequestIssueProfileResult.profile로 그대로 반환됨 | M |

<br>

## 7.8 IssueVcApiRequest

`IssuerApiClient.issueVc`(POST /issuer/api/v1/issue-vc)의 요청이며 `VcIssuanceProtocol.requestIssueVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| txId | `String` | VcIssuanceDataProvider.getExternalTxId의 Issuer 트랜잭션 ID | M |
| accE2e | `AccE2e` | 월렛의 E2E 수락 정보. RequestIssueVcRequest.accE2e에서 복사. 7.18 참조 | M |
| encReqVc | `String` | 월렛이 암호화한 VC 요청. RequestIssueVcRequest.encReqVc에서 복사 | M |

<br>

## 7.9 IssueVcApiResponse

`IssuerApiClient.issueVc`의 응답입니다. e2e가 null이면 VC_ISSUANCE_FAILED, e2e.encVc가 비어 있으면 VC_ENCRYPTION_FAILED를 `requestIssueVc`가 던집니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| e2e | `E2e` | E2E 암호화된 VC. 7.19 참조 | M |

<br>

## 7.10 CompleteVcApiRequest

`IssuerApiClient.completeVc`(POST /issuer/api/v1/complete-vc)의 요청이며 `VcIssuanceProtocol.confirmIssueVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| txId | `String` | VcIssuanceDataProvider.getExternalTxId의 Issuer 트랜잭션 ID | M |
| vcId | `String` | 발급된 VC ID (UUID) | M |

<br>

## 7.11 CompleteVcApiResponse

`IssuerApiClient.completeVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

필드와 Lombok 애노테이션이 없는 클래스이며 `new CompleteVcApiResponse()`로 생성합니다. SDK는 반환 객체를 사용하지 않으므로 null을 반환해도 됩니다.

<br>

## 7.12 InspectProposeRevokeApiRequest

`IssuerApiClient.inspectProposeRevoke`(POST /issuer/api/v1/inspect-propose-revoke)의 요청이며 `VcRevocationProtocol.proposeRevokeVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| vcId | `String` | 폐기할 VC ID | M |

<br>

## 7.13 InspectProposeRevokeApiResponse

`IssuerApiClient.inspectProposeRevoke`의 응답입니다. issuerNonce가 비어 있으면 `proposeRevokeVc`가 PROPOSE_REVOKE_VC_FAILED를 던집니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| issuerNonce | `String` | Issuer nonce (16바이트, multibase 인코딩) | M |
| authType | `Integer` | 필요한 인증 방식: 1 PIN, 2 BIO, 3 PIN 또는 BIO, 4 PIN과 BIO | O |

<br>

## 7.14 RevokeVcApiRequest

`IssuerApiClient.revokeVc`(POST /issuer/api/v1/revoke-vc)의 요청이며 `VcRevocationProtocol.requestRevokeVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| reqRevokeVc | `ReqRevokeVc` | 월렛의 폐기 요청. RequestRevokeVcRequest.request에서 복사. 7.20 참조 | M |

<br>

## 7.15 RevokeVcApiResponse

`IssuerApiClient.revokeVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

필드와 Lombok 애노테이션이 없는 클래스이며 `new RevokeVcApiResponse()`로 생성합니다. SDK는 반환 객체를 사용하지 않으므로 null을 반환해도 됩니다.

<br>

## 7.16 CompleteRevokeApiRequest

`IssuerApiClient.completeRevoke`(POST /issuer/api/v1/complete-revoke)의 요청이며 `VcRevocationProtocol.confirmRevokeVc`가 만듭니다.

**패키지**: `org.omnione.did.tas.model.request.issuer`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 프로토콜 요청에서 복사 | O |
| txId | `String` | TAS 트랜잭션 ID (ConfirmRevokeVcRequest.txId) | M |

<br>

## 7.17 CompleteRevokeApiResponse

`IssuerApiClient.completeRevoke`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.issuer`

필드와 Lombok 애노테이션이 없는 클래스이며 `new CompleteRevokeApiResponse()`로 생성합니다. SDK는 반환 객체를 사용하지 않으므로 null을 반환해도 됩니다.

<br>

## 7.18 AccE2e

Issuer의 E2E 암호화 요청에 대한 월렛의 수락 정보입니다 (P210 Step 6). `RequestIssueVcRequest`와 `IssueVcApiRequest`에 쓰입니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| publicKey | `String` | E2E 암호화용 월렛 공개키 (multibase 인코딩) | M |
| iv | `String` | 초기화 벡터 (multibase 인코딩) | M |
| proof | `Proof` | 수락 정보의 proof (`org.omnione.did.data.model.did.Proof`) | M |

<br>

## 7.19 E2e

Issuer가 반환한 E2E 암호화 VC입니다 (P210 Step 6). TAS는 전달만 하며 복호화할 수 없습니다. `IssueVcApiResponse`와 `RequestIssueVcResult`에 쓰입니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| iv | `String` | 초기화 벡터 (multibase 인코딩) | M |
| encVc | `String` | 월렛만 복호화할 수 있는 암호화 VC (multibase 인코딩) | M |

<br>

## 7.20 ReqRevokeVc

월렛의 VC 폐기 요청입니다 (P220 Step 4). `RequestRevokeVcRequest`와 `RevokeVcApiRequest`에 쓰입니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| vcId | `String` | 폐기할 VC ID. VcRevocationDataProvider.getVcId(txId)와 같아야 함 | M |
| issuerNonce | `String` | Step 1에서 받은 Issuer nonce. VcRevocationDataProvider.getIssuerNonce(txId)와 같아야 함 | M |
| proof | `Proof` | assertionMethod 목적의 PIN 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`). type, verificationMethod, proofPurpose, proofValue 필수 | M |
| proofs | `List<Proof>` | 복합 인증용 추가 proof. SDK는 검사하지 않음 | O |

<br>

## 7.21 EnrollTasRequest

`TasEnrollmentProtocol.enrollTas`(P110)의 요청입니다. setter(`@Setter`)도 있습니다. `toString()`은 request를 가려서 출력합니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.tas`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| request | `EnrollTasRequest.Request` | 등록 요청 정보. 7.22 참조. null이면 enrollTas가 TAS_ENROLLMENT_FAILED를 던짐 | M |

<br>

## 7.22 EnrollTasRequest.Request

`EnrollTasRequest`의 등록 요청 정보입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.tas`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| password | `String` | TAS 등록 비밀번호. TasProvider.getTasPassword()와 같아야 하며 다르면 enrollTas가 PASSWORD_MISMATCH를 던짐 | M |

<br>

## 7.23 EnrollTasResponse

`TasEnrollmentProtocol.enrollTas`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.tas`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. SDK는 채우지 않음 | O |
| certVcRef | `String` | 게시된 TAS Certificate VC의 URL. CertificateProvider.publishCertificateVc의 반환값 | M |

<br>

## 7.24 ProposeEnrollEntityRequest

`EntityEnrollmentProtocol.proposeEnrollEntity`(P120 Step 1)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 로그에만 남김 | O |

<br>

## 7.25 ProposeEnrollEntityResponse

`EntityEnrollmentProtocol.proposeEnrollEntity`의 응답입니다. SDK는 이 값을 저장하지 않으므로, Step 3에서 TransactionProvider.getTransaction(txId)가 authNonce를 반환하도록 저장해 두어야 합니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 거래 ID (UUID) | M |
| authNonce | `String` | Step 3 DID Auth용 인증 nonce (16바이트, multibase 인코딩) | M |

<br>

## 7.26 RequestEnrollEntityRequest

`EntityEnrollmentProtocol.requestEnrollEntity`(P120 Step 3)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TransactionProvider.getTransaction과 EcdhSessionProvider 조회에 사용 | M |
| didAuth | `DidAuth` | 엔티티의 DID Auth: did, authNonce, proof. 7.66의 클래스가 아니라 `org.omnione.did.data.model.did.Proof`를 쓰는 `org.omnione.did.common.datamodel.data.did.DidAuth`임. SDK는 DID 형식, proof.verificationMethod의 DID key URL 형식, Transaction.authNonce와의 authNonce 일치, verificationMethod 키로 한 서명을 검사하며, proofPurpose와 verificationMethod의 DID가 did와 같은지는 검사하지 않음. 엔티티는 EntityProvider.getEntityByDid(did)로 조회. didAuth가 null이면 NullPointerException 발생 | M |
| dn | `String` | Certificate VC의 org.opendid.v1.subject 클레임에 쓸 DN. 비어 있으면 Entity.dn, 그것도 비어 있으면 "cn={name},o={role}" 사용 | O |

<br>

## 7.27 RequestEnrollEntityResponse

`EntityEnrollmentProtocol.requestEnrollEntity`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |
| iv | `String` | Certificate VC 암호화에 쓴 IV (16바이트, multibase 인코딩) | M |
| encVc | `String` | txId의 ECDH 세션키, cipher, padding으로 암호화한 Certificate VC (multibase 인코딩) | M |
| vcUrl | `String` | CertificateProvider.publishCertificateVc가 반환한 URL | M |
| vcId | `String` | 발급된 Certificate VC ID. Step 4의 vcId를 확인하도록 트랜잭션에 저장. 서버 내부용이며 API 응답에는 넣지 않음 | M |

<br>

## 7.28 ConfirmEnrollEntityRequest

`EntityEnrollmentProtocol.confirmEnrollEntity`(P120 Step 4)의 요청입니다. setter(`@Setter`)도 있습니다. SDK는 txId를 응답에 복사만 하므로 트랜잭션 상태와 vcId는 호출 전에 확인해야 합니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. 그대로 반환됨 | M |
| vcId | `String` | Step 3에서 받은 Certificate VC ID. SDK는 로그에만 남기므로 서버가 RequestEnrollEntityResponse.vcId와 비교 | O |

<br>

## 7.29 ConfirmEnrollEntityResponse

`EntityEnrollmentProtocol.confirmEnrollEntity`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.entity`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.30 ProposeRegisterUserRequest

`UserRegistrationProtocol.proposeRegisterUser`(P132 Step 1)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 로그에만 남김 | O |

<br>

## 7.31 ProposeRegisterUserResponse

`UserRegistrationProtocol.proposeRegisterUser`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 거래 ID (UUID). SDK는 저장하지 않음 | M |

<br>

## 7.32 RetrieveKycRequest

`UserRegistrationProtocol.retrieveKyc`(P132 Step 4)의 요청입니다. setter(`@Setter`)도 있습니다. kycToken과 kycTxId 중 하나를 넣어야 하며, 둘 다 비어 있으면 retrieveKyc가 KYC_TOKEN_AND_TXID_BOTH_MISSING을 던집니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| kycToken | `String` | KYC 시스템이 발급한 토큰 (Option A). 비어 있지 않으면 KycProvider.getPiiFromKycToken으로 PII를 얻고 kycTxId는 무시 | O |
| kycTxId | `String` | KYC 시스템의 거래 ID (Option B). kycToken이 비어 있으면 KycProvider.retrieveKycData에 사용 | O |

<br>

## 7.33 RetrieveKycResponse

`UserRegistrationProtocol.retrieveKyc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |
| piiData | `PiiData` | KycProvider가 반환한 PII. JSON에서 제외됨(`@JsonIgnore`). 서버가 txId와 함께 저장하며 클라이언트에 보내지 않음. 7.77 참조 | M |

<br>

## 7.34 RequestRegisterUserRequest

`UserRegistrationProtocol.requestRegisterUser`(P132 Step 5)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| signedDidDoc | `SignedDidDoc` | 월렛이 서명한 DID Document. 7.67 참조. ownerDidDoc의 DID는 아직 등록되지 않은 것이어야 함(USER_DID_ALREADY_EXISTS) | M |

<br>

## 7.35 RequestRegisterUserResponse

`UserRegistrationProtocol.requestRegisterUser`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.36 ConfirmRegisterUserRequest

`UserRegistrationProtocol.confirmRegisterUser`(P132 Step 6)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken에 전달되고 그대로 반환됨 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |

<br>

## 7.37 ConfirmRegisterUserResponse

`UserRegistrationProtocol.confirmRegisterUser`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.38 ProposeUpdateDidDocRequest

`DidDocUpdateProtocol.proposeUpdateDidDoc`(P141 Step 1)의 요청입니다. 호출 전에 사용자가 ACTIVATED인지 확인하고, Step 4에서 Transaction.did와 비교할 수 있도록 did를 트랜잭션에 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| did | `String` | DID Document를 갱신할 사용자 DID. SDK는 로그에만 남김 | O |

<br>

## 7.39 ProposeUpdateDidDocResponse

`DidDocUpdateProtocol.proposeUpdateDidDoc`의 응답입니다. Step 4에서 TransactionProvider.getTransaction(txId)가 반환하도록 txId, did, authNonce를 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 거래 ID (UUID) | M |
| authNonce | `String` | Step 4 DID Auth용 authNonce (16바이트, multibase 인코딩) | M |

<br>

## 7.40 RequestUpdateDidDocRequest

`DidDocUpdateProtocol.requestUpdateDidDoc`(P141 Step 4)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken과 TransactionProvider.getTransaction에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| signedDidDoc | `SignedDidDoc` | 월렛이 서명한 갱신 DID Document. 7.67 참조. versionId는 현재 versionId + 1, updated는 현재 값보다 이후여야 하며 @context, id, controller, created, deactivated는 바뀌면 안 됨 | M |
| didAuth | `DidAuth` | 사용자의 DID Auth. 7.66 참조. did는 갱신 DID Document의 id와 같아야 함 | M |

<br>

## 7.41 RequestUpdateDidDocResponse

`DidDocUpdateProtocol.requestUpdateDidDoc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.42 ConfirmUpdateDidDocRequest

`DidDocUpdateProtocol.confirmUpdateDidDoc`(P141 Step 5)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken에 전달되고 그대로 반환됨 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |

<br>

## 7.43 ConfirmUpdateDidDocResponse

`DidDocUpdateProtocol.confirmUpdateDidDoc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.44 ProposeRestoreDidDocRequest

`DidDocRestoreProtocol.proposeRestoreDidDoc`(P142 Step 1)의 요청입니다. 호출 전에 offerId와 사용자가 DEACTIVATED인지 확인하고, Step 4에서 Transaction.did와 비교할 수 있도록 did를 트랜잭션에 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| offerId | `String` | Offer 단계(푸시 또는 이메일)에서 받은 Offer ID. SDK는 읽지 않음 | O |
| did | `String` | DID Document를 복구할 사용자 DID. SDK는 로그에만 남김 | O |

<br>

## 7.45 ProposeRestoreDidDocResponse

`DidDocRestoreProtocol.proposeRestoreDidDoc`의 응답입니다. Step 4에서 TransactionProvider.getTransaction(txId)가 반환하도록 txId, did, authNonce를 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 거래 ID (UUID) | M |
| authNonce | `String` | Step 4 DID Auth용 authNonce (16바이트, multibase 인코딩) | M |

<br>

## 7.46 RequestRestoreDidDocRequest

`DidDocRestoreProtocol.requestRestoreDidDoc`(P142 Step 4)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken과 TransactionProvider.getTransaction에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| didAuth | `DidAuth` | 사용자의 DID Auth. 7.66 참조. 이 did의 DID Document가 ACTIVATED로 바뀜 | M |

<br>

## 7.47 RequestRestoreDidDocResponse

`DidDocRestoreProtocol.requestRestoreDidDoc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.48 ConfirmRestoreDidDocRequest

`DidDocRestoreProtocol.confirmRestoreDidDoc`(P142 Step 5)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| txId | `String` | Step 1에서 받은 거래 ID. TokenProvider.validateServerToken에 전달되고 그대로 반환됨 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |

<br>

## 7.49 ConfirmRestoreDidDocResponse

`DidDocRestoreProtocol.confirmRestoreDidDoc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.user`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.50 ProposeIssueVcRequest

`VcIssuanceProtocol.proposeIssueVc`(P210 Step 2)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 InspectIssueProposeApiRequest.id에 복사 | O |
| vcPlanId | `String` | VC Plan ID. VcIssuanceDataProvider.getVcPlan으로 조회(null이면 VC_PLAN_NOT_FOUND)하고 InspectIssueProposeApiRequest.vcPlanId에 복사 | M |
| issuer | `String` | Issuer DID. 비어 있으면 VcPlanInfo.issuerDid 사용. 둘 다 비어 있으면 ISSUER_NOT_FOUND | O |
| offerId | `String` | Offer ID (사용자가 Offer에 응답한 경우). InspectIssueProposeApiRequest.offerId에 복사 | O |

<br>

## 7.51 ProposeIssueVcResult

`VcIssuanceProtocol.proposeIssueVc`의 응답입니다. SDK는 이 값을 저장하지 않으므로, 이후 단계에서 VcIssuanceDataProvider가 반환하도록 txId와 함께 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 TAS 거래 ID (UUID) | M |
| externalTxId | `String` | Issuer 거래 ID (InspectIssueProposeApiResponse.txId). VcIssuanceDataProvider.getExternalTxId(txId)로 반환해야 함 | M |
| refId | `String` | Issuer 참조 번호 (InspectIssueProposeApiResponse.refId) | M |

<br>

## 7.52 RequestIssueProfileRequest

`VcIssuanceProtocol.requestIssueProfile`(P210 Step 5)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 GenerateIssueProfileApiRequest.id에 복사 | O |
| txId | `String` | TAS 거래 ID. TokenProvider.validateServerToken, VcIssuanceDataProvider.getExternalTxId와 getHolderInfo에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |

<br>

## 7.53 RequestIssueProfileResult

`VcIssuanceProtocol.requestIssueProfile`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |
| authNonce | `String` | Step 6 DID Auth용 새 authNonce (16바이트, multibase 인코딩). VcIssuanceDataProvider.getAuthNonce(txId)가 반환하도록 저장 | M |
| profile | `Object` | Issuer의 Issue Profile (GenerateIssueProfileApiResponse.profile). 그대로 반환됨 | M |

<br>

## 7.54 RequestIssueVcRequest

`VcIssuanceProtocol.requestIssueVc`(P210 Step 6)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 IssueVcApiRequest.id에 복사 | O |
| txId | `String` | TAS 거래 ID. TokenProvider.validateServerToken, VcIssuanceDataProvider.getAuthNonce와 getExternalTxId에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| didAuth | `DidAuth` | Holder의 DID Auth. 7.66 참조. VcIssuanceDataProvider.getAuthNonce(txId)로 검증. SDK는 did를 HolderInfo.did와 비교하지 않음 | M |
| accE2e | `AccE2e` | 월렛의 E2E 수락 정보. 검사 없이 IssueVcApiRequest.accE2e로 전달. 7.18 참조 | M |
| encReqVc | `String` | 월렛이 암호화한 VC 요청. 검사 없이 IssueVcApiRequest.encReqVc로 전달 | M |

<br>

## 7.55 RequestIssueVcResult

`VcIssuanceProtocol.requestIssueVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |
| e2e | `E2e` | Issuer의 E2E 암호화 VC (IssueVcApiResponse.e2e). 그대로 반환됨. 7.19 참조 | M |

<br>

## 7.56 ConfirmIssueVcRequest

`VcIssuanceProtocol.confirmIssueVc`(P210 Step 7)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 CompleteVcApiRequest.id에 복사 | O |
| txId | `String` | TAS 거래 ID. TokenProvider.validateServerToken과 VcIssuanceDataProvider.getExternalTxId에 전달 | M |
| serverToken | `String` | 서버 토큰. TokenProvider.validateServerToken(serverToken, txId)로 검증 | M |
| vcId | `String` | 발급된 VC ID. UUID여야 함(비어 있거나 UUID가 아니면 VC_ID_MISMATCH). CompleteVcApiRequest.vcId에 복사 | M |

<br>

## 7.57 ConfirmIssueVcResult

`VcIssuanceProtocol.confirmIssueVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.58 ProposeRevokeVcRequest

`VcRevocationProtocol.proposeRevokeVc`(P220 Step 1)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 InspectProposeRevokeApiRequest.id에 복사 | O |
| vcId | `String` | 폐기할 VC ID. VcRevocationDataProvider.getVcMeta로 조회(null이면 VC_META_FETCH_FAILED)하고 InspectProposeRevokeApiRequest.vcId에 복사 | M |

<br>

## 7.59 ProposeRevokeVcResult

`VcRevocationProtocol.proposeRevokeVc`의 응답입니다. Step 4에서 VcRevocationDataProvider.getVcId(txId)와 getIssuerNonce(txId)가 반환하도록 txId, vcId, issuerNonce를 저장해야 합니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | SDK가 생성한 TAS 거래 ID (UUID) | M |
| issuerNonce | `String` | Issuer nonce (InspectProposeRevokeApiResponse.issuerNonce, 16바이트, multibase 인코딩) | M |
| authType | `Integer` | Issuer가 요구하는 인증 방식: 1 PIN, 2 BIO, 3 PIN 또는 BIO, 4 PIN과 BIO. Issuer가 주지 않으면 null | O |

<br>

## 7.60 RequestRevokeVcRequest

`VcRevocationProtocol.requestRevokeVc`(P220 Step 4)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 RevokeVcApiRequest.id에 복사 | O |
| txId | `String` | TAS 거래 ID. VcRevocationDataProvider.getVcId와 getIssuerNonce에 전달 | M |
| serverToken | `String` | 서버 토큰. SDK는 읽지도 검증하지도 않으므로 호출 전에 서버가 검증 | O |
| request | `ReqRevokeVc` | 월렛의 폐기 요청. 7.20 참조. 검사 후 RevokeVcApiRequest.reqRevokeVc로 전달. null이면 requestRevokeVc가 SDK_UNKNOWN_ERROR를 던짐 | M |

<br>

## 7.61 RequestRevokeVcResult

`VcRevocationProtocol.requestRevokeVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.62 ConfirmRevokeVcRequest

`VcRevocationProtocol.confirmRevokeVc`(P220 Step 5)의 요청입니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK가 CompleteRevokeApiRequest.id에 복사 | O |
| txId | `String` | TAS 거래 ID. CompleteRevokeApiRequest.txId와 응답에 복사 | M |
| serverToken | `String` | 서버 토큰. SDK는 읽지도 검증하지도 않으므로 호출 전에 서버가 검증 | O |

<br>

## 7.63 ConfirmRevokeVcResult

`VcRevocationProtocol.confirmRevokeVc`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.vc`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| txId | `String` | 거래 ID. 요청에서 복사 | M |

<br>

## 7.64 RegisterWalletRequest

`WalletRegistrationProtocol.registerWallet`(P131)의 요청입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.request.protocol.wallet`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | 메시지 ID. SDK는 읽지 않음 | O |
| attestedDidDoc | `AttestedDidDoc` | Wallet Provider가 증명한 월렛 DID Document (`org.omnione.did.common.datamodel.data.did.AttestedDidDoc`): walletId, ownerDidDoc(키 proof를 포함한 DID Document JSON, multibase 인코딩), provider(`Provider`: did, certVcRef), nonce, proof(구조 전체에 대한 provider의 assertionMethod 서명). provider.certVcRef는 선택이며 있으면 CertificateProvider.validateCertificateVc로 검증. attestedDidDoc이 null이면 registerWallet이 SDK_FIELD_REQUIRED를 던짐 | M |

<br>

## 7.65 RegisterWalletResponse

`WalletRegistrationProtocol.registerWallet`의 응답입니다.

**패키지**: `org.omnione.did.tas.model.response.protocol.wallet`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| walletDid | `String` | 등록된 월렛 DID (owner DID Document의 id) | M |
| txId | `String` | 거래 ID. SDK는 채우지 않음 | O |

<br>

## 7.66 DidAuth

`RequestUpdateDidDocRequest`, `RequestRestoreDidDocRequest`, `RequestIssueVcRequest`에 쓰이는 DID Auth입니다. `RequestEnrollEntityRequest`는 대신 `org.omnione.did.common.datamodel.data.did.DidAuth`를 씁니다. setter(`@Setter`)도 있습니다. SDK는 proof.proofValue를 뺀 이 객체에 대한 서명을 검증합니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | 사용자 DID. 유효한 DID여야 하고 proof.verificationMethod의 DID와 같아야 함. DID Document 프로토콜에서는 Transaction.did가 있으면 그 값과도 같아야 함 | M |
| authNonce | `String` | TAS에서 받은 authNonce (16바이트, multibase 인코딩). 저장된 값과 디코딩한 바이트로 비교(AUTH_NONCE_MISMATCH) | M |
| proof | `Proof` | 사용자 authentication 키로 한 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`). verificationMethod는 DID key URL, proofPurpose는 authentication이어야 함. null이면 PROOF_MISSING | M |

<br>

## 7.67 SignedDidDoc

월렛이 서명한 DID Document입니다. `RequestRegisterUserRequest`와 `RequestUpdateDidDocRequest`에 쓰입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| ownerDidDoc | `String` | 각 키의 proof를 포함한 owner DID Document JSON (multibase 인코딩). SDK가 디코딩·파싱하고 모든 키 proof를 검증(OWNER_DID_DOC_KEY_PROOF_FAILED) | M |
| wallet | `Wallet` | 이 객체에 서명한 월렛. 7.68 참조. wallet.did는 proof.verificationMethod의 DID와 같아야 함 | M |
| nonce | `String` | 서명 nonce (multibase 인코딩). 서명 대상에 포함되며 SDK는 값을 검사하지 않음 | M |
| proof | `Proof` | ownerDidDoc, wallet, nonce에 대한 월렛 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`). type, verificationMethod(월렛 DID의 DID key URL), proofPurpose(assertionMethod), proofValue 필수 | M |

<br>

## 7.68 Wallet

`SignedDidDoc`과 `SignedWalletInfo`의 월렛 식별 정보입니다. setter(`@Setter`)도 있습니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| id | `String` | TAS에 등록된 월렛 ID. 서명 대상에 포함되며 SDK는 값을 검사하지 않음 | M |
| did | `String` | 월렛 DID. SignedDidDoc에서는 proof.verificationMethod의 DID와 같아야 함(P132는 SDK_INVALID_PROOF, P141은 DID_MISMATCH) | M |

<br>

## 7.69 ServerTokenSeed

토큰 생성 단계에서 월렛이 보내는 서버 토큰 seed입니다. 이 클래스를 읽거나 반환하는 SDK 메서드는 없으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| purpose | `ServerTokenPurpose` | 서버 토큰 용도. P132에서는 CREATE_DID(5) 또는 CREATE_DID_AND_ISSUE_VC(13). 4.2 참조 | M |
| walletInfo | `SignedWalletInfo` | 월렛이 서명한 월렛 정보. 7.70 참조 | M |
| caAppInfo | `AttestedAppInfo` | CA Provider가 증명한 CA 앱 정보. 7.71 참조 | M |

<br>

## 7.70 SignedWalletInfo

월렛이 서명한 월렛 정보이며 `ServerTokenSeed`에 쓰입니다. 이 클래스를 읽거나 반환하는 SDK 메서드는 없으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| wallet | `Wallet` | 월렛 ID와 DID. 7.68 참조 | M |
| nonce | `String` | 재전송 방지용 서명 nonce (multibase 인코딩) | M |
| proof | `Proof` | 월렛 정보에 대한 월렛 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`) | M |

<br>

## 7.71 AttestedAppInfo

CA Provider가 증명한 CA 앱 정보이며 `ServerTokenSeed`에 쓰입니다. 이 클래스를 읽거나 반환하는 SDK 메서드는 없으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| appId | `String` | CA Provider에 등록된 클라이언트 앱 ID | M |
| provider | `Provider` | CA Provider의 DID와 certVcRef (`org.omnione.did.common.datamodel.data.provider.Provider`) | M |
| nonce | `String` | 재전송 방지용 서명 nonce (multibase 인코딩) | M |
| proof | `Proof` | 앱 정보에 대한 CA Provider 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`) | M |

<br>

## 7.72 ServerTokenData

TAS가 서명하는 서버 토큰 데이터입니다. setter(`@Setter`)도 있습니다. null 필드는 JSON에서 빠집니다(`@JsonInclude(NON_NULL)`). 이 클래스를 읽거나 반환하는 SDK 메서드는 없으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.data`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| purpose | `ServerTokenPurpose` | 서버 토큰 용도. 4.2 참조 | M |
| walletId | `String` | 월렛 ID. ServerTokenSeed.walletInfo.wallet.id에서 가져옴 | M |
| appId | `String` | CA 앱 ID. ServerTokenSeed.caAppInfo.appId에서 가져옴 | M |
| validUntil | `String` | 토큰 만료 시각 (ISO 8601, UTC) | M |
| provider | `Provider` | 토큰을 발급한 TAS의 DID와 certVcRef (`org.omnione.did.common.datamodel.data.provider.Provider`) | M |
| nonce | `String` | 재전송 방지용 서명 nonce (16바이트, multibase 인코딩) | M |
| proof | `Proof` | TAS assert 키 서명 (`org.omnione.did.common.datamodel.data.signature.Proof`). proofValue를 null로 두고 서명 메시지를 만든 뒤 proofValue를 채움 | M |

<br>

## 7.73 Tas

`TasProvider.getTasInfo`가 반환하는 TAS 정보입니다. `TasEnrollmentProtocol.enrollTas`가 TAS Certificate VC를 만들 때 사용합니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | TAS DID. TAS DID Document 조회(StorageProvider.findDidDoc), Certificate VC의 issuer와 subject, CertificateProvider.generateCertificateVcUrl에 사용 | M |
| name | `String` | TAS 이름. Certificate VC의 issuer 이름으로 사용 | M |
| status | `TasStatus` | TAS 등록 상태. 4.3 참조. SDK는 읽지 않음 | O |
| subjectDn | `String` | Certificate VC의 org.opendid.v1.subject 클레임에 쓸 subject DN. null이면 enrollTas가 CERTIFICATE_VC_GENERATION_FAILED를 던짐 | M |
| version | `String` | TAS 버전. SDK는 읽지 않음 | O |

<br>

## 7.74 Entity

`EntityProvider.getEntityByDid`와 `getEntityByRole`이 반환하는 엔티티 정보입니다. `EntityEnrollmentProtocol.requestEnrollEntity`가 Certificate VC를 만들 때, `WalletRegistrationProtocol.registerWallet`이 Wallet Provider를 확인할 때 사용합니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | 엔티티 DID. Certificate VC의 subject와 CertificateProvider.generateCertificateVcUrl에 사용 | M |
| name | `String` | 엔티티 이름. 기본 DN "cn={name},o={role}"에 사용 | O |
| role | `RoleType` | 엔티티 역할 (`org.omnione.did.data.model.enums.vc.RoleType`). raw 값이 org.opendid.v1.role 클레임이 되며, registerWallet은 WALLET_PROVIDER를 요구 | M |
| dn | `String` | org.opendid.v1.subject 클레임에 쓸 DN. RequestEnrollEntityRequest.dn이 비어 있으면 사용 | O |
| certVcRef | `String` | 엔티티 Certificate VC의 URL. SDK는 읽지 않음 | O |

<br>

## 7.75 Transaction

`TransactionProvider.getTransaction(txId)`가 반환하는 거래 정보입니다. SDK는 `EntityEnrollmentProtocol.requestEnrollEntity`와 `DidDocUpdateProtocol`·`DidDocRestoreProtocol`의 Step 4에서 읽습니다. null이면 requestEnrollEntity는 SDK_TRANSACTION_NOT_FOUND를, DID Document 프로토콜은 REQUEST_UPDATE_DIDDOC_FAILED 또는 REQUEST_RESTORE_DIDDOC_FAILED를 던집니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| authNonce | `String` | Propose 단계에서 반환한 authNonce (16바이트, multibase 인코딩). DidAuth.authNonce와 비교 | M |
| did | `String` | Propose 단계에서 제출된 DID. DID Document 프로토콜에서는 값이 있으면 DidAuth.did와 같아야 함(DID_MISMATCH). 엔티티 등록 프로토콜은 읽지 않음 | O |

<br>

## 7.76 User

`UserProvider.findByDid`가 반환하는 사용자 정보입니다. findByDid를 호출하는 SDK 프로토콜이 없으므로 SDK는 이 필드를 읽지 않으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | 사용자 DID | M |
| status | `UserStatus` | 사용자 상태: ACTIVATED, DEACTIVATED, REVOKED, TERMINATED. 4.4 참조 | M |
| pii | `String` | KYC로 얻은 사용자 PII(CI). 로그에 남길 때 마스킹 | M |

<br>

## 7.77 PiiData

`KycProvider.getPiiFromKycToken`과 `retrieveKycData`가 반환하고 `RetrieveKycResponse.piiData`에 담기는 PII입니다. SDK는 null 여부만 검사(KYC_DATA_RETRIEVAL_FAILED)하고 필드는 읽지 않으며, 필수 여부는 소스 주석을 따릅니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| pii | `String` | KYC 시스템이 사용자 신원에서 만든 PII(CI). 중복 사용자 판별 키 | M |
| name | `String` | 이름 | M |
| birthDate | `String` | 생년월일 (YYYYMMDD) | M |
| phoneNumber | `String` | 전화번호 | O |
| address | `String` | 주소 | O |
| additionalClaims | `Map<String, String>` | KYC 시스템이 준 추가 신원 속성 | O |

<br>

## 7.78 HolderInfo

`VcIssuanceDataProvider.getHolderInfo(txId)`가 반환하는 Holder 정보입니다. `VcIssuanceProtocol.requestIssueProfile`이 `GenerateIssueProfileApiRequest.ApiHolder`(7.6)로 복사합니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| did | `String` | Holder DID. null이면 requestIssueProfile이 HOLDER_NOT_FOUND를 던짐 | M |
| pii | `String` | Holder PII (CI, multibase 인코딩). ApiHolder.pii로 복사 | O |

<br>

## 7.79 VcPlanInfo

`VcIssuanceDataProvider.getVcPlan(vcPlanId)`가 반환하는 VC Plan 정보입니다. null이면 `VcIssuanceProtocol.proposeIssueVc`가 VC_PLAN_NOT_FOUND를 던집니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| vcPlanId | `String` | VC Plan ID. SDK는 읽지 않음 | O |
| name | `String` | VC Plan 이름. SDK는 읽지 않음 | O |
| issuerDid | `String` | Issuer DID. ProposeIssueVcRequest.issuer가 비어 있으면 사용. 둘 다 비어 있으면 ISSUER_NOT_FOUND | O |
| vcSchemaId | `String` | VC Schema ID. SDK는 읽지 않음 | O |

<br>

## 7.80 VcMeta

`VcRevocationDataProvider.getVcMeta(vcId)`가 반환하는 VC 메타데이터입니다. `StorageProvider`가 쓰는 `org.omnione.did.data.model.vc.VcMeta`와는 다른 클래스입니다. null이면 `VcRevocationProtocol.proposeRevokeVc`가 VC_META_FETCH_FAILED를 던집니다.

**패키지**: `org.omnione.did.tas.model.domain`

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| vcId | `String` | VC ID. 오류 메시지에만 사용 | O |
| issuer | `String` | Issuer DID. 비어 있으면 proposeRevokeVc가 ISSUER_NOT_FOUND를 던짐 | M |
| subject | `String` | Subject(Holder) DID. SDK는 읽지 않음 | O |
| status | `String` | VC 상태: "ACTIVE", "REVOKED", "EXPIRED". proposeRevokeVc는 "ACTIVE"를 요구하며 아니면 VC_ALREADY_REVOKED, VC_EXPIRED, VC_STATUS_INVALID를 던짐 | M |
| issuedDate | `String` | 발급일 (ISO 8601). SDK는 읽지 않음 | O |
| expirationDate | `String` | 만료일 (ISO 8601). SDK는 읽지 않음 | O |

<br>

---

<div style="page-break-after: always;"></div>

# 8. 보조 유틸리티

SDK 구현을 돕는 보조 클래스입니다. 같은 일을 하는 주요 API가 있으면 주요 API를 쓰는 것을 권장합니다.

## 8.1 AuthNonceMatcher

authNonce 값을 디코딩한 바이트로 비교합니다. 프로토콜이 DID Auth 검사에 사용합니다.

**패키지**: `org.omnione.did.tas.core`

| 메서드 | 설명 |
|--------|------|
| `static boolean matches(String expected, String actual)` | 두 값을 multibase 디코딩해 바이트를 비교하므로, 같은 nonce가 다른 multibase 인코딩이어도 일치합니다. 둘 중 하나가 null이거나 디코딩할 수 없으면 false를 반환합니다. |

<br>

## 8.2 ClaimManager

`IssueVcParam`용 `ClaimInfo` 객체와 클레임 맵을 만드는 static 도우미입니다. Certificate VC에는 `VcBuilder.BuildContext.addClaim`이 같은 일을 합니다.

**패키지**: `org.omnione.did.tas.core`

| 메서드 | 설명 |
|--------|------|
| `static ClaimInfo create(String code, String value)` | code와 value의 UTF-8 바이트로 ClaimInfo를 만듭니다. value가 null이면 NullPointerException이 발생합니다. |
| `static HashMap<String, ClaimInfo> build(String... codeValuePairs)` | 코드와 값이 번갈아 오는 문자열로 코드→ClaimInfo 맵을 만듭니다. 인자 개수가 홀수이면 IllegalArgumentException을 던집니다. 같은 코드가 다시 오면 뒤의 값으로 바뀝니다. |
| `static void configure(IssueVcParam param, HashMap<String, ClaimInfo> claims)` | claims를 param의 클레임(privacy)으로 설정하며, 기존 클레임은 대체됩니다. |

<br>
