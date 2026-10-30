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
- 일자: 2026-03-04
- 버전: v2.0.0

| 버전   | 일자       | 변경 내용 |
| ------ | ---------- | --------- |
| v2.0.0 | 2026-03-04 | 초기 작성 |


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
- [3. APIs - Config](#3-apis---config)
    - [3.1 TaSdkConfig](#31-tasdkconfig)
    - [3.2 WalletConfig](#32-walletconfig)
    - [3.3 CryptoConfig](#33-cryptoconfg)
    - [3.4 StorageConfig](#34-storageconfig)
- [4. Enumerator](#4-enumerator)
    - [4.1 OfferType](#41-offertype)
    - [4.2 ServerTokenPurpose](#42-servertokenpurpose)
    - [4.3 TasStatus](#43-tasstatus)
    - [4.4 UserStatus](#44-userstatus)
- [5. APIs - Exceptions](#5-apis---exceptions)
    - [5.1 TaSdkException](#51-tasdkexception)
    - [5.2 TaSdkErrorCode](#52-tasdkerrorcode)

<div style="page-break-after: always;"></div>

# 1. APIs - Protocols

Protocol 인터페이스는 TA SDK의 핵심 API입니다. 서버는 각 Protocol 구현체를 스프링 빈으로 주입받아 사용합니다. 모든 Protocol 구현체는 Provider 인터페이스 구현체를 생성자 파라미터로 받습니다.

## 1.1 TasEnrollmentProtocol

### 1.1.1 enrollTas

#### Class Name
`TasEnrollmentProtocol`

#### Function Name
`enrollTas`

#### Function Introduction
`TAS 자가 등록을 수행합니다 (P110). 비밀번호 검증 후 TAS DID Document를 블록체인에 등록하고 Certificate VC를 발급합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `EnrollTasRequest` | TAS 등록 요청 데이터 | M | password 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `EnrollTasResponse` | TAS 등록 응답 | M | txId 포함 |

#### Function Declaration
```java
EnrollTasResponse enrollTas(EnrollTasRequest request) throws TaSdkException
```

#### Function Usage
```java
TasEnrollmentProtocol protocol = new TasEnrollmentProtocolImpl(
    tasProvider, certificateProvider, storageProvider, walletProvider, didDocumentProvider
);
EnrollTasResponse response = protocol.enrollTas(request);
```
<br>

---

## 1.2 EntityEnrollmentProtocol

### 1.2.1 proposeEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`proposeEnrollEntity`

#### Function Introduction
`Entity 등록 프로토콜 Step 1 (P120). txId와 authNonce를 생성하고 트랜잭션을 초기화합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeEnrollEntityRequest` | Entity 등록 제안 요청 | M | did 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeEnrollEntityResponse` | 등록 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeEnrollEntityResponse proposeEnrollEntity(ProposeEnrollEntityRequest request) throws TaSdkException
```

#### Function Usage
```java
ProposeEnrollEntityResponse response = protocol.proposeEnrollEntity(request);
```
<br>

### 1.2.2 requestEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`requestEnrollEntity`

#### Function Introduction
`Entity 등록 프로토콜 Step 3 (P120). DID Auth를 검증하고 Certificate VC를 발급합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestEnrollEntityRequest` | Entity 등록 요청 데이터 | M | txId, didAuth, signedDidDoc 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestEnrollEntityResponse` | 등록 요청 응답 | M | txId, vcUrl 포함 |

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
`Entity 등록 프로토콜 Step 4 (P120). Entity 등록을 최종 확인하고 트랜잭션을 완료합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmEnrollEntityRequest` | Entity 등록 확인 요청 | M | txId 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmEnrollEntityResponse` | 등록 확인 응답 | M | txId 포함 |

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

### 1.3.1 registerWallet

#### Class Name
`WalletRegistrationProtocol`

#### Function Name
`registerWallet`

#### Function Introduction
`월렛 DID Document 등록을 수행합니다 (P131). 3계층 서명(월렛, DID Document 키, CA App)을 검증하고 InvokedDidDoc을 블록체인에 등록합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RegisterWalletRequest` | 월렛 등록 요청 데이터 | M | signedWalletInfo, attestedAppInfo, signedDidDoc 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RegisterWalletResponse` | 월렛 등록 응답 | M | txId 포함 |

#### Function Declaration
```java
RegisterWalletResponse registerWallet(RegisterWalletRequest request) throws TaSdkException
```

#### Function Usage
```java
WalletRegistrationProtocol protocol = new WalletRegistrationProtocolImpl(
    entityProvider, certificateProvider, storageProvider, walletProvider,
    didDocumentProvider, walletRegistrationProvider
);
RegisterWalletResponse response = protocol.registerWallet(request);
```
<br>

---

## 1.4 UserRegistrationProtocol

### 1.4.1 proposeRegisterUser

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`proposeRegisterUser`

#### Function Introduction
`사용자 등록 프로토콜 Step 1 (P132). txId를 생성하고 트랜잭션을 초기화합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeRegisterUserRequest` | 사용자 등록 제안 요청 | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeRegisterUserResponse` | 등록 제안 응답 | M | txId 포함 |

#### Function Declaration
```java
ProposeRegisterUserResponse proposeRegisterUser(ProposeRegisterUserRequest request) throws TaSdkException
```

#### Function Usage
```java
ProposeRegisterUserResponse response = protocol.proposeRegisterUser(request);
```
<br>

### 1.4.2 retrieveKyc

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`retrieveKyc`

#### Function Introduction
`사용자 등록 프로토콜 Step 4 (P132). serverToken을 검증하고 KYC PII 데이터를 조회하여 serverToken Seed에 서명합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RetrieveKycRequest` | KYC 조회 요청 | M | txId, serverTokenSeed, kycToken 또는 kycTxId 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RetrieveKycResponse` | KYC 조회 응답 | M | txId, iv, encStd 포함 |

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
`사용자 등록 프로토콜 Step 5 (P132). SignedDidDoc의 서명을 검증하고 사용자 DID Document를 블록체인에 등록합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestRegisterUserRequest` | 사용자 등록 요청 데이터 | M | txId, signedDidDoc 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestRegisterUserResponse` | 등록 요청 응답 | M | txId 포함 |

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
`사용자 등록 프로토콜 Step 6 (P132). 사용자 등록을 최종 확인하고 트랜잭션을 완료합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmRegisterUserRequest` | 사용자 등록 확인 요청 | M | txId 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmRegisterUserResponse` | 등록 확인 응답 | M | txId 포함 |

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

### 1.5.1 proposeUpdateDidDoc

#### Class Name
`DidDocUpdateProtocol`

#### Function Name
`proposeUpdateDidDoc`

#### Function Introduction
`DID Document 갱신 프로토콜 Step 1 (P141). 사용자 상태를 검증하고 txId, authNonce를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeUpdateDidDocRequest` | DID Document 갱신 제안 요청 | M | did 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeUpdateDidDocResponse` | 갱신 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeUpdateDidDocResponse proposeUpdateDidDoc(ProposeUpdateDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
DidDocUpdateProtocol protocol = new DidDocUpdateProtocolImpl(
    userProvider, transactionProvider, tokenProvider, storageProvider,
    walletProvider, didDocumentProvider
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
`DID Document 갱신 프로토콜 Step 4 (P141). DID Auth를 검증하고 갱신된 DID Document를 블록체인에 등록합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestUpdateDidDocRequest` | DID Document 갱신 요청 데이터 | M | txId, signedDidDoc 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestUpdateDidDocResponse` | 갱신 요청 응답 | M | txId 포함 |

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
`DID Document 갱신 프로토콜 Step 5 (P141). serverToken을 검증하고 갱신을 최종 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmUpdateDidDocRequest` | DID Document 갱신 확인 요청 | M | txId, serverToken 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmUpdateDidDocResponse` | 갱신 확인 응답 | M | txId 포함 |

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

### 1.6.1 proposeRestoreDidDoc

#### Class Name
`DidDocRestoreProtocol`

#### Function Name
`proposeRestoreDidDoc`

#### Function Introduction
`DID Document 복구 프로토콜 Step 1 (P142). DEACTIVATED 상태 사용자를 검증하고 txId, authNonce를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeRestoreDidDocRequest` | DID Document 복구 제안 요청 | M | did, offerType 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeRestoreDidDocResponse` | 복구 제안 응답 | M | txId, authNonce 포함 |

#### Function Declaration
```java
ProposeRestoreDidDocResponse proposeRestoreDidDoc(ProposeRestoreDidDocRequest request) throws TaSdkException
```

#### Function Usage
```java
DidDocRestoreProtocol protocol = new DidDocRestoreProtocolImpl(
    userProvider, transactionProvider, tokenProvider, storageProvider,
    walletProvider, didDocumentProvider
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
`DID Document 복구 프로토콜 Step 4 (P142). DID Auth를 검증하고 DID Document를 블록체인에서 활성화합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestRestoreDidDocRequest` | DID Document 복구 요청 데이터 | M | txId, didAuth 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestRestoreDidDocResponse` | 복구 요청 응답 | M | txId 포함 |

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
`DID Document 복구 프로토콜 Step 5 (P142). serverToken을 검증하고 복구를 최종 확인합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmRestoreDidDocRequest` | DID Document 복구 확인 요청 | M | txId, serverToken 포함 |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmRestoreDidDocResponse` | 복구 확인 응답 | M | txId 포함 |

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

### 1.7.1 proposeIssueVc

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`proposeIssueVc`

#### Function Introduction
`VC 발급 프로토콜 Step 2 (P210). VC Plan을 조회하고 Issuer에 발급 제안을 전달하여 txId와 refId를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeIssueVcRequest` | VC 발급 제안 요청 | M | vcPlanId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeIssueVcResult` | VC 발급 제안 응답 | M | txId, refId 포함 |

#### Function Declaration
```java
ProposeIssueVcResult proposeIssueVc(ProposeIssueVcRequest request, IssuerApiClient issuerClient, VcIssuanceDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
VcIssuanceProtocol protocol = new VcIssuanceProtocolImpl(
    transactionProvider, storageProvider, walletProvider,
    didDocumentProvider, vcStorageProvider, vcSchemaProvider
);
ProposeIssueVcResult result = protocol.proposeIssueVc(request, issuerClient, dataProvider);
```
<br>

### 1.7.2 requestIssueProfile

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`requestIssueProfile`

#### Function Introduction
`VC 발급 프로토콜 Step 5 (P210). Issuer로부터 Issue Profile을 조회하고 authNonce를 생성합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestIssueProfileRequest` | Issue Profile 요청 | M | txId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestIssueProfileResult` | Issue Profile 응답 | M | txId, authNonce, profile 포함 |

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
`VC 발급 프로토콜 Step 6 (P210). DID Auth를 검증하고 Issuer에 VC 발급을 요청합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestIssueVcRequest` | VC 발급 요청 데이터 | M | txId, didAuth, encReqVc 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestIssueVcResult` | VC 발급 요청 응답 | M | txId, e2e 포함 |

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
`VC 발급 프로토콜 Step 7 (P210). Issuer에 VC 발급 완료를 통보하고 트랜잭션을 완료합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmIssueVcRequest` | VC 발급 확인 요청 | M | txId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcIssuanceDataProvider` | VC 발급 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmIssueVcResult` | VC 발급 확인 응답 | M | txId 포함 |

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

### 1.8.1 proposeRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`proposeRevokeVc`

#### Function Introduction
`VC 폐기 프로토콜 Step 1 (P220). VC 상태를 검증하고 Issuer에 폐기 제안을 전달하여 txId와 issuerNonce를 반환합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ProposeRevokeVcRequest` | VC 폐기 제안 요청 | M | vcId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ProposeRevokeVcResult` | VC 폐기 제안 응답 | M | txId, issuerNonce 포함 |

#### Function Declaration
```java
ProposeRevokeVcResult proposeRevokeVc(ProposeRevokeVcRequest request, IssuerApiClient issuerClient, VcRevocationDataProvider dataProvider) throws TaSdkException
```

#### Function Usage
```java
VcRevocationProtocol protocol = new VcRevocationProtocolImpl(
    transactionProvider, storageProvider, walletProvider,
    didDocumentProvider, vcStorageProvider
);
ProposeRevokeVcResult result = protocol.proposeRevokeVc(request, issuerClient, dataProvider);
```
<br>

### 1.8.2 requestRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`requestRevokeVc`

#### Function Introduction
`VC 폐기 프로토콜 Step 4 (P220). PIN 서명을 검증하고 Issuer에 폐기 실행을 요청합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `RequestRevokeVcRequest` | VC 폐기 요청 데이터 | M | txId, vcId, reqRevokeVc 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `RequestRevokeVcResult` | VC 폐기 요청 응답 | M | txId 포함 |

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
`VC 폐기 프로토콜 Step 5 (P220). Issuer에 폐기 완료를 통보하고 트랜잭션을 완료합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| request | `ConfirmRevokeVcRequest` | VC 폐기 확인 요청 | M | txId 포함 |
| issuerClient | `IssuerApiClient` | Issuer API 클라이언트 | M | |
| dataProvider | `VcRevocationDataProvider` | VC 폐기 데이터 Provider | M | |

#### Output Parameters
| Type | Description | M/O | 비고 |
|---|---|---|---|
| `ConfirmRevokeVcResult` | VC 폐기 확인 응답 | M | txId 포함 |

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
`ECDH 키 협의 완료 후 세션 데이터(세션 키, 병합 Nonce)를 저장합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| clientDid | `String` | 클라이언트 DID | M | |
| sessionKey | `String` | ECDH 협의된 세션 키 (Base64) | M | |
| mergedNonce | `String` | 병합된 Nonce | M | |

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
| `String` | ECDH 세션 키 | M | |

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
| `String` | 협의된 패딩 방식 (예: "PKCS5Padding") | M | |

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
| `Entity` | 조회된 Entity 정보 | M | |

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
`Entity가 작업 수행 가능한 상태(COMPLETED)인지 검증합니다. 유효하지 않은 경우 예외를 발생시킵니다.`

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
| vcMeta | `VcMeta` | 등록할 VC 메타데이터 | M | vcId, issuer, status 등 포함 |

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
| `VcMeta` | 조회된 VC 메타데이터 | O | 없으면 null |

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
| `String` | TAS 등록 비밀번호 | M | |

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
| `HolderInfo` | Holder 정보 | M | holderDid, pii 등 포함 |

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
| `VcPlanInfo` | VC Plan 정보 | M | vcPlanId, issuerDid, schemaId 등 포함 |

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
| `VcMeta` | VC 메타데이터 | M | |

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
`TAS 개인키로 VC에 서명합니다.`

#### Input Parameters
| Parameter | Type | Description | M/O | 비고 |
|---|---|---|---|---|
| vc | `VerifiableCredential` | 서명할 VC | M | |
| didDoc | `DidDocument` | TAS DID Document | M | |
| keyPurpose | `String` | 서명에 사용할 키 목적 | M | 예: "assertionMethod" |

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
| `String` | Base64 인코딩된 서명값 | M | |

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
`월렛이 연결되어 있지 않으면 예외를 발생시킵니다.`

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

<div style="page-break-after: always;"></div>

# 3. APIs - Config

## 3.1 TaSdkConfig

TaSdkConfig는 TA SDK 초기화에 필요한 설정을 묶은 컨테이너입니다. WalletConfig, CryptoConfig, StorageConfig를 포함합니다.

### 3.1.1 builder

#### Class Name
`TaSdkConfig`

#### Function Name
`builder`

#### Function Introduction
`TaSdkConfig 빌더를 생성합니다.`

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

---

## 3.2 WalletConfig

WalletConfig는 파일 기반 월렛의 경로와 비밀번호를 관리합니다.

### 3.2.1 builder

#### Class Name
`WalletConfig`

#### Function Name
`builder`

#### Function Introduction
`WalletConfig 빌더를 생성합니다.`

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

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| filePath | `String` | 월렛 파일 경로 | M |
| password | `String` | 월렛 비밀번호 | M |

<br>

---

## 3.3 CryptoConfig

CryptoConfig는 서명 알고리즘, 해시 알고리즘, 키 크기를 설정합니다. 기본값으로 사용 가능합니다.

### 3.3.1 builder

#### Class Name
`CryptoConfig`

#### Function Name
`builder`

#### Function Introduction
`CryptoConfig 빌더를 생성합니다.`

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
CryptoConfig config = CryptoConfig.builder()
    .signatureAlgorithm("Secp256r1")
    .hashAlgorithm("SHA-256")
    .keySize(256)
    .build();
```

| 필드 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| signatureAlgorithm | `String` | `"Secp256r1"` | 서명 알고리즘 |
| hashAlgorithm | `String` | `"SHA-256"` | 해시 알고리즘 |
| keySize | `int` | `256` | 키 크기 (bits) |

<br>

---

## 3.4 StorageConfig

StorageConfig는 DID Document와 VC 메타데이터를 저장할 스토리지 타입(블록체인 또는 LSS)을 설정합니다.

### 3.4.1 forBlockchain

#### Class Name
`StorageConfig`

#### Function Name
`forBlockchain`

#### Function Introduction
`블록체인 스토리지 설정을 생성합니다.`

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
`LSS(Ledger Service Server) 스토리지 설정을 생성합니다.`

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
`스토리지 타입을 확인합니다.`

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

P142 DID Document 복구 프로토콜에서 Offer 전송 방식을 정의합니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | 설명 |
|---------|------|
| `PUSH` | 푸시 알림으로 Offer를 전송합니다 |
| `EMAIL` | 이메일로 Offer를 전송합니다 |

**사용 예시**:
```java
ProposeRestoreDidDocRequest request = new ProposeRestoreDidDocRequest();
request.setOfferType(OfferType.PUSH);
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
- `Integer getPurpose()` — 숫자 purpose 값 반환
<br>

## 4.3 TasStatus

TAS의 등록 상태를 나타냅니다.

**패키지**: `org.omnione.did.tas.model.enums`

| Enum 값 | 설명 |
|---------|------|
| `PENDING` | TAS가 설정되었지만 아직 등록되지 않은 상태 |
| `COMPLETED` | TAS 등록이 완료된 상태 |

**상태 흐름**: `PENDING` → (enrollTas 성공) → `COMPLETED`
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

**참고**: P141(DID Doc 갱신)은 `ACTIVATED` 상태에서만 가능하고, P142(DID Doc 복구)는 `DEACTIVATED` 상태에서만 가능합니다.
<br>

---

<div style="page-break-after: always;"></div>

# 5. APIs - Exceptions

## 5.1 TaSdkException

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
