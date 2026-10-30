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

- Topic: TA SDK Server API
- Author: OpenDID
- Date: 2026-03-04
- Version: v2.0.0

| Version | Date       | Changes       |
| ------- | ---------- | ------------- |
| v2.0.0  | 2026-03-04 | Initial Draft |


<div style="page-break-after: always;"></div>

# Table of Contents

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
    - [3.3 CryptoConfig](#33-cryptoconfig)
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

Protocol interfaces are the core API of the TA SDK. The server obtains each Protocol implementation as a Spring bean and invokes it. Every Protocol implementation accepts Provider interface implementations as constructor parameters.

## 1.1 TasEnrollmentProtocol

### 1.1.1 enrollTas

#### Class Name
`TasEnrollmentProtocol`

#### Function Name
`enrollTas`

#### Function Introduction
`Performs TAS self-enrollment (P110). Validates the password, registers the TAS DID Document on the blockchain, and issues a Certificate VC.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `EnrollTasRequest` | TAS enrollment request data | M | Includes password |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `EnrollTasResponse` | TAS enrollment response | M | Includes txId |

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
`Entity enrollment protocol Step 1 (P120). Generates a txId and authNonce, and initializes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeEnrollEntityRequest` | Entity enrollment proposal request | M | Includes did |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeEnrollEntityResponse` | Enrollment proposal response | M | Includes txId, authNonce |

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
`Entity enrollment protocol Step 3 (P120). Validates DID Auth and issues a Certificate VC.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestEnrollEntityRequest` | Entity enrollment request data | M | Includes txId, didAuth, signedDidDoc |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestEnrollEntityResponse` | Enrollment request response | M | Includes txId, vcUrl |

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
`Entity enrollment protocol Step 4 (P120). Confirms entity enrollment and completes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmEnrollEntityRequest` | Entity enrollment confirmation request | M | Includes txId |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmEnrollEntityResponse` | Enrollment confirmation response | M | Includes txId |

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
`Registers a Wallet DID Document (P131). Verifies the 3-layer signature (wallet, DID Document key, CA App) and registers the InvokedDidDoc on the blockchain.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RegisterWalletRequest` | Wallet registration request data | M | Includes signedWalletInfo, attestedAppInfo, signedDidDoc |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RegisterWalletResponse` | Wallet registration response | M | Includes txId |

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
`User registration protocol Step 1 (P132). Generates a txId and initializes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeRegisterUserRequest` | User registration proposal request | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeRegisterUserResponse` | Registration proposal response | M | Includes txId |

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
`User registration protocol Step 4 (P132). Validates the serverToken, retrieves KYC PII data, and signs the ServerTokenSeed.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RetrieveKycRequest` | KYC retrieval request | M | Includes txId, serverTokenSeed, and either kycToken or kycTxId |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RetrieveKycResponse` | KYC retrieval response | M | Includes txId, iv, encStd |

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
`User registration protocol Step 5 (P132). Verifies the SignedDidDoc signatures and registers the user's DID Document on the blockchain.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestRegisterUserRequest` | User registration request data | M | Includes txId, signedDidDoc |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestRegisterUserResponse` | Registration request response | M | Includes txId |

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
`User registration protocol Step 6 (P132). Confirms user registration and completes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmRegisterUserRequest` | User registration confirmation request | M | Includes txId |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmRegisterUserResponse` | Registration confirmation response | M | Includes txId |

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
`DID Document update protocol Step 1 (P141). Validates the user status and generates a txId and authNonce.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeUpdateDidDocRequest` | DID Document update proposal request | M | Includes did |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeUpdateDidDocResponse` | Update proposal response | M | Includes txId, authNonce |

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
`DID Document update protocol Step 4 (P141). Validates DID Auth and registers the updated DID Document on the blockchain.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestUpdateDidDocRequest` | DID Document update request data | M | Includes txId, signedDidDoc |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestUpdateDidDocResponse` | Update request response | M | Includes txId |

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
`DID Document update protocol Step 5 (P141). Validates the serverToken and confirms the update.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmUpdateDidDocRequest` | DID Document update confirmation request | M | Includes txId, serverToken |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmUpdateDidDocResponse` | Update confirmation response | M | Includes txId |

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
`DID Document restore protocol Step 1 (P142). Validates that the user is in DEACTIVATED status and generates a txId and authNonce.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeRestoreDidDocRequest` | DID Document restore proposal request | M | Includes did, offerType |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeRestoreDidDocResponse` | Restore proposal response | M | Includes txId, authNonce |

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
`DID Document restore protocol Step 4 (P142). Validates DID Auth and activates the DID Document on the blockchain.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestRestoreDidDocRequest` | DID Document restore request data | M | Includes txId, didAuth |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestRestoreDidDocResponse` | Restore request response | M | Includes txId |

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
`DID Document restore protocol Step 5 (P142). Validates the serverToken and confirms the restore.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmRestoreDidDocRequest` | DID Document restore confirmation request | M | Includes txId, serverToken |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmRestoreDidDocResponse` | Restore confirmation response | M | Includes txId |

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
`VC issuance protocol Step 2 (P210). Retrieves the VC Plan, forwards the issuance proposal to the Issuer, and returns the txId and refId.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeIssueVcRequest` | VC issuance proposal request | M | Includes vcPlanId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeIssueVcResult` | VC issuance proposal response | M | Includes txId, refId |

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
`VC issuance protocol Step 5 (P210). Retrieves the Issue Profile from the Issuer and generates an authNonce.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestIssueProfileRequest` | Issue Profile request | M | Includes txId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestIssueProfileResult` | Issue Profile response | M | Includes txId, authNonce, profile |

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
`VC issuance protocol Step 6 (P210). Validates DID Auth and requests VC issuance from the Issuer.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestIssueVcRequest` | VC issuance request data | M | Includes txId, didAuth, encReqVc |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestIssueVcResult` | VC issuance request response | M | Includes txId, e2e |

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
`VC issuance protocol Step 7 (P210). Notifies the Issuer of VC issuance completion and finalizes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmIssueVcRequest` | VC issuance confirmation request | M | Includes txId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmIssueVcResult` | VC issuance confirmation response | M | Includes txId |

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
`VC revocation protocol Step 1 (P220). Validates the VC status, forwards the revocation proposal to the Issuer, and returns the txId and issuerNonce.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ProposeRevokeVcRequest` | VC revocation proposal request | M | Includes vcId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ProposeRevokeVcResult` | VC revocation proposal response | M | Includes txId, issuerNonce |

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
`VC revocation protocol Step 4 (P220). Validates the PIN signature and requests VC revocation from the Issuer.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RequestRevokeVcRequest` | VC revocation request data | M | Includes txId, vcId, reqRevokeVc |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RequestRevokeVcResult` | VC revocation request response | M | Includes txId |

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
`VC revocation protocol Step 5 (P220). Notifies the Issuer of revocation completion and finalizes the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `ConfirmRevokeVcRequest` | VC revocation confirmation request | M | Includes txId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ConfirmRevokeVcResult` | VC revocation confirmation response | M | Includes txId |

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

Provider interfaces are the contract between the SDK and the server. The server implements each Provider interface and injects it into the Protocol implementations. Through Providers, the SDK operates without any direct dependency on databases, blockchains, or external systems.

## 2.1 CertificateProvider

### 2.1.1 publishCertificateVc

#### Class Name
`CertificateProvider`

#### Function Name
`publishCertificateVc`

#### Function Introduction
`Persists a Certificate VC to the database and returns its publicly accessible URL.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| certificateVc | `VerifiableCredential` | Certificate VC to store | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Publicly accessible URL of the Certificate VC | M | |

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
`Generates a Certificate VC URL for the given DID without performing any actual storage.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID for which to generate the Certificate VC URL | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Generated Certificate VC URL | M | |

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
`Retrieves a Certificate VC by its VC ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VerifiableCredential` | Retrieved Certificate VC | M | |

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
`Retrieves the Certificate VC at the given URL and verifies that it matches the specified provider DID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| certVcRef | `String` | Certificate VC reference URL | M | |
| providerDid | `String` | Provider DID to validate against | M | |

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
`Retrieves a DID Document by its DID identifier, from cache or blockchain.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID identifier to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `DidDocument` | Retrieved DID Document | M | |

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
`Returns the TAS's own DID Document.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
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
`Persists ECDH session data (session key, merged nonce) after key agreement is complete.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| clientDid | `String` | Client DID | M | |
| sessionKey | `String` | Negotiated ECDH session key (Base64) | M | |
| mergedNonce | `String` | Merged nonce | M | |

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
`Retrieves the ECDH session key by transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | ECDH session key | M | |

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
`Retrieves the negotiated cipher algorithm by transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Negotiated cipher algorithm (e.g., "AES-256-CBC") | M | |

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
`Retrieves the negotiated padding scheme by transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Negotiated padding scheme (e.g., "PKCS5Padding") | M | |

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
`Retrieves the client DID by transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Client DID | M | |

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
`Checks whether an ECDH session exists for the given transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the ECDH session exists | M | |

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
`Retrieves entity information by DID identifier.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | Entity DID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Entity` | Retrieved entity information | M | |

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
`Retrieves an entity by its role type.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| roleType | `RoleType` | Role type to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Entity` | Retrieved entity information | M | |

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
`Checks whether an entity with the given DID exists.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | Entity DID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the entity exists | M | |

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
`Validates that the entity is in an eligible state (COMPLETED) to perform operations. Throws an exception if invalid.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | Entity DID to validate | M | |

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
`Retrieves PII (Personally Identifiable Information) using a KYC system token.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| kycToken | `String` | KYC system access token | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `PiiData` | Retrieved PII data | M | Includes pii (CI), name, etc. |

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
`Retrieves PII by calling an external KYC server API with a KYC transaction ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| kycTxId | `String` | KYC transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `PiiData` | Retrieved PII data | M | |

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
`Registers a DID Document on the blockchain or LSS.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| invokedDidDoc | `InvokedDidDoc` | InvokedDidDoc to register | M | Includes TAS signature |
| roleType | `RoleType` | DID Document role type | M | |

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
`Retrieves a DID Document from the blockchain or LSS. Throws an exception if not found.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID identifier to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `DidDocument` | Retrieved DID Document | M | |

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
`Updates the status of a DID Document on the blockchain or LSS.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID whose status to update | M | |
| status | `String` | New status value | M | e.g., "ACTIVATED", "DEACTIVATED" |

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
`Checks whether a DID Document exists on the blockchain or LSS.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID identifier to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the DID Document exists | M | |

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
`Retrieves the version ID of a DID Document from the blockchain or LSS.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID identifier to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | DID Document version ID | M | |

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
`Retrieves a DID Document from the blockchain or LSS. Returns null if not found.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID identifier to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `DidDocument` | Retrieved DID Document, or null if not found | O | |

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
`Registers VC metadata on the blockchain or LSS.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcMeta | `VcMeta` | VC metadata to register | M | Includes vcId, issuer, status, etc. |

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
`Retrieves VC metadata from the blockchain or LSS by VC ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcMeta` | Retrieved VC metadata, or null if not found | O | |

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
`Returns TAS entity information.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Tas` | TAS entity information | M | |

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
`Returns the password used for TAS enrollment.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | TAS enrollment password | M | |

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
`Returns the current enrollment status of the TAS.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `TasStatus` | TAS enrollment status | M | PENDING or COMPLETED |

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
`Returns the DID identifier of the TAS.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
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
`Validates the server token against the given transaction ID. Throws an exception if the token is invalid.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| serverToken | `String` | Server token to validate | M | |
| txId | `String` | Transaction ID | M | |

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
`Retrieves a Transaction object by its external transaction ID (txId).`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Transaction` | Retrieved transaction | M | |

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
`Checks whether a transaction with the given txId exists.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the transaction exists | M | |

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
`Finds a user by their DID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | User DID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `User` | Retrieved user | M | |

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
`Checks whether a user with the given DID is already registered.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | User DID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the user exists | M | |

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
`Checks whether a user with the given PII (CI) is already registered.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| pii | `String` | PII (CI) value to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the user exists | M | |

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
`Retrieves the authNonce from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | authNonce value | M | |

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
`Retrieves the Issuer's external transaction ID from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Issuer transaction ID | M | |

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
`Retrieves the refId from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | refId value | M | |

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
`Retrieves the Issuer DID from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves Holder information required for generating an Issue Profile from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `HolderInfo` | Holder information | M | Includes holderDid, pii, etc. |

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
`Retrieves the FCM Push Token for the given Holder DID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| holderDid | `String` | Holder DID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves VC Plan information by VC Plan ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcPlanId | `String` | VC Plan ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcPlanInfo` | VC Plan information | M | Includes vcPlanId, issuerDid, schemaId, etc. |

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
`Retrieves the Issuer DID from the VC Plan.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcPlanId | `String` | VC Plan ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves VC metadata from the blockchain by VC ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcMeta` | VC metadata | M | |

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
`Retrieves the Issuer DID from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves the Issuer Nonce from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves the VC ID from the transaction.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| txId | `String` | Transaction ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Retrieves the VC schema JSON by schema ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| schemaId | `String` | VC schema ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | VC schema JSON string | M | |

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
`Persists a newly issued VC.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vc | `VerifiableCredential` | VC to store | M | |

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
`Checks whether a VC has been revoked. Throws an exception if it has.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID to check | M | |

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
`Revokes a VC with the given reason.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID to revoke | M | |
| reason | `String` | Revocation reason | M | |

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
`Signs a VC with the TAS private key.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vc | `VerifiableCredential` | VC to sign | M | |
| didDoc | `DidDocument` | TAS DID Document | M | |
| keyPurpose | `String` | Key purpose for signing | M | e.g., "assertionMethod" |

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
`Signs an InvokedDidDoc with the TAS private key.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| unsignedInvokedDidDoc | `InvokedDidDoc` | Unsigned InvokedDidDoc | M | |
| keyId | `String` | Key ID to use for signing | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `InvokedDidDoc` | InvokedDidDoc with TAS signature appended | M | |

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
`Signs arbitrary data with the TAS private key.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| message | `byte[]` | Data to sign | M | |
| keyId | `String` | Key ID to use for signing | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Base64-encoded signature | M | |

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
`Checks the wallet connection status.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the wallet is connected | M | |

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
`Throws an exception if the wallet is not connected.`

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
`Checks whether a wallet with the given walletId is already registered.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletId | `String` | Wallet ID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the walletId exists | M | |

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
`Checks whether a wallet with the given DID is already registered.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | Wallet DID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the DID exists | M | |

#### Function Declaration
```java
boolean checkWalletDidExists(String did)
```
<br>

---

<div style="page-break-after: always;"></div>

# 3. APIs - Config

## 3.1 TaSdkConfig

TaSdkConfig is a container for all settings required to initialize the TA SDK. It holds WalletConfig, CryptoConfig, and StorageConfig.

### 3.1.1 builder

#### Class Name
`TaSdkConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a TaSdkConfig builder.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `TaSdkConfig.Builder` | TaSdkConfig builder instance | M | |

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

WalletConfig manages the file path and password for the file-based wallet.

### 3.2.1 builder

#### Class Name
`WalletConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a WalletConfig builder.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `WalletConfig.Builder` | WalletConfig builder instance | M | |

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

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| filePath | `String` | Wallet file path | M |
| password | `String` | Wallet password | M |

<br>

---

## 3.3 CryptoConfig

CryptoConfig configures the signature algorithm, hash algorithm, and key size. Default values can be used without any explicit configuration.

### 3.3.1 builder

#### Class Name
`CryptoConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a CryptoConfig builder.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `CryptoConfig.Builder` | CryptoConfig builder instance | M | |

#### Function Declaration
```java
static CryptoConfig.Builder builder()
```

#### Function Usage
```java
// Use defaults (Secp256r1, SHA-256, 256-bit)
CryptoConfig config = new CryptoConfig();

// Custom configuration
CryptoConfig config = CryptoConfig.builder()
    .signatureAlgorithm("Secp256r1")
    .hashAlgorithm("SHA-256")
    .keySize(256)
    .build();
```

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| signatureAlgorithm | `String` | `"Secp256r1"` | Signature algorithm |
| hashAlgorithm | `String` | `"SHA-256"` | Hash algorithm |
| keySize | `int` | `256` | Key size in bits |

<br>

---

## 3.4 StorageConfig

StorageConfig specifies the storage type (blockchain or LSS) for persisting DID Documents and VC metadata.

### 3.4.1 forBlockchain

#### Class Name
`StorageConfig`

#### Function Name
`forBlockchain`

#### Function Introduction
`Creates a blockchain storage configuration.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| blockChainConfig | `BlockChainConfig` | Blockchain connection configuration | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `StorageConfig` | Blockchain storage configuration instance | M | |

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
`Creates an LSS (Ledger Service Server) storage configuration.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| lssConfig | `LssConfig` | LSS connection configuration | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `StorageConfig` | LSS storage configuration instance | M | |

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
`Checks the storage type.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the storage type matches | M | |

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

Defines the delivery method for Offers in the P142 DID Document restore protocol.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Description |
|-------|-------------|
| `PUSH` | Delivers the Offer via push notification |
| `EMAIL` | Delivers the Offer via email |

**Usage**:
```java
ProposeRestoreDidDocRequest request = new ProposeRestoreDidDocRequest();
request.setOfferType(OfferType.PUSH);
```
<br>

## 4.2 ServerTokenPurpose

Defines the purpose of a server token, distinguished by a numeric purpose value.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Purpose | Description |
|-------|---------|-------------|
| `CREATE_DID` | 5 | DID creation |
| `UPDATE_DID` | 6 | DID update |
| `RESTORE_DID` | 7 | DID restore |
| `ISSUE_VC` | 8 | VC issuance |
| `REVOKE_VC` | 9 | VC revocation |
| `PRESENT_VP` | 10 | VP presentation |
| `CREATE_DID_AND_ISSUE_VC` | 13 | Combined DID creation + VC issuance |

**Methods**:
- `Integer getPurpose()` — Returns the numeric purpose value
<br>

## 4.3 TasStatus

Represents the enrollment status of the TAS.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Description |
|-------|-------------|
| `PENDING` | TAS is configured but not yet enrolled |
| `COMPLETED` | TAS enrollment has been successfully completed |

**State transition**: `PENDING` → (enrollTas succeeds) → `COMPLETED`
<br>

## 4.4 UserStatus

Represents the current state of a user.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Description |
|-------|-------------|
| `ACTIVATED` | Active user |
| `DEACTIVATED` | Temporarily deactivated user (restorable) |
| `REVOKED` | Permanently revoked user |
| `TERMINATED` | Withdrawn user |

**Note**: P141 (DID Doc update) requires `ACTIVATED` status; P142 (DID Doc restore) requires `DEACTIVATED` status.
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
`Creates an SDK exception from a TaSdkErrorCode.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |

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
`Creates an SDK exception from a TaSdkErrorCode and a cause.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| cause | `Throwable` | Cause exception | M | |

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
`Creates an SDK exception from a TaSdkErrorCode and a custom message.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| message | `String` | Additional message | M | |

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
`Creates an SDK exception from a TaSdkErrorCode, a custom message, and a cause.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| message | `String` | Additional message | M | |
| cause | `Throwable` | Cause exception | M | |

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
`Returns the TaSdkErrorCode of this exception.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `TaSdkErrorCode` | Error code | M | |

#### Function Declaration
```java
TaSdkErrorCode getErrorCode()
```
<br>

---

## 5.2 TaSdkErrorCode

Defines all error codes for the TA SDK. Error code format: `SSDKTRA{category}{number}`

**Package**: `org.omnione.did.tas.exception`

### getCode / getMessage / getHttpStatus

#### Function Declaration
```java
String getCode()       // Returns the error code string (e.g., "SSDKTRA001001")
String getMessage()    // Returns the error message
int getHttpStatus()    // Returns the HTTP status code
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

### Error Code List

#### SDK Common Validation Errors (SSDKTRA001xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_INVALID_DID` | SSDKTRA001001 | 400 | Invalid DID format |
| `SDK_INVALID_PROOF` | SSDKTRA001002 | 400 | Invalid proof |
| `SDK_INVALID_SIGNATURE` | SSDKTRA001003 | 400 | Invalid signature |
| `SDK_INVALID_MULTIBASE` | SSDKTRA001004 | 400 | Invalid multibase encoding |
| `SDK_FIELD_REQUIRED` | SSDKTRA001005 | 400 | Required field is missing |
| `SDK_INVALID_TRANSACTION` | SSDKTRA001006 | 400 | Invalid transaction |
| `SDK_INVALID_PARAMETER` | SSDKTRA001007 | 400 | Invalid parameter |
| `SDK_INVALID_CONFIGURATION` | SSDKTRA001008 | 500 | Invalid configuration |

#### SDK Protocol Errors (SSDKTRA002xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_INVALID_PROTOCOL_STEP` | SSDKTRA002001 | 400 | Invalid protocol step |
| `SDK_INVALID_TRANSACTION_STATUS` | SSDKTRA002002 | 400 | Invalid transaction status |
| `SDK_INVALID_SUB_TRANSACTION_STATUS` | SSDKTRA002003 | 400 | Invalid sub-transaction status |
| `SDK_TRANSACTION_EXPIRED` | SSDKTRA002004 | 400 | Transaction has expired |
| `SDK_TRANSACTION_NOT_FOUND` | SSDKTRA002005 | 404 | Transaction not found |

#### SDK Crypto Errors (SSDKTRA003xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_SIGNATURE_GENERATION_FAILED` | SSDKTRA003001 | 500 | Failed to generate signature |
| `SDK_SIGNATURE_VERIFICATION_FAILED` | SSDKTRA003002 | 400 | Failed to verify signature |
| `SDK_ECDH_KEY_AGREEMENT_FAILED` | SSDKTRA003003 | 500 | ECDH key agreement failed |
| `SDK_ENCRYPTION_FAILED` | SSDKTRA003004 | 500 | Encryption failed |
| `SDK_DECRYPTION_FAILED` | SSDKTRA003005 | 500 | Decryption failed |
| `SDK_VC_ISSUANCE_FAILED` | SSDKTRA003006 | 500 | Failed to issue VC |

#### SDK Blockchain Errors (SSDKTRA004xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_DID_DOCUMENT_NOT_FOUND` | SSDKTRA004001 | 404 | DID Document not found |
| `SDK_DID_DOCUMENT_REGISTRATION_FAILED` | SSDKTRA004002 | 500 | Failed to register DID Document |
| `SDK_DID_DOCUMENT_UPDATE_FAILED` | SSDKTRA004003 | 500 | Failed to update DID Document |
| `SDK_VC_META_NOT_FOUND` | SSDKTRA004004 | 404 | VC metadata not found |
| `SDK_BLOCKCHAIN_CONNECTION_FAILED` | SSDKTRA004005 | 500 | Blockchain connection failed |
| `SDK_VC_REVOKED` | SSDKTRA004006 | 400 | VC has been revoked |

#### SDK Wallet Errors (SSDKTRA005xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_WALLET_CONNECTION_FAILED` | SSDKTRA005001 | 500 | Failed to connect to wallet |
| `SDK_WALLET_NOT_CONNECTED` | SSDKTRA005002 | 500 | Wallet is not connected |
| `SDK_WALLET_CREATION_FAILED` | SSDKTRA005003 | 500 | Failed to create wallet |
| `SDK_KEY_NOT_FOUND` | SSDKTRA005004 | 404 | Key not found in wallet |
| `SDK_KEY_GENERATION_FAILED` | SSDKTRA005005 | 500 | Failed to generate key |

#### SDK Provider Errors (SSDKTRA006xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_USER_NOT_FOUND` | SSDKTRA006001 | 404 | User not found |
| `SDK_ENTITY_NOT_FOUND` | SSDKTRA006002 | 404 | Entity not found |
| `SDK_WALLET_INFO_NOT_FOUND` | SSDKTRA006003 | 404 | Wallet information not found |
| `SDK_ECDH_SESSION_NOT_FOUND` | SSDKTRA006004 | 404 | ECDH session not found |
| `SDK_TOKEN_NOT_FOUND` | SSDKTRA006005 | 404 | Token not found |
| `SDK_VC_SCHEMA_NOT_FOUND` | SSDKTRA006006 | 404 | VC schema not found |

#### P110 TAS Enrollment Protocol Errors (SSDKTRA011xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `PASSWORD_MISMATCH` | SSDKTRA011001 | 400 | Password mismatch |
| `TAS_DID_DOCUMENT_NOT_FOUND` | SSDKTRA011003 | 404 | TAS DID Document not found |
| `CERTIFICATE_VC_GENERATION_FAILED` | SSDKTRA011004 | 500 | Failed to generate Certificate VC |
| `CERTIFICATE_VC_SIGNING_FAILED` | SSDKTRA011005 | 500 | Failed to sign Certificate VC |
| `CERTIFICATE_VC_PUBLISHING_FAILED` | SSDKTRA011006 | 500 | Failed to publish Certificate VC |
| `VC_META_REGISTRATION_FAILED` | SSDKTRA011007 | 500 | Failed to register VcMeta to blockchain |
| `TAS_ENROLLMENT_FAILED` | SSDKTRA011008 | 500 | Failed to enroll TAS |
| `TAS_PASSWORD_NOT_CONFIGURED` | SSDKTRA011009 | 500 | TAS password not configured |

#### P131 Wallet Registration Protocol Errors (SSDKTRA013xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `WALLET_PROVIDER_NOT_REGISTERED` | SSDKTRA013001 | 400 | Wallet Provider not registered |
| `ENTITY_NOT_COMPLETED` | SSDKTRA013002 | 400 | Entity is not in COMPLETED status |
| `CERTIFICATE_VC_NOT_FOUND` | SSDKTRA013003 | 404 | Certificate VC not found |
| `PROVIDER_DID_MISMATCH` | SSDKTRA013004 | 400 | Provider DID mismatch in Certificate VC |
| `INVALID_CERTIFICATE_VC_ISSUER` | SSDKTRA013005 | 400 | Invalid Certificate VC issuer or VC expired |
| `ATTESTATION_VERIFICATION_FAILED` | SSDKTRA013006 | 400 | Attestation signature verification failed |
| `WALLET_ID_ALREADY_EXISTS` | SSDKTRA013007 | 400 | WalletId already exists |
| `DID_DOCUMENT_KEY_VERIFICATION_FAILED` | SSDKTRA013008 | 400 | DID Document key signature verification failed |
| `INVOKED_DID_DOC_CREATION_FAILED` | SSDKTRA013009 | 500 | Failed to create InvokedDidDoc |
| `DID_DOCUMENT_REGISTRATION_FAILED` | SSDKTRA013010 | 500 | Failed to register DID Document to blockchain |

#### P132 User Registration Protocol Errors (SSDKTRA032xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `WALLET_INFO_PROOF_VERIFICATION_FAILED` | SSDKTRA032001 | 400 | WalletInfo proof signature verification failed |
| `CA_APP_INFO_PROOF_VERIFICATION_FAILED` | SSDKTRA032002 | 400 | CaAppInfo proof signature verification failed |
| `SERVER_TOKEN_DATA_SIGNING_FAILED` | SSDKTRA032003 | 500 | Failed to sign ServerTokenData |
| `SERVER_TOKEN_ENCRYPTION_FAILED` | SSDKTRA032004 | 500 | Failed to encrypt server token data |
| `SERVER_TOKEN_INVALID` | SSDKTRA032005 | 400 | Server token is invalid or expired |
| `KYC_DATA_RETRIEVAL_FAILED` | SSDKTRA032006 | 500 | Failed to retrieve KYC data |
| `KYC_TOKEN_AND_TXID_BOTH_MISSING` | SSDKTRA032007 | 400 | Either kycToken or kycTxId must be provided |
| `USER_DID_ALREADY_EXISTS` | SSDKTRA032008 | 400 | User DID already registered |
| `USER_PII_ALREADY_EXISTS` | SSDKTRA032009 | 400 | User with same PII already registered |
| `SIGNED_DID_DOC_WALLET_PROOF_FAILED` | SSDKTRA032010 | 400 | SignedDidDoc wallet proof verification failed |
| `OWNER_DID_DOC_KEY_PROOF_FAILED` | SSDKTRA032011 | 400 | OwnerDidDoc key proof verification failed |
| `USER_VC_GENERATION_FAILED` | SSDKTRA032012 | 500 | Failed to generate User VC |
| `USER_VC_META_REGISTRATION_FAILED` | SSDKTRA032013 | 500 | Failed to register User VC metadata to blockchain |

#### P141 DID Document Update Protocol Errors (SSDKTRA017xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `USER_NOT_FOUND` | SSDKTRA017002 | 404 | User not found |
| `USER_NOT_ACTIVATED` | SSDKTRA017003 | 400 | User status must be ACTIVATED |
| `PROPOSE_UPDATE_DIDDOC_FAILED` | SSDKTRA017011 | 500 | Failed to propose update DID Document |
| `REQUEST_UPDATE_DIDDOC_FAILED` | SSDKTRA017012 | 500 | Failed to request update DID Document |
| `CONFIRM_UPDATE_DIDDOC_FAILED` | SSDKTRA017013 | 500 | Failed to confirm update DID Document |
| `AUTH_NONCE_MISMATCH` | SSDKTRA016520 | 400 | AuthNonce mismatch |
| `DID_DOC_KEY_VERIFICATION_FAILED` | SSDKTRA016516 | 400 | DID Document key proof verification failed |
| `NONCE_GENERATION_FAILED` | SSDKTRA017020 | 500 | Failed to generate nonce |
| `MULTIBASE_DECODE_FAILED` | SSDKTRA017021 | 400 | Failed to decode multibase |
| `PROOF_MISSING` | SSDKTRA017022 | 400 | Proof is missing |
| `INVALID_VERIFICATION_METHOD` | SSDKTRA017023 | 400 | Invalid verification method |
| `INVALID_PROOF_PURPOSE` | SSDKTRA017024 | 400 | Invalid proof purpose |
| `DID_MISMATCH` | SSDKTRA017025 | 400 | DID mismatch |
| `DID_DOCUMENT_NOT_FOUND` | SSDKTRA017026 | 404 | DID Document not found |
| `PUBLIC_KEY_NOT_FOUND` | SSDKTRA017027 | 404 | Public key not found |

#### P142 DID Document Restore Protocol Errors (SSDKTRA020xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `USER_NOT_FOUND_FOR_RESTORE` | SSDKTRA020002 | 404 | User not found for restore |
| `USER_NOT_DEACTIVATED` | SSDKTRA020003 | 400 | User status must be DEACTIVATED |
| `TRANSACTION_NOT_FOUND_FOR_RESTORE` | SSDKTRA020004 | 404 | Transaction not found for restore |
| `OFFER_CREATION_FAILED` | SSDKTRA020005 | 500 | Failed to create offer |
| `PUSH_SEND_FAILED` | SSDKTRA020006 | 500 | Failed to send push notification |
| `EMAIL_SEND_FAILED` | SSDKTRA020007 | 500 | Failed to send email |
| `OFFER_NOT_FOUND` | SSDKTRA020008 | 404 | Offer not found |
| `OFFER_CODE_MISMATCH` | SSDKTRA020009 | 400 | Offer code mismatch |
| `OFFER_EXPIRED` | SSDKTRA020010 | 400 | Offer has expired |
| `OFFER_ALREADY_USED` | SSDKTRA020011 | 400 | Offer has already been used |
| `DEACTIVATED_FLAG_NOT_FALSE` | SSDKTRA020012 | 400 | Deactivated flag must be false for restore |
| `OFFER_TRANSACTION_MISMATCH` | SSDKTRA020013 | 400 | Offer transaction ID mismatch |
| `INVALID_OFFER_TYPE` | SSDKTRA020014 | 400 | Invalid offer type |
| `OFFER_SEND_FAILED` | SSDKTRA020015 | 500 | Failed to send offer |

#### P210 VC Issuance Protocol Errors (SSDKTRA021xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `VC_ISSUANCE_TRANSACTION_NOT_FOUND` | SSDKTRA021001 | 404 | Transaction not found |
| `VC_ISSUANCE_TRANSACTION_INVALID_STATE` | SSDKTRA021002 | 400 | Transaction is in an invalid state |
| `VC_ISSUANCE_TRANSACTION_EXPIRED` | SSDKTRA021003 | 400 | Transaction has expired |
| `VC_ISSUANCE_INVALID_PREVIOUS_STEP` | SSDKTRA021004 | 400 | Invalid previous step |
| `VC_PLAN_NOT_FOUND` | SSDKTRA021005 | 404 | VC Plan not found |
| `VC_PLAN_INVALID` | SSDKTRA021006 | 400 | VC Plan is invalid |
| `ISSUER_NOT_FOUND` | SSDKTRA021007 | 404 | Issuer not found |
| `ISSUER_COMMUNICATION_FAILED` | SSDKTRA021009 | 500 | Issuer communication failed |
| `SERVER_TOKEN_VALIDATION_FAILED` | SSDKTRA021015 | 400 | Server token validation failed |
| `ISSUE_PROFILE_RETRIEVAL_FAILED` | SSDKTRA021016 | 500 | Issue profile retrieval failed |
| `VC_SCHEMA_VALIDATION_FAILED` | SSDKTRA021017 | 400 | VC schema validation failed |
| `VP_DECRYPTION_FAILED` | SSDKTRA021018 | 500 | VP decryption failed |
| `VP_SIGNATURE_VERIFICATION_FAILED` | SSDKTRA021019 | 400 | VP signature verification failed |
| `VC_ISSUANCE_FAILED` | SSDKTRA021020 | 500 | VC issuance failed |
| `VC_ENCRYPTION_FAILED` | SSDKTRA021021 | 500 | VC encryption failed |
| `VC_ID_MISMATCH` | SSDKTRA021022 | 400 | VC ID mismatch |
| `VC_METADATA_REGISTRATION_FAILED` | SSDKTRA021023 | 500 | VC metadata registration failed |
| `HOLDER_NOT_FOUND` | SSDKTRA021024 | 404 | Holder not found |
| `PUSH_TOKEN_NOT_FOUND` | SSDKTRA021025 | 404 | Push token not found |

#### P220 VC Revocation Protocol Errors (SSDKTRA022xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `VC_ALREADY_REVOKED` | SSDKTRA022001 | 400 | VC is already revoked |
| `VC_EXPIRED` | SSDKTRA022002 | 400 | VC is expired |
| `VC_STATUS_INVALID` | SSDKTRA022003 | 400 | VC status is invalid |
| `VC_META_FETCH_FAILED` | SSDKTRA022004 | 500 | Failed to fetch VC metadata from blockchain |
| `ISSUER_NONCE_MISMATCH` | SSDKTRA022005 | 400 | Issuer nonce mismatch |
| `SIGNATURE_INVALID` | SSDKTRA022006 | 400 | Signature is invalid |
| `PROPOSE_REVOKE_VC_FAILED` | SSDKTRA022007 | 500 | Failed to propose revoke VC |
| `REQUEST_REVOKE_VC_FAILED` | SSDKTRA022008 | 500 | Failed to request revoke VC |
| `CONFIRM_REVOKE_VC_FAILED` | SSDKTRA022009 | 500 | Failed to confirm revoke VC |

#### SDK General Errors (SSDKTRA009xxx)

| Error Code | Code Value | HTTP | Description |
|------------|------------|------|-------------|
| `SDK_NOT_INITIALIZED` | SSDKTRA009001 | 500 | SDK not initialized |
| `SDK_UNKNOWN_ERROR` | SSDKTRA009999 | 500 | Unknown SDK error occurred |
