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
- Date: 2026-11-09
- Version: v3.0.0

| Version | Date       | Changes       |
| ------- | ---------- | ------------- |
| v1.0.0  | 2026-03-04 | Initial Draft |
| v3.0.0  | 2026-11-09 | Documented all public APIs and corrected signatures |


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
- [8. Auxiliary Utilities](#8-auxiliary-utilities)
    - [8.1 AuthNonceMatcher](#81-authnoncematcher)
    - [8.2 ClaimManager](#82-claimmanager)

<div style="page-break-after: always;"></div>

# 1. APIs - Protocols

Protocol interfaces are the core API of the TA SDK. Create each `*ProtocolImpl` with the constructor listed in its section (or register it as a bean in your container) and call the protocol methods. The implementations receive Provider implementations through their constructors; `TasEnrollmentProtocolImpl` and `EntityEnrollmentProtocolImpl` also take a `VcBuilder`, and `VcRevocationProtocolImpl` has a no-argument constructor. `VcIssuanceProtocol` and `VcRevocationProtocol` receive the `IssuerApiClient` and their data provider as method arguments. The SDK does not store transactions: values such as txId and authNonce that a method returns must be stored by the caller and supplied again through `TransactionProvider` or the data providers in later steps.

## 1.1 TasEnrollmentProtocol

- Implementation: `TasEnrollmentProtocolImpl` implements `TasEnrollmentProtocol`; its methods are described in this section.
- Constructor: `TasEnrollmentProtocolImpl(TasProvider tasProvider, StorageProvider storageProvider, WalletProvider walletProvider, CertificateProvider certificateProvider, VcBuilder vcBuilder)`
- Common methods: `TasEnrollmentProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.1.1 enrollTas

#### Class Name
`TasEnrollmentProtocol`

#### Function Name
`enrollTas`

#### Function Introduction
`Performs TAS self-enrollment (P110). Compares the request password with TasProvider.getTasPassword(), loads the TAS DID Document that must already be registered (StorageProvider.findDidDoc), builds a Certificate VC with VcBuilder, signs it with the TAS assert key through WalletProvider.signVc, publishes it through CertificateProvider, and registers its VcMeta through StorageProvider. Throws TaSdkException: TAS_PASSWORD_NOT_CONFIGURED, PASSWORD_MISMATCH, TAS_DID_DOCUMENT_NOT_FOUND, CERTIFICATE_VC_GENERATION_FAILED, CERTIFICATE_VC_SIGNING_FAILED, CERTIFICATE_VC_PUBLISHING_FAILED, VC_META_REGISTRATION_FAILED, or TAS_ENROLLMENT_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`EnrollTasRequest`](#721-enrolltasrequest) | TAS enrollment request data | M | `request.password` (nested class `EnrollTasRequest.Request`) |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`EnrollTasResponse`](#723-enrolltasresponse) | TAS enrollment response | M | Includes certVcRef (Certificate VC URL). The SDK does not set txId |

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

- Implementation: `EntityEnrollmentProtocolImpl` implements `EntityEnrollmentProtocol`; its methods are described in this section.
- Constructor: `EntityEnrollmentProtocolImpl(TransactionProvider transactionProvider, EntityProvider entityProvider, EcdhSessionProvider ecdhSessionProvider, StorageProvider storageProvider, WalletProvider walletProvider, DidDocumentProvider didDocumentProvider, CertificateProvider certificateProvider, VcBuilder vcBuilder)`
- Common methods: `EntityEnrollmentProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.2.1 proposeEnrollEntity

#### Class Name
`EntityEnrollmentProtocol`

#### Function Name
`proposeEnrollEntity`

#### Function Introduction
`Entity enrollment protocol Step 1 (P120). Generates a UUID txId and a 16-byte multibase-encoded authNonce. The SDK does not store them; the caller must store them so that TransactionProvider.getTransaction(txId) returns the authNonce in Step 3.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeEnrollEntityRequest`](#724-proposeenrollentityrequest) | Entity enrollment proposal request | M | Has only id; the SDK does not read it |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeEnrollEntityResponse`](#725-proposeenrollentityresponse) | Enrollment proposal response | M | Includes txId, authNonce |

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
`Entity enrollment protocol Step 3 (P120). Loads the transaction (TransactionProvider), verifies DID Auth (DID format, verificationMethod, authNonce, signature), loads the entity (EntityProvider), builds and signs a Certificate VC, registers its VcMeta, publishes it, and encrypts it with the ECDH session key, cipher, and padding looked up by txId (EcdhSessionProvider). Throws TaSdkException such as SDK_TRANSACTION_NOT_FOUND, SDK_INVALID_DID, SDK_INVALID_PROOF, SDK_SIGNATURE_VERIFICATION_FAILED, SDK_ENTITY_NOT_FOUND, CERTIFICATE_VC_SIGNING_FAILED, SDK_ENCRYPTION_FAILED, or SDK_UNKNOWN_ERROR.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestEnrollEntityRequest`](#726-requestenrollentityrequest) | Entity enrollment request data | M | Includes txId, didAuth, dn (optional; the entity's dn is used if empty) |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestEnrollEntityResponse`](#727-requestenrollentityresponse) | Enrollment request response | M | Includes txId, iv, encVc, vcUrl, and vcId (vcId is internal to the server) |

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
`Entity enrollment protocol Step 4 (P120). Returns the txId only. The SDK does not check the transaction state or the vcId; the caller must check them before calling this method.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmEnrollEntityRequest`](#728-confirmenrollentityrequest) | Entity enrollment confirmation request | M | Includes txId, vcId |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmEnrollEntityResponse`](#729-confirmenrollentityresponse) | Enrollment confirmation response | M | Includes txId |

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

- Implementation: `WalletRegistrationProtocolImpl` implements `WalletRegistrationProtocol`; its methods are described in this section.
- Constructor: `WalletRegistrationProtocolImpl(EntityProvider entityProvider, WalletRegistrationProvider walletRegistrationProvider, StorageProvider storageProvider, DidDocumentProvider didDocumentProvider, CertificateProvider certificateProvider, WalletProvider walletProvider)`
- Common methods: `WalletRegistrationProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.3.1 registerWallet

#### Class Name
`WalletRegistrationProtocol`

#### Function Name
`registerWallet`

#### Function Introduction
`Registers a Wallet DID Document (P131). Checks that the Wallet Provider in attestedDidDoc.provider is a registered WALLET_PROVIDER entity in a valid state (EntityProvider), validates its Certificate VC when certVcRef is present, verifies the provider's assertionMethod proof on the AttestedDidDoc, rejects a duplicate walletId, verifies the owner DID Document key proofs, and registers the TAS-signed InvokedDidDoc with RoleType.WALLET. Throws TaSdkException such as SDK_FIELD_REQUIRED, WALLET_PROVIDER_NOT_REGISTERED, ATTESTATION_VERIFICATION_FAILED, WALLET_ID_ALREADY_EXISTS, DID_DOCUMENT_KEY_VERIFICATION_FAILED, INVOKED_DID_DOC_CREATION_FAILED, or DID_DOCUMENT_REGISTRATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RegisterWalletRequest`](#764-registerwalletrequest) | Wallet registration request data | M | Includes attestedDidDoc (`AttestedDidDoc`: walletId, ownerDidDoc, provider, nonce, proof) |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RegisterWalletResponse`](#765-registerwalletresponse) | Wallet registration response | M | Includes walletDid. The SDK does not set txId |

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

- Implementation: `UserRegistrationProtocolImpl` implements `UserRegistrationProtocol`; its methods are described in this section.
- Constructor: `UserRegistrationProtocolImpl(TokenProvider tokenProvider, KycProvider kycProvider, UserProvider userProvider, DidDocumentProvider didDocumentProvider, WalletProvider walletProvider, StorageProvider storageProvider, CertificateProvider certificateProvider)`
- Common methods: `UserRegistrationProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.4.1 proposeRegisterUser

#### Class Name
`UserRegistrationProtocol`

#### Function Name
`proposeRegisterUser`

#### Function Introduction
`User registration protocol Step 1 (P132). Generates a UUID txId. The SDK does not store the transaction; the caller stores it.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeRegisterUserRequest`](#730-proposeregisteruserrequest) | User registration proposal request | M | The SDK does not read the request |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeRegisterUserResponse`](#731-proposeregisteruserresponse) | Registration proposal response | M | Includes txId |

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
`User registration protocol Step 4 (P132). Validates the serverToken (TokenProvider) and retrieves PII with KycProvider.getPiiFromKycToken when kycToken is present, otherwise with KycProvider.retrieveKycData(kycTxId). Throws TaSdkException KYC_TOKEN_AND_TXID_BOTH_MISSING when both are empty and KYC_DATA_RETRIEVAL_FAILED when the provider returns null.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RetrieveKycRequest`](#732-retrievekycrequest) | KYC retrieval request | M | Includes txId, serverToken, and kycToken or kycTxId (kycToken takes precedence) |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RetrieveKycResponse`](#733-retrievekycresponse) | KYC retrieval response | M | Includes txId, piiData. piiData is for the server to store with the txId |

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
`User registration protocol Step 5 (P132). Validates the serverToken, decodes the owner DID Document and verifies its key proofs, rejects a DID for which UserProvider.existsUserByDid returns true, verifies the wallet's assertionMethod proof on the SignedDidDoc, and registers the TAS-signed InvokedDidDoc with RoleType.ETC. Throws TaSdkException such as SDK_INVALID_MULTIBASE, OWNER_DID_DOC_KEY_PROOF_FAILED, USER_DID_ALREADY_EXISTS, SIGNED_DID_DOC_WALLET_PROOF_FAILED, INVOKED_DID_DOC_CREATION_FAILED, or DID_DOCUMENT_REGISTRATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestRegisterUserRequest`](#734-requestregisteruserrequest) | User registration request data | M | Includes txId, serverToken, signedDidDoc |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestRegisterUserResponse`](#735-requestregisteruserresponse) | Registration request response | M | Includes txId |

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
`User registration protocol Step 6 (P132). Validates the serverToken and returns the txId.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmRegisterUserRequest`](#736-confirmregisteruserrequest) | User registration confirmation request | M | Includes txId, serverToken |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmRegisterUserResponse`](#737-confirmregisteruserresponse) | Registration confirmation response | M | Includes txId |

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

- Implementation: `DidDocUpdateProtocolImpl` implements `DidDocUpdateProtocol`; its methods are described in this section.
- Constructor: `DidDocUpdateProtocolImpl(TokenProvider tokenProvider, UserProvider userProvider, DidDocumentProvider didDocumentProvider, WalletProvider walletProvider, StorageProvider storageProvider, CertificateProvider certificateProvider, TransactionProvider transactionProvider)`
- Common methods: `DidDocUpdateProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.5.1 proposeUpdateDidDoc

#### Class Name
`DidDocUpdateProtocol`

#### Function Name
`proposeUpdateDidDoc`

#### Function Introduction
`DID Document update protocol Step 1 (P141). Generates a UUID txId and a 16-byte multibase-encoded authNonce. The SDK neither checks the user status nor stores the transaction: the server checks that the user is ACTIVATED before the call and stores txId, did, and authNonce for Step 4.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeUpdateDidDocRequest`](#738-proposeupdatediddocrequest) | DID Document update proposal request | M | Includes did. The SDK does not read it |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeUpdateDidDocResponse`](#739-proposeupdatediddocresponse) | Update proposal response | M | Includes txId, authNonce |

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
`DID Document update protocol Step 4 (P141). Validates the serverToken, verifies the key proofs of the updated DID Document and the wallet proof on the SignedDidDoc, verifies DID Auth against the authNonce and DID stored for the txId (TransactionProvider), checks that versionId is the current version + 1, that updated is later than the current value, and that @context, id, controller, created, and deactivated are unchanged, then registers the TAS-signed InvokedDidDoc with RoleType.ETC. Throws TaSdkException such as AUTH_NONCE_MISMATCH, DID_MISMATCH, INVALID_PROOF_PURPOSE, SDK_SIGNATURE_VERIFICATION_FAILED, INVALID_DID_DOC_VERSION, INVALID_DID_DOC_UPDATED, or SDK_DID_DOCUMENT_REGISTRATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestUpdateDidDocRequest`](#740-requestupdatediddocrequest) | DID Document update request data | M | Includes txId, serverToken, signedDidDoc, didAuth |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestUpdateDidDocResponse`](#741-requestupdatediddocresponse) | Update request response | M | Includes txId |

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
`DID Document update protocol Step 5 (P141). Validates the serverToken and returns the txId.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmUpdateDidDocRequest`](#742-confirmupdatediddocrequest) | DID Document update confirmation request | M | Includes txId, serverToken |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmUpdateDidDocResponse`](#743-confirmupdatediddocresponse) | Update confirmation response | M | Includes txId |

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

- Implementation: `DidDocRestoreProtocolImpl` implements `DidDocRestoreProtocol`; its methods are described in this section.
- Constructor: `DidDocRestoreProtocolImpl(TokenProvider tokenProvider, TransactionProvider transactionProvider, DidDocumentProvider didDocumentProvider, StorageProvider storageProvider)`
- Common methods: `DidDocRestoreProtocol` extends `Protocol`. For `getProtocolCode()`, `getProtocolName()`, and `getVersion()`, see [1.9 Protocol](#19-protocol).

### 1.6.1 proposeRestoreDidDoc

#### Class Name
`DidDocRestoreProtocol`

#### Function Name
`proposeRestoreDidDoc`

#### Function Introduction
`DID Document restore protocol Step 1 (P142). Generates a UUID txId and a 16-byte multibase-encoded authNonce. The SDK neither checks the user status nor stores the transaction: the server checks that the user is DEACTIVATED before the call and stores txId, did, and authNonce for Step 4.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeRestoreDidDocRequest`](#744-proposerestorediddocrequest) | DID Document restore proposal request | M | Includes did, offerId (optional). The SDK does not read them |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeRestoreDidDocResponse`](#745-proposerestorediddocresponse) | Restore proposal response | M | Includes txId, authNonce |

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
`DID Document restore protocol Step 4 (P142). Validates the serverToken, verifies DID Auth against the authNonce and DID stored for the txId (TransactionProvider), and calls StorageProvider.updateDidDocumentStatus(did, "ACTIVATED"). Throws TaSdkException such as AUTH_NONCE_MISMATCH, DID_MISMATCH, INVALID_PROOF_PURPOSE, SDK_SIGNATURE_VERIFICATION_FAILED, or REQUEST_RESTORE_DIDDOC_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestRestoreDidDocRequest`](#746-requestrestorediddocrequest) | DID Document restore request data | M | Includes txId, serverToken, didAuth |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestRestoreDidDocResponse`](#747-requestrestorediddocresponse) | Restore request response | M | Includes txId |

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
`DID Document restore protocol Step 5 (P142). Validates the serverToken and returns the txId.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmRestoreDidDocRequest`](#748-confirmrestorediddocrequest) | DID Document restore confirmation request | M | Includes txId, serverToken |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmRestoreDidDocResponse`](#749-confirmrestorediddocresponse) | Restore confirmation response | M | Includes txId |

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

- Implementation: `VcIssuanceProtocolImpl` implements `VcIssuanceProtocol`; its methods are described in this section.
- Constructor: `VcIssuanceProtocolImpl(TokenProvider tokenProvider, DidDocumentProvider didDocumentProvider)`
- Common methods: `VcIssuanceProtocol` does not extend `Protocol`, so it has no `getProtocolCode()`, `getProtocolName()`, or `getVersion()`.

### 1.7.1 proposeIssueVc

#### Class Name
`VcIssuanceProtocol`

#### Function Name
`proposeIssueVc`

#### Function Introduction
`VC issuance protocol Step 2 (P210). Loads the VC Plan (VcIssuanceDataProvider.getVcPlan), uses request.issuer or else the plan's issuerDid, calls IssuerApiClient.inspectProposeIssue, and returns a new TAS txId together with the Issuer txId (externalTxId) and refId. The caller stores them for later steps. Throws TaSdkException VC_PLAN_NOT_FOUND, ISSUER_NOT_FOUND, or ISSUER_COMMUNICATION_FAILED (IssuerApiException, or empty txId or refId in the response).`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeIssueVcRequest`](#750-proposeissuevcrequest) | VC issuance proposal request | M | Includes vcPlanId, issuer (optional), offerId (optional) |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeIssueVcResult`](#751-proposeissuevcresult) | VC issuance proposal response | M | Includes txId, externalTxId, refId |

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
`VC issuance protocol Step 5 (P210). Validates the serverToken, gets the Issuer txId and the holder information from VcIssuanceDataProvider, calls IssuerApiClient.generateIssueProfile, and generates a new 16-byte authNonce. The caller stores the authNonce so that VcIssuanceDataProvider.getAuthNonce(txId) returns it in Step 6. Throws TaSdkException HOLDER_NOT_FOUND, ISSUER_COMMUNICATION_FAILED, or ISSUE_PROFILE_RETRIEVAL_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestIssueProfileRequest`](#752-requestissueprofilerequest) | Issue Profile request | M | Includes txId, serverToken |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestIssueProfileResult`](#753-requestissueprofileresult) | Issue Profile response | M | Includes txId, authNonce, profile |

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
`VC issuance protocol Step 6 (P210). Validates the serverToken, verifies DID Auth against VcIssuanceDataProvider.getAuthNonce(txId), and forwards accE2e and encReqVc to IssuerApiClient.issueVc with the Issuer txId. Returns the Issuer's E2E-encrypted VC as received. Throws TaSdkException such as AUTH_NONCE_MISMATCH, SDK_SIGNATURE_VERIFICATION_FAILED, ISSUER_COMMUNICATION_FAILED, VC_ISSUANCE_FAILED (no e2e), or VC_ENCRYPTION_FAILED (empty encVc).`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestIssueVcRequest`](#754-requestissuevcrequest) | VC issuance request data | M | Includes txId, serverToken, didAuth, accE2e, encReqVc |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestIssueVcResult`](#755-requestissuevcresult) | VC issuance request response | M | Includes txId, e2e |

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
`VC issuance protocol Step 7 (P210). Validates the serverToken, checks that vcId is a UUID, and calls IssuerApiClient.completeVc with the Issuer txId. Throws TaSdkException VC_ID_MISMATCH or ISSUER_COMMUNICATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmIssueVcRequest`](#756-confirmissuevcrequest) | VC issuance confirmation request | M | Includes txId, serverToken, vcId (UUID) |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcIssuanceDataProvider` | VC issuance data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmIssueVcResult`](#757-confirmissuevcresult) | VC issuance confirmation response | M | Includes txId |

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

- Implementation: `VcRevocationProtocolImpl` implements `VcRevocationProtocol`; its methods are described in this section.
- Constructor: `VcRevocationProtocolImpl()`
- Common methods: `VcRevocationProtocol` does not extend `Protocol`, so it has no `getProtocolCode()`, `getProtocolName()`, or `getVersion()`.

### 1.8.1 proposeRevokeVc

#### Class Name
`VcRevocationProtocol`

#### Function Name
`proposeRevokeVc`

#### Function Introduction
`VC revocation protocol Step 1 (P220). Loads the VC metadata (VcRevocationDataProvider.getVcMeta), requires the status "ACTIVE", calls IssuerApiClient.inspectProposeRevoke, and returns a new txId with the issuerNonce and authType. The caller stores txId, vcId, and issuerNonce for Step 4. Throws TaSdkException VC_META_FETCH_FAILED, VC_ALREADY_REVOKED, VC_EXPIRED, VC_STATUS_INVALID, ISSUER_NOT_FOUND, or PROPOSE_REVOKE_VC_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ProposeRevokeVcRequest`](#758-proposerevokevcrequest) | VC revocation proposal request | M | Includes vcId |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ProposeRevokeVcResult`](#759-proposerevokevcresult) | VC revocation proposal response | M | Includes txId, issuerNonce, authType |

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
`VC revocation protocol Step 4 (P220). Checks that request.vcId and request.issuerNonce equal VcRevocationDataProvider.getVcId(txId) and getIssuerNonce(txId), checks that request.proof has all fields and the assertionMethod purpose, and calls IssuerApiClient.revokeVc. In this step the SDK verifies neither the proof signature nor the serverToken. Throws TaSdkException VC_ID_MISMATCH, ISSUER_NONCE_MISMATCH, SIGNATURE_INVALID, or REQUEST_REVOKE_VC_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`RequestRevokeVcRequest`](#760-requestrevokevcrequest) | VC revocation request data | M | Includes txId, serverToken, request (`ReqRevokeVc`) |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`RequestRevokeVcResult`](#761-requestrevokevcresult) | VC revocation request response | M | Includes txId |

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
`VC revocation protocol Step 5 (P220). Calls IssuerApiClient.completeRevoke with the request id and the TAS txId and returns the txId. The SDK does not validate the serverToken in this step. Throws TaSdkException CONFIRM_REVOKE_VC_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | [`ConfirmRevokeVcRequest`](#762-confirmrevokevcrequest) | VC revocation confirmation request | M | Includes txId, serverToken (not validated by the SDK) |
| issuerClient | `IssuerApiClient` | Issuer API client | M | |
| dataProvider | `VcRevocationDataProvider` | VC revocation data provider | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| [`ConfirmRevokeVcResult`](#763-confirmrevokevcresult) | VC revocation confirmation response | M | Includes txId |

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

`Protocol` is the common parent interface of `TasEnrollmentProtocol`, `EntityEnrollmentProtocol`, `WalletRegistrationProtocol`, `UserRegistrationProtocol`, `DidDocUpdateProtocol`, and `DidDocRestoreProtocol`. Each of these interfaces implements the three methods below as default methods, so an implementation does not need to override them. `VcIssuanceProtocol` and `VcRevocationProtocol` do not extend `Protocol`.

**Package**: `org.omnione.did.tas.protocol`

| Interface | getProtocolCode() | getProtocolName() | getVersion() |
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
`Returns the protocol code, for example "P110". See the table above for each protocol.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Protocol code | M | |

#### Function Declaration
```java
String getProtocolCode()
```

#### Function Usage
```java
String code = protocol.getProtocolCode(); // "P110" for TasEnrollmentProtocol
```
<br>

### 1.9.2 getProtocolName

#### Class Name
`Protocol`

#### Function Name
`getProtocolName`

#### Function Introduction
`Returns the protocol name, for example "TAS Registration Protocol".`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Protocol name | M | |

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
`Returns the protocol version. All protocols return "1.0".`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Protocol version | M | |

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
| did | `String` | DID identifier to look up | M | When verifying DID Auth and other proofs, the SDK passes the verificationMethod, a DID URL with `#keyId` and possibly `?versionId=`; return the DID Document of that DID |

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
`Persists ECDH session data (session key, merged nonce) after key agreement is complete. TaEcdhService calls it with the client DID; the server associates the session with the current transaction so that the txId-based methods below can find it.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| clientDid | `String` | Client DID | M | |
| sessionKey | `String` | Negotiated ECDH session key (multibase-encoded) | M | |
| mergedNonce | `String` | Merged nonce (clientNonce + serverNonce, multibase-encoded) | M | |

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
| `String` | ECDH session key (multibase-encoded) | M | |

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
| `String` | Negotiated padding scheme (e.g., "PKCS5") | M | |

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
| `Entity` | Retrieved entity information, or null if not found | O | |

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
`Validates that the entity is in a state that allows protocol operations. The server decides which states are valid; the SDK does not assume a specific status. Throws TaSdkException (for example ENTITY_NOT_COMPLETED) if the state is not valid. Used in P131.`

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
| vcMeta | `VcMeta` | VC metadata to register | M | `org.omnione.did.data.model.vc.VcMeta`; includes id, issuer, subject, status, etc. |

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
| `VcMeta` | Retrieved VC metadata | M | `org.omnione.did.data.model.vc.VcMeta`. Throws TaSdkException if not found |

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
| `String` | TAS enrollment password, or null if not configured | O | enrollTas throws TAS_PASSWORD_NOT_CONFIGURED for null or empty |

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
| `HolderInfo` | Holder information | M | Includes did, pii. requestIssueProfile throws HOLDER_NOT_FOUND if null or did is null |

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
| `VcPlanInfo` | VC Plan information | M | Includes vcPlanId, name, issuerDid, vcSchemaId. proposeIssueVc throws VC_PLAN_NOT_FOUND if null |

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

### 2.11.9 getIssuerServerUrl

#### Class Name
`VcIssuanceDataProvider`

#### Function Name
`getIssuerServerUrl`

#### Function Introduction
`Deprecated. Retrieves the Issuer server base URL for an Issuer DID. The SDK protocols do not call this method; use IssuerApiClient.getIssuerServerUrl() instead.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| issuerDid | `String` | Issuer DID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Issuer server base URL | M | |

#### Function Declaration
```java
@Deprecated
String getIssuerServerUrl(String issuerDid) throws TaSdkException
```

#### Function Usage
```java
// Deprecated: prefer issuerClient.getIssuerServerUrl()
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
`Retrieves VC metadata from the blockchain by VC ID.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | VC ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcMeta` | VC metadata | M | `org.omnione.did.tas.model.domain.VcMeta` (vcId, issuer, subject, status, issuedDate, expirationDate). proposeRevokeVc requires status "ACTIVE" |

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
`Signs a VC with the TAS private key. The VC is modified in place: after the call, vc.getProof() returns the proof.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vc | `VerifiableCredential` | VC to sign | M | |
| didDoc | `DidDocument` | TAS DID Document | M | |
| keyPurpose | `String` | Key purpose for signing | M | Wallet key ID, e.g., "assert" |

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
| `String` | Multibase-encoded signature (proofValue) | M | |

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
`Ensures that the wallet is connected. If it is not, tries to reconnect and throws TaSdkException if reconnection fails.`

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

## 2.17 IssuerApiClient

`IssuerApiClient` is the interface the TA SDK uses to call the Issuer server in P210 (VC issuance) and P220 (VC revocation). The SDK only defines the interface: the server implements the HTTP calls (for example with RestTemplate, WebClient, or OpenFeign) and passes the implementation to the `VcIssuanceProtocol` and `VcRevocationProtocol` methods. A method should throw `IssuerApiException` when the call fails or the Issuer returns an error; the protocols convert it to `TaSdkException`. The request and response types are described in [7. Data Models](#7-data-models).

**Package**: `org.omnione.did.tas.client`

### 2.17.1 offerIssueVc

#### Class Name
`IssuerApiClient`

#### Function Name
`offerIssueVc`

#### Function Introduction
`Requests a VC issuance offer from the Issuer (P210 Step 1a QR, 1b push, 1c email). The SDK protocols do not call this method in this version; call it directly if you implement offers.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `OfferIssueVcApiRequest` | Request body for POST /issuer/api/v1/offer-issue-vc | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `OfferIssueVcApiResponse` | Response body of POST /issuer/api/v1/offer-issue-vc | M | Includes offerId, validUntil |

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
`Asks the Issuer to inspect a VC issuance proposal. Called by VcIssuanceProtocol.proposeIssueVc (P210 Step 2). If the response has no txId or refId, the protocol throws ISSUER_COMMUNICATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `InspectIssueProposeApiRequest` | Request body for POST /issuer/api/v1/inspect-propose-issue | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `InspectIssueProposeApiResponse` | Response body of POST /issuer/api/v1/inspect-propose-issue | M | txId and refId are required |

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
`Requests the Issue Profile from the Issuer. Called by VcIssuanceProtocol.requestIssueProfile (P210 Step 5). If profile is null, the protocol throws ISSUE_PROFILE_RETRIEVAL_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `GenerateIssueProfileApiRequest` | Request body for POST /issuer/api/v1/generate-issue-profile | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `GenerateIssueProfileApiResponse` | Response body of POST /issuer/api/v1/generate-issue-profile | M | profile is required |

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
`Requests VC issuance from the Issuer. Called by VcIssuanceProtocol.requestIssueVc (P210 Step 6). If e2e is null, the protocol throws VC_ISSUANCE_FAILED; if e2e.encVc is empty, VC_ENCRYPTION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `IssueVcApiRequest` | Request body for POST /issuer/api/v1/issue-vc | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `IssueVcApiResponse` | Response body of POST /issuer/api/v1/issue-vc | M | e2e and e2e.encVc are required |

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
`Notifies the Issuer that VC issuance is complete. Called by VcIssuanceProtocol.confirmIssueVc (P210 Step 7). The protocol ignores the returned object.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `CompleteVcApiRequest` | Request body for POST /issuer/api/v1/complete-vc | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `CompleteVcApiResponse` | Response body of POST /issuer/api/v1/complete-vc | O | No fields. Ignored by the SDK; null is accepted |

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
`Asks the Issuer to inspect a VC revocation proposal. Called by VcRevocationProtocol.proposeRevokeVc (P220 Step 1). If issuerNonce is empty, the protocol throws PROPOSE_REVOKE_VC_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `InspectProposeRevokeApiRequest` | Request body for POST /issuer/api/v1/inspect-propose-revoke | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `InspectProposeRevokeApiResponse` | Response body of POST /issuer/api/v1/inspect-propose-revoke | M | issuerNonce is required |

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
`Requests VC revocation from the Issuer. Called by VcRevocationProtocol.requestRevokeVc (P220 Step 4). The protocol ignores the returned object.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `RevokeVcApiRequest` | Request body for POST /issuer/api/v1/revoke-vc | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `RevokeVcApiResponse` | Response body of POST /issuer/api/v1/revoke-vc | O | No fields. Ignored by the SDK; null is accepted |

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
`Notifies the Issuer that VC revocation is complete. Called by VcRevocationProtocol.confirmRevokeVc (P220 Step 5) with the TAS txId. The protocol ignores the returned object.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| request | `CompleteRevokeApiRequest` | Request body for POST /issuer/api/v1/complete-revoke | M | See chapter 7 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `CompleteRevokeApiResponse` | Response body of POST /issuer/api/v1/complete-revoke | O | No fields. Ignored by the SDK; null is accepted |

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
`Returns the Issuer server base URL, for example "https://issuer.example.org". The SDK protocols do not call this method.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Issuer server base URL | M | |

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

TaSdkConfig is a container for all settings required to initialize the TA SDK. It holds WalletConfig, CryptoConfig, and StorageConfig. No SDK component reads TaSdkConfig directly: pass `getWalletConfig()` to `FileWalletAdapter` and `getStorageConfig()` to `BlockchainAdapter`.

Fields:

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| walletConfig | `WalletConfig` | Wallet configuration. Set with `walletConfig(WalletConfig walletConfig)`, read with `getWalletConfig()`. IllegalArgumentException if null | M |
| cryptoConfig | `CryptoConfig` | Crypto configuration. Set with `cryptoConfig(CryptoConfig cryptoConfig)`, read with `getCryptoConfig()`. If null, `new CryptoConfig()` (defaults) is used | O |
| storageConfig | `StorageConfig` | Storage configuration. Set with `storageConfig(StorageConfig storageConfig)`, read with `getStorageConfig()`. IllegalArgumentException if null | M |

### 3.1.1 builder

#### Class Name
`TaSdkConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a TaSdkConfig builder. Builder.build() calls the constructor in 3.1.2 and throws IllegalArgumentException if validation fails.`

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

### 3.1.2 TaSdkConfig(WalletConfig, CryptoConfig, StorageConfig)

#### Class Name
`TaSdkConfig`

#### Function Name
`TaSdkConfig`

#### Function Introduction
`Creates a TA SDK configuration directly. Throws IllegalArgumentException if walletConfig or storageConfig is null; uses new CryptoConfig() when cryptoConfig is null.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletConfig | `WalletConfig` | Wallet configuration | M | |
| cryptoConfig | `CryptoConfig` | Crypto configuration | O | Defaults if null |
| storageConfig | `StorageConfig` | Storage configuration | M | |

#### Output Parameters
n/a

#### Function Declaration
```java
TaSdkConfig(WalletConfig walletConfig, CryptoConfig cryptoConfig, StorageConfig storageConfig)
```

#### Function Usage
```java
TaSdkConfig config = new TaSdkConfig(walletConfig, null, storageConfig); // default CryptoConfig
```
<br>

---

## 3.2 WalletConfig

WalletConfig manages the file path and password for the file-based wallet. Pass it to `FileWalletAdapter`.

Fields:

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| filePath | `String` | Wallet file path. Set with `filePath(String filePath)`, read with `getFilePath()`. IllegalArgumentException if null or blank | M |
| password | `String` | Wallet password. Set with `password(String password)`, read with `getPassword()`. IllegalArgumentException if null or empty | M |

### 3.2.1 builder

#### Class Name
`WalletConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a WalletConfig builder. Builder.build() calls the constructor in 3.2.2 and throws IllegalArgumentException if validation fails.`

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
<br>

### 3.2.2 WalletConfig(String, String)

#### Class Name
`WalletConfig`

#### Function Name
`WalletConfig`

#### Function Introduction
`Creates a wallet configuration directly. Throws IllegalArgumentException if filePath is null or blank, or if password is null or empty.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| filePath | `String` | Wallet file path | M | |
| password | `String` | Wallet password | M | |

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

CryptoConfig configures the signature algorithm, hash algorithm, and key size. Default values can be used without any explicit configuration. The values are only checked for being non-empty or positive, not against supported algorithms. The components of this SDK do not read CryptoConfig: the wallet keys are always generated on secp256r1.

Fields:

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| signatureAlgorithm | `String` | Signature algorithm. Default `"Secp256r1"` (`DEFAULT_SIGNATURE_ALGORITHM`). Set with `signatureAlgorithm(String signatureAlgorithm)`, read with `getSignatureAlgorithm()`. IllegalArgumentException if null or blank | O |
| hashAlgorithm | `String` | Hash algorithm. Default `"SHA-256"` (`DEFAULT_HASH_ALGORITHM`). Set with `hashAlgorithm(String hashAlgorithm)`, read with `getHashAlgorithm()`. IllegalArgumentException if null or blank | O |
| keySize | `int` | Key size in bits. Default `256` (`DEFAULT_KEY_SIZE`). Set with `keySize(int keySize)`, read with `getKeySize()`. IllegalArgumentException if not positive | O |

### 3.3.1 builder

#### Class Name
`CryptoConfig`

#### Function Name
`builder`

#### Function Introduction
`Creates a CryptoConfig builder whose fields start with the default values. Builder.build() calls the constructor in 3.3.3 and throws IllegalArgumentException if validation fails.`

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
`Creates a crypto configuration with the default values: "Secp256r1", "SHA-256", 256.`

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
`Creates a crypto configuration with custom values. Throws IllegalArgumentException if signatureAlgorithm or hashAlgorithm is null or blank, or if keySize is not positive.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| signatureAlgorithm | `String` | Signature algorithm (e.g., "Secp256r1") | M | |
| hashAlgorithm | `String` | Hash algorithm (e.g., "SHA-256") | M | |
| keySize | `int` | Key size in bits | M | Positive |

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

StorageConfig specifies the storage type (blockchain or LSS) for persisting DID Documents and VC metadata.

Fields:

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| storageType | `StorageConfig.StorageType` | `BLOCKCHAIN` or `LSS`, set by the factory method. Read with `getStorageType()` | M |
| blockChainConfig | `BlockChainConfig` | Set only by `forBlockchain`; otherwise null. Read with `getBlockChainConfig()` | O |
| lssConfig | `LssConfig` | Set only by `forLss`; otherwise null. Read with `getLssConfig()` | O |

### 3.4.1 forBlockchain

#### Class Name
`StorageConfig`

#### Function Name
`forBlockchain`

#### Function Introduction
`Creates a blockchain storage configuration. The constructors are private, so use this method or forLss. Throws IllegalArgumentException if blockChainConfig is null.`

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
`Creates an LSS (Ledger Service Server) storage configuration. Throws IllegalArgumentException if lssConfig is null.`

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
`Checks the storage type set by forBlockchain or forLss.`

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

Defines the Offer delivery method (push or email) for the P142 DID Document restore protocol. No SDK method currently takes or returns this enum; `ProposeRestoreDidDocRequest` carries only `offerId`.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Description |
|-------|-------------|
| `PUSH` | Delivers the Offer via push notification |
| `EMAIL` | Delivers the Offer via email |

**Usage**:
```java
OfferType offerType = OfferType.valueOf("PUSH");
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
- `Integer getPurpose()` — Returns the numeric purpose value (serialized to JSON as this number through `@JsonValue`)
<br>

## 4.3 TasStatus

Represents the enrollment status of the TAS.

**Package**: `org.omnione.did.tas.model.enums`

| Value | Description |
|-------|-------------|
| `PENDING` | TAS is configured but not yet enrolled |
| `COMPLETED` | TAS enrollment has been successfully completed |

**State transition**: `PENDING` → (enrollTas succeeds) → `COMPLETED`. The SDK does not change this value; the server updates it after `enrollTas` succeeds.
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

**Note**: The SDK does not check user status. Before calling P141 (DID Doc update), the server checks that the user is `ACTIVATED`; before calling P142 (DID Doc restore), that the user is `DEACTIVATED`.
<br>

## 4.5 StorageConfig.StorageType

Storage type of a `StorageConfig`. It is set by `StorageConfig.forBlockchain` or `StorageConfig.forLss`.

**Package**: `org.omnione.did.tas.config` (nested enum of `StorageConfig`)

| Value | Description |
|-------|-------------|
| `BLOCKCHAIN` | Blockchain storage (Hyperledger Fabric or EVM-compatible) |
| `LSS` | LSS (Ledger Service Server) storage |

<br>

---

<div style="page-break-after: always;"></div>

# 5. APIs - Exceptions

## 5.1 TaSdkException

`TaSdkException` extends `RuntimeException`, so it is unchecked. SDK methods report failures with it unless a section states otherwise. `getErrorCode()` returns the error code and `getMessage()` returns the custom message if one was given, otherwise the error code's message.

**Package**: `org.omnione.did.tas.exception`

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

### 5.1.6 TaSdkException(TaSdkErrorCode, Object[])

#### Class Name
`TaSdkException`

#### Function Name
`TaSdkException`

#### Function Introduction
`Creates an SDK exception with an error code and message arguments. The message is the error code's message.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| args | `Object[]` | Message arguments | O | Kept for getArgs() and argsToString() |

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
`Creates an SDK exception with an error code, message arguments, and a cause. The message is the error code's message.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| args | `Object[]` | Message arguments | O | Kept for getArgs() and argsToString() |
| cause | `Throwable` | Cause exception | M | |

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
`Creates an SDK exception with an error code, message arguments, and a custom message.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| args | `Object[]` | Message arguments | O | Kept for getArgs() and argsToString() |
| message | `String` | Exception message | M | Replaces the error code message |

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
`Creates an SDK exception with an error code, message arguments, a custom message, and a cause.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| errorCode | `TaSdkErrorCode` | Error code | M | |
| args | `Object[]` | Message arguments | O | Kept for getArgs() and argsToString() |
| message | `String` | Exception message | M | Replaces the error code message |
| cause | `Throwable` | Cause exception | M | |

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
`Returns the message arguments, or null if the exception was created without them.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Object[]` | Message arguments | O | null if not set |

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
`Returns the message arguments joined with ", ", or null if there are none.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Joined arguments | O | null if args is null or empty |

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
String unknown = TaSdkErrorCode.getMessageByCode("SSDKTRA999999");
// → "Unknown error code: SSDKTRA999999"
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
| `DID_DOCUMENT_PARSING_FAILED` | SSDKTRA012006 | 400 | Failed to parse DID Document |
| `INVALID_DID_DOC_VERSION` | SSDKTRA012009 | 400 | Invalid DID Document version |
| `INVALID_DID_DOC_UPDATED` | SSDKTRA018006 | 400 | Invalid DID Document updated time |
| `INVALID_DID_DOC_CONTEXT` | SSDKTRA018007 | 400 | Invalid DID Document context |
| `INVALID_DID_DOC_ID` | SSDKTRA018008 | 400 | Invalid DID Document id |
| `INVALID_DID_DOC_CONTROLLER` | SSDKTRA018009 | 400 | Invalid DID Document controller |
| `INVALID_DID_DOC_CREATED` | SSDKTRA018014 | 400 | Invalid DID Document created time |
| `INVALID_DID_DOC_DEACTIVATED` | SSDKTRA018015 | 400 | Invalid DID Document deactivated status |
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
| `PROPOSE_RESTORE_DIDDOC_FAILED` | SSDKTRA020021 | 500 | Failed to propose restore DID Document |
| `REQUEST_ISSUE_OFFER_RESTORE_DIDDOC_FAILED` | SSDKTRA020022 | 500 | Failed to request issue offer restore DID Document |
| `VERIFY_OFFER_RESTORE_DIDDOC_FAILED` | SSDKTRA020023 | 500 | Failed to verify offer restore DID Document |
| `REQUEST_RESTORE_DIDDOC_FAILED` | SSDKTRA020024 | 500 | Failed to request restore DID Document |
| `CONFIRM_RESTORE_DIDDOC_FAILED` | SSDKTRA020025 | 500 | Failed to confirm restore DID Document |

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

<br>

---

## 5.3 IssuerApiException

`IssuerApiException` is a checked exception (`extends Exception`) that `IssuerApiClient` implementations throw when a call to the Issuer fails or the Issuer returns an error. The protocols convert it to `TaSdkException`: ISSUER_COMMUNICATION_FAILED in P210, and PROPOSE_REVOKE_VC_FAILED, REQUEST_REVOKE_VC_FAILED, or CONFIRM_REVOKE_VC_FAILED in P220.

**Package**: `org.omnione.did.tas.exception`

### 5.3.1 IssuerApiException(String)

#### Class Name
`IssuerApiException`

#### Function Name
`IssuerApiException`

#### Function Introduction
`Creates an exception with a detail message.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| message | `String` | Detail message | M | |

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
`Creates an exception with a detail message and a cause.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| message | `String` | Detail message | M | |
| cause | `Throwable` | Cause exception | M | |

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

Core components are concrete classes for wallet access, DID Documents, blockchain access, ECDH, signatures, and VC issuance. Except for `VcBuilder`, which `TasEnrollmentProtocolImpl` and `EntityEnrollmentProtocolImpl` take in their constructors, the protocol implementations in chapter 1 do not use these classes; they reach wallets, storage, and DID Documents through Providers. Use these classes to implement Providers such as `WalletProvider` and `StorageProvider`, or call them directly.

## 6.1 FileWalletAdapter

`FileWalletAdapter` is a file-based wallet for the TAS keys. It extends `BaseWalletServiceImpl` of did-common-sdk-server, which provides `createWallet`, `loadWallet`, `generateKey`, and `sign`. When `createWallet` creates a new wallet, four secp256r1 keys with the IDs `assert`, `auth`, `keyagree`, and `invoke` are generated. `KeyManager`, `DidDocumentBuilder`, `InvokedDidDocHandler`, `TaEcdhService`, `TaSignatureService`, and `TaVcService` take an adapter whose wallet has been loaded or created.

**Package**: `org.omnione.did.tas.core`

### 6.1.1 FileWalletAdapter(WalletConfig)

#### Class Name
`FileWalletAdapter`

#### Function Name
`FileWalletAdapter`

#### Function Introduction
`Creates a wallet adapter for the given configuration. The wallet file is not opened until initialize(), loadWallet(), or createWallet() is called. The argument is not checked for null; a null value causes NullPointerException in initialize().`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletConfig | `WalletConfig` | Wallet file path and password | M | See 3.2 |

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
`Opens the existing wallet file at WalletConfig.getFilePath() with WalletConfig.getPassword(). It does not create a wallet; use createWallet for a new one. Throws TaSdkException(SDK_WALLET_CONNECTION_FAILED) if the file cannot be opened.`

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
`Creates a new wallet file and generates the four default TAS keys (assert, auth, keyagree, invoke) on secp256r1; the adapter then uses the new wallet. Inherited from BaseWalletServiceImpl. It uses its arguments, not WalletConfig. Throws CommonSdkException if creation or key generation fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletFilePath | `String` | Path of the wallet file to create | M | |
| password | `String` | Wallet password | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `WalletManagerInterface` | Wallet manager of the created wallet | M | |

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
`Opens an existing wallet file and makes the adapter use it. Inherited from BaseWalletServiceImpl; initialize() calls it with the WalletConfig values. Throws CommonSdkException if the file cannot be opened.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletFilePath | `String` | Path of the wallet file | M | |
| password | `String` | Wallet password | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `WalletManagerInterface` | Wallet manager of the opened wallet | M | |

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
`Generates a key with the given ID in the wallet and returns the same keyId. SECP256R1 selects secp256r1; null or any other value selects secp256k1. Inherited from BaseWalletServiceImpl. Throws CommonSdkException if no wallet is open or generation fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| keyId | `String` | Key ID | M | |
| curveType | `EccCurveType` | Curve | O | null selects secp256k1 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | The keyId argument | M | |

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
`Hashes data with SHA-256 and signs the hash with the key keyId, returning a compact signature. Inherited from BaseWalletServiceImpl. Throws CommonSdkException if no wallet is open, the key does not exist, or signing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| keyId | `String` | Key ID (e.g., "assert") | M | |
| data | `byte[]` | Data to sign | M | Hashed before signing |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `byte[]` | Compact signature | M | |

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
`Returns the underlying WalletManagerInterface for low-level wallet operations. Throws TaSdkException(SDK_NOT_INITIALIZED) if no wallet has been opened or created.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `WalletManagerInterface` | Wallet manager | M | |

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
`Returns the WalletConfig passed to the constructor.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `WalletConfig` | Wallet configuration | O | null if null was passed |

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

`KeyManager` wraps `FileWalletAdapter` to generate TAS keys and sign data, converting `CommonSdkException` to `TaSdkException`. User and entity keys are not managed by the TA SDK.

**Package**: `org.omnione.did.tas.core`

### 6.2.1 KeyManager(FileWalletAdapter)

#### Class Name
`KeyManager`

#### Function Name
`KeyManager`

#### Function Introduction
`Creates a KeyManager that uses the given wallet adapter. The constructor is generated by Lombok @RequiredArgsConstructor and does not check for null.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| walletAdapter | `FileWalletAdapter` | Wallet adapter with an open wallet | M | |

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
`Generates a secp256r1 key whose key ID is purpose (for example "assert", "auth", "keyagree", or "invoke") and returns that key ID. Only null or blank values are rejected; any other string is used as the key ID. Throws TaSdkException SDK_INVALID_PARAMETER for a null or blank purpose and SDK_KEY_GENERATION_FAILED if generation fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| purpose | `String` | Key purpose, used as the key ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Generated key ID (same as purpose) | M | |

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
`Signs data with the key keyId through FileWalletAdapter.sign (SHA-256 hash, compact signature). Throws TaSdkException(SDK_SIGNATURE_GENERATION_FAILED) if signing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| keyId | `String` | Key ID | M | |
| data | `byte[]` | Data to sign | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `byte[]` | Compact signature | M | |

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
`Encodes data as UTF-8 and calls sign(String, byte[]). A null data causes NullPointerException.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| keyId | `String` | Key ID | M | |
| data | `String` | Text to sign | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `byte[]` | Compact signature | M | |

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
`Returns the FileWalletAdapter passed to the constructor.`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `FileWalletAdapter` | Wallet adapter | M | |

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

`DidDocumentBuilder` creates, parses, and verifies DID Documents with `BaseCoreDidUtil` of did-common-sdk-server. Only `createAndSign` uses the wallet adapter.

**Package**: `org.omnione.did.tas.core`

### 6.3.1 DidDocumentBuilder(FileWalletAdapter)

#### Class Name
`DidDocumentBuilder`

#### Function Name
`DidDocumentBuilder`

#### Function Introduction
`Creates a builder that uses the given wallet adapter in createAndSign. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | Wallet adapter | M | Needed only for createAndSign |

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
`Creates a DID Document from every key in the wallet and signs it. Keys named auth, assert, keyagree, and invoke get the purposes authentication, assertionMethod, keyAgreement, and capabilityInvocation; other keys get no purpose. auth, assert, and invoke sign the document; keyagree does not. Throws TaSdkException SDK_NOT_INITIALIZED if no wallet is open and SDK_UNKNOWN_ERROR for other failures.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID of the document | M | e.g., "did:omn:tas" |
| controller | `String` | Controller DID | M | Usually the same as did |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `DidDocument` | Signed DID Document | M | |

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
`Parses a DID Document JSON string. Throws TaSdkException(SDK_INVALID_PARAMETER) if parsing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocJson | `String` | DID Document JSON | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `DidDocument` | Parsed DID Document | M | |

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
`Verifies the proof of every key in the DID Document. Returns normally if all proofs are valid and throws TaSdkException(SDK_INVALID_PROOF) otherwise.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document to verify | M | |

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
`Builds the verification method ID {id}?versionId={versionId}#{keyId}, where keyId is assert, auth, keyagree, or invoke for the proof purpose. Uses versionId "1" if the document has none. Does not check that the key exists in the document.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| proofPurpose | `ProofPurpose` | Proof purpose | M | `org.omnione.did.common.datamodel.enums.did.ProofPurpose` |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Verification method ID | M | e.g., "did:omn:tas?versionId=1#assert" |

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
`Returns the verification method whose id equals keyId or ends with "#" + keyId. Throws TaSdkException(SDK_INVALID_PROOF) if none is found.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| keyId | `String` | Key ID or full verification method ID | M | e.g., "assert" |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VerificationMethod` | Matching verification method | M | |

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
`Returns publicKeyMultibase of the verification method found as in getVerificationMethod. Throws TaSdkException(SDK_INVALID_PROOF) if the method or its public key is not found.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document | M | |
| keyId | `String` | Key ID or full verification method ID | M | e.g., "keyagree" |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `String` | Multibase-encoded public key | M | |

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

`InvokedDidDocHandler` creates the `InvokedDidDoc` that is submitted to the blockchain when a DID Document is registered or updated. TAS is the invoker and signs with its capabilityInvocation (`invoke`) key.

**Package**: `org.omnione.did.tas.core`

### 6.4.1 InvokedDidDocHandler(FileWalletAdapter, DidDocumentProvider)

#### Class Name
`InvokedDidDocHandler`

#### Function Name
`InvokedDidDocHandler`

#### Function Introduction
`Creates a handler that signs with the given wallet adapter and gets the TAS DID Document from the provider. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | Wallet adapter with the TAS invoke key | M | |
| didDocumentProvider | `DidDocumentProvider` | Provider of the TAS DID Document | M | |

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
`Gets the TAS DID Document from DidDocumentProvider.getTasDidDocument(), removes the proof from a copy of ownerDidDoc, builds an InvokedDidDoc with TAS as the invoker and certificateUrl as its Certificate VC reference (proof type Secp256r1Signature2018), and signs it with the wallet key whose ID is the id of the TAS invoke verification method. ownerDidDoc itself is not changed. Throws TaSdkException from the provider as is, and TaSdkException(SDK_SIGNATURE_GENERATION_FAILED) for other failures.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| ownerDidDoc | `DidDocument` | DID Document to register (TAS, entity, or user) | M | |
| certificateUrl | `String` | URL of the TAS Certificate VC | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `InvokedDidDoc` | Signed InvokedDidDoc for blockchain submission | M | |

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

`BlockchainAdapter` registers and queries DID Documents on the blockchain (Hyperledger Fabric or EVM) through `ContractApi` of did-blockchain-sdk-server. Call `initialize()` before any other method. It can be used to implement `StorageProvider` for blockchain storage.

**Package**: `org.omnione.did.tas.core`

### 6.5.1 BlockchainAdapter(StorageConfig)

#### Class Name
`BlockchainAdapter`

#### Function Name
`BlockchainAdapter`

#### Function Introduction
`Creates an adapter for a blockchain StorageConfig. Throws TaSdkException(SDK_INVALID_CONFIGURATION) if the configuration was created with StorageConfig.forLss, and NullPointerException if it is null.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| storageConfig | `StorageConfig` | Storage configuration from StorageConfig.forBlockchain | M | |

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
`Creates the ContractApi from StorageConfig.getBlockChainConfig(). A second call only logs a warning. Throws TaSdkException(SDK_BLOCKCHAIN_CONNECTION_FAILED) if the connection cannot be created.`

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
`Registers the InvokedDidDoc on the blockchain for the given role. Throws TaSdkException SDK_NOT_INITIALIZED before initialize() and SDK_DID_DOCUMENT_REGISTRATION_FAILED if the blockchain call fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| invokedDidDoc | `InvokedDidDoc` | TAS-signed DID Document | M | See 6.4 |
| roleType | `RoleType` | Role of the DID owner | M | e.g., TAS, WALLET, ETC |

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
`Retrieves the DID Document of did from the blockchain. Throws TaSdkException SDK_NOT_INITIALIZED before initialize() and SDK_DID_DOCUMENT_NOT_FOUND if it is not found or the blockchain call fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID to look up | M | |

#### Output Parameters
| Type | Description | M/O | Note |
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
`Changes the status of a DID Document on the blockchain. Unlike StorageProvider.updateDidDocumentStatus, the status is the DidDocStatus enum. Throws TaSdkException SDK_NOT_INITIALIZED before initialize() and SDK_DID_DOCUMENT_UPDATE_FAILED if the blockchain call fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID whose status to change | M | |
| status | `DidDocStatus` | New status | M | ACTIVATED, DEACTIVATED, REVOKED, TERMINATED |

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
`Returns true if getDidDocument(did) succeeds and false if it throws TaSdkException. Throws TaSdkException(SDK_NOT_INITIALIZED) before initialize().`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | DID to check | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `boolean` | Whether the DID Document exists | M | |

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
`Returns the ContractApi for operations this class does not cover. Throws TaSdkException(SDK_NOT_INITIALIZED) before initialize().`

#### Input Parameters
n/a

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `ContractApi` | Blockchain contract API | M | |

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

`TaEcdhService` performs ECDH key agreement for TAS and signs the response. It extends `BaseEcdhServiceImpl` of did-common-sdk-server. It gets the client's `keyagree` public key with `DidDocumentProvider.getDidDocument(clientDid)` and saves the session with `EcdhSessionProvider.saveEcdhSession`. The inherited methods `processEcdhRequest(EcdhReqData, SymmetricCipherType, SymmetricPaddingType)`, `verifyEcdhRequest(EcdhReqData, String)`, and `generateSessionKey(EcdhReqData, byte[], byte[], SymmetricCipherType)` are also public, but `processEcdhRequest` returns an `AccEcdh` without the server DID and proof. Use `processAndSign`.

**Package**: `org.omnione.did.tas.core`

### 6.6.1 TaEcdhService(FileWalletAdapter, DidDocumentBuilder, DidDocumentProvider, EcdhSessionProvider)

#### Class Name
`TaEcdhService`

#### Function Name
`TaEcdhService`

#### Function Introduction
`Creates the ECDH service. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | Wallet adapter with the TAS keyagree key | M | |
| didDocumentBuilder | `DidDocumentBuilder` | Used to extract the client public key | M | |
| didDocumentProvider | `DidDocumentProvider` | Provider of client and TAS DID Documents | M | |
| ecdhSessionProvider | `EcdhSessionProvider` | Provider that saves the session | M | |

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
`Verifies the client's ECDH request (client DID format, keyAgreement proof purpose, signature with the client's keyagree key), generates a server key pair on the requested curve and a 16-byte server nonce, requires a 16-byte client nonce, derives the session key, saves it with EcdhSessionProvider.saveEcdhSession(clientDid, sessionKey, mergedNonce), and returns an AccEcdh whose server is the TAS DID and whose keyAgreement proof is signed with the TAS keyagree key ({did}?versionId={versionId}#keyagree). TaSdkException thrown by EcdhSessionProvider.saveEcdhSession or DidDocumentProvider.getTasDidDocument is passed through; other failures, including a client DID Document that cannot be retrieved, become TaSdkException(SDK_ECDH_KEY_AGREEMENT_FAILED).`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| ecdhReqData | `EcdhReqData` | Client ECDH request | M | client, clientNonce, curve, publicKey, candidate, proof |
| serverCipher | `SymmetricCipherType` | Server cipher | M | e.g., AES_256_CBC |
| serverPadding | `SymmetricPaddingType` | Server padding | M | PKCS5 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `AccEcdh` | Signed ECDH acceptance | M | server, serverNonce, publicKey, cipher, padding, proof |

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

`TaSignatureService` creates and verifies proofs with the TAS wallet. It extends `BaseSignatureServiceImpl` of did-common-sdk-server. The public methods below are inherited; the TA SDK supplies the signing key for each proof purpose (ASSERTION_METHOD → `assert`, AUTHENTICATION → `auth`, CAPABILITY_INVOCATION → `invoke`) and the creation time.

**Package**: `org.omnione.did.tas.core`

### 6.7.1 TaSignatureService(FileWalletAdapter)

#### Class Name
`TaSignatureService`

#### Function Name
`TaSignatureService`

#### Function Introduction
`Creates the signature service. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | Wallet adapter with the TAS keys | M | |

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
`Serializes data to sorted JSON, hashes it, signs it with the wallet key for proofPurpose, and returns a Proof with the multibase-encoded signature and the current UTC time. The proof is not attached to data. Throws TaSdkException(SDK_INVALID_PARAMETER) for KEY_AGREEMENT and CommonSdkException if signing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| data | `Object` | Object to sign | M | |
| proofPurpose | `ProofPurpose` | Proof purpose | M | KEY_AGREEMENT not allowed |
| proofType | `ProofType` | Proof type | M | e.g., SECP_256R1_SIGNATURE_2018 |
| verificationMethod | `String` | Verification method ID to put in the proof | M | See 6.3.5 |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `Proof` | Generated proof | M | `org.omnione.did.common.datamodel.data.signature.Proof` |

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
`Removes the proof value from data, hashes the sorted JSON, and verifies proof.proofValue with the public key that proof.verificationMethod identifies in didDocument (matched by full id or by "#" + key ID). Throws CommonSdkException if the key is not found or the signature is invalid.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| data | `Object` | Signed object | M | |
| proof | `Proof` | Proof to verify | M | `org.omnione.did.common.datamodel.data.signature.Proof` |
| didDocument | `DidDocument` | DID Document of the signer | M | |

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
`Verifies the proof of every key in the DID Document. Throws CommonSdkException if any proof is invalid. Same check as DidDocumentBuilder.verifyKeyProofs, which throws TaSdkException instead.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| didDocument | `DidDocument` | DID Document to verify | M | |

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

`TaVcService` issues, verifies, and revokes VCs with TAS as the issuer. It extends `BaseVcServiceImpl` of did-common-sdk-server, which implements `VcService`. VCs are signed with the TAS `assert` key using the TAS DID Document from `DidDocumentProvider.getTasDidDocument()`, saved with `VcStorageProvider.saveVc`, and revoked through `VcStorageProvider.revokeVc`. `issueCredential` and `verifyCredential` are inherited, `revokeCredential` overrides `VcService.revokeCredential`, and `issueVc` is added by the TA SDK.

**Package**: `org.omnione.did.tas.core`

### 6.8.1 TaVcService(FileWalletAdapter, DidDocumentProvider, VcStorageProvider)

#### Class Name
`TaVcService`

#### Function Name
`TaVcService`

#### Function Introduction
`Creates the VC service. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| fileWalletAdapter | `FileWalletAdapter` | Wallet adapter with the TAS assert key | M | |
| didDocumentProvider | `DidDocumentProvider` | Provider of the TAS DID Document | M | |
| vcStorageProvider | `VcStorageProvider` | Provider that saves, checks, and revokes VCs | M | |

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
`Generates a VC from a prepared IssueVcParam (schema, issuer, claims, and optionally types, evidence, and validity period), signs it with the TAS assert key, saves it with VcStorageProvider.saveVc, and returns it. Throws TaSdkException from the providers and the wallet adapter as is, and TaSdkException(SDK_VC_ISSUANCE_FAILED) for other failures.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| issueVcParam | `IssueVcParam` | VC issuance parameters | M | Set schema, issuer, and claims beforehand (VcSchemaProcessor.configure, ClaimManager.configure) |
| holderDid | `String` | DID of the credential subject | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VerifiableCredential` | Signed VC | M | |

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
`Issues a VC from an IssueProfile and a CredentialSubject: the schema JSON comes from issueProfile.profile.credentialSchema.value, the issuer from issueProfile.profile.issuer (id, name, certVcRef), and the claims and holder DID from credentialSubject. The VC is signed with the TAS assert key and saved with VcStorageProvider.saveVc. Inherited from BaseVcServiceImpl. Throws CommonSdkException (for example VC_GENERATION_FAILED or VC_SIGNING_FAILED) on failure.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| issueProfile | `IssueProfile` | Issue Profile with schema and issuer | M | |
| credentialSubject | `CredentialSubject` | Holder DID and claims | M | Claims may be null |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VerifiableCredential` | Signed VC | M | |

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
`Validates the VC structure, verifies its proof with issuerDidDoc, rejects a VC whose validUntil (ISO 8601) has passed, and calls VcStorageProvider.checkRevocationStatus. Inherited from BaseVcServiceImpl. Throws CommonSdkException if any check fails. Note: the inherited proof check converts proof.type with ProofType.valueOf, so in the current did-common-sdk-server a VC whose proof type is a display name such as Secp256r1Signature2018 fails with SIGNATURE_VERIFICATION_FAILED.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vc | `VerifiableCredential` | VC to verify | M | |
| issuerDidDoc | `DidDocument` | DID Document of the issuer | M | |

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
`Revokes a VC by calling VcStorageProvider.revokeVc(vcId, reason). Overrides VcService.revokeCredential. Throws CommonSdkException(VC_GENERATION_FAILED) if the provider throws TaSdkException.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcId | `String` | ID of the VC to revoke | M | |
| reason | `String` | Revocation reason | M | e.g., "VOLUNTARY" |

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

`VcBuilder` builds unsigned VCs, such as the Certificate VCs of P110 and P120, with a fluent API. `setIssuer` loads the schema and returns a new `VcBuilder.BuildContext`; the remaining methods belong to `BuildContext`. A `VcBuilder` can be shared between threads; a `BuildContext` cannot. `TasEnrollmentProtocolImpl` and `EntityEnrollmentProtocolImpl` take a `VcBuilder` in their constructors.

**Package**: `org.omnione.did.tas.core.vc`

### 6.9.1 VcBuilder(VcSchemaProvider, String)

#### Class Name
`VcBuilder`

#### Function Name
`VcBuilder`

#### Function Introduction
`Creates a builder that loads the VC schema schemaId through vcSchemaProvider on every setIssuer call. Arguments are not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcSchemaProvider | `VcSchemaProvider` | Provider of the schema JSON | M | |
| schemaId | `String` | Schema ID passed to the provider | M | e.g., "certificate" |

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
`Starts a new build: gets the schema JSON with VcSchemaProvider.getVcSchemaJson(schemaId), sets it and the issuer (DID, name, Certificate VC URL) on a new IssueVcParam, and returns a new BuildContext. Throws TaSdkException from the provider as is, and TaSdkException(CERTIFICATE_VC_GENERATION_FAILED) if the schema cannot be parsed.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| did | `String` | Issuer DID | M | |
| name | `String` | Issuer name | M | |
| certVcRef | `String` | Issuer Certificate VC URL | O | May be null |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | New build context | M | |

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
`Sets the VC types. Returns this context.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| types | `List<VcType>` | VC types | M | e.g., VERIFIABLE_CREDENTIAL, CERTIFICATE_VC |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | This context | M | |

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
`Adds a claim with a raw value, usually UTF-8 bytes. A claim with the same code replaces the earlier one. Returns this context.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| code | `String` | Claim code | M | e.g., "org.opendid.v1.subject" |
| value | `byte[]` | Claim value | M | UTF-8 bytes in most cases |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | This context | M | |

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
`Sets validFrom and validUntil from ISO 8601 strings parsed with ZonedDateTime.parse. Throws DateTimeParseException for an invalid string and NullPointerException for null. Returns this context.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| validFrom | `String` | Start of validity (ISO 8601) | M | e.g., "2026-01-01T00:00:00Z" |
| validUntil | `String` | End of validity (ISO 8601) | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | This context | M | |

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
`Sets validFrom and validUntil. The values are not checked. Returns this context.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| validFrom | `ZonedDateTime` | Start of validity | M | |
| validUntil | `ZonedDateTime` | End of validity | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | This context | M | |

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
`Sets the evidence list to this single evidence. A null value causes NullPointerException. Returns this context.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| evidence | `DocumentVerificationEvidence` | Document verification evidence | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcBuilder.BuildContext` | This context | M | |

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
`Sets the collected claims on the parameters and generates the unsigned VC for holderDid. Sign the result afterwards, for example with WalletProvider.signVc. Throws TaSdkException(CERTIFICATE_VC_GENERATION_FAILED) if generation fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| holderDid | `String` | DID of the credential subject | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VerifiableCredential` | Unsigned VC | M | |

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

`VcSchemaProcessor` loads VC schemas through `VcSchemaProvider` and parses them with `BaseCoreVcUtil` of did-common-sdk-server.

**Package**: `org.omnione.did.tas.core`

### 6.10.1 VcSchemaProcessor(VcSchemaProvider)

#### Class Name
`VcSchemaProcessor`

#### Function Name
`VcSchemaProcessor`

#### Function Introduction
`Creates a schema processor. Null is not checked.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcSchemaProvider | `VcSchemaProvider` | Provider of the schema JSON | M | |

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
`Parses a VC schema JSON string. Throws TaSdkException(SDK_INVALID_PARAMETER) if parsing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| vcSchemaJson | `String` | VC schema JSON | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcSchema` | Parsed schema | M | |

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
`Gets the schema JSON with VcSchemaProvider.getVcSchemaJson(schemaId) and parses it. Throws TaSdkException from the provider as is, and TaSdkException(SDK_INVALID_PARAMETER) if parsing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| schemaId | `String` | Schema ID | M | |

#### Output Parameters
| Type | Description | M/O | Note |
|---|---|---|---|
| `VcSchema` | Parsed schema | M | |

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
`Gets the schema JSON with VcSchemaProvider.getVcSchemaJson(schemaId), parses it, and sets it on param. Throws TaSdkException from the provider as is, and TaSdkException(SDK_INVALID_PARAMETER) if parsing fails.`

#### Input Parameters
| Parameter | Type | Description | M/O | Note |
|---|---|---|---|---|
| param | `IssueVcParam` | Issuance parameters to configure | M | |
| schemaId | `String` | Schema ID | M | |

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

Data models used in the `IssuerApiClient`, protocol, and provider method signatures and their nested types. Unless noted otherwise, the classes use Lombok `@Getter`, `@Builder`, `@NoArgsConstructor`, and `@AllArgsConstructor`. They have no setters, so create them with the builder or the all-arguments constructor. M/O shows what the SDK requires or always sets.

## 7.1 OfferIssueVcApiRequest

Request of `IssuerApiClient.offerIssueVc` (POST /issuer/api/v1/offer-issue-vc). The SDK protocols do not build this request.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID | O |
| vcPlanId | `String` | VC Plan ID | M |
| issuer | `String` | Issuer DID | M |
| holder | `String` | Holder DID, for push and email offers | O |
| email | `String` | Email address, for email offers | O |

<br>

## 7.2 OfferIssueVcApiResponse

Response of `IssuerApiClient.offerIssueVc`.

**Package**: `org.omnione.did.tas.model.response.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| offerId | `String` | Offer ID | M |
| validUntil | `String` | Expiry time of the offer | M |

<br>

## 7.3 InspectIssueProposeApiRequest

Request of `IssuerApiClient.inspectProposeIssue` (POST /issuer/api/v1/inspect-propose-issue), built by `VcIssuanceProtocol.proposeIssueVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| vcPlanId | `String` | VC Plan ID | M |
| issuer | `String` | Issuer DID: ProposeIssueVcRequest.issuer, or VcPlanInfo.issuerDid if empty | M |
| offerId | `String` | Offer ID, when the user responds to an offer | O |

<br>

## 7.4 InspectIssueProposeApiResponse

Response of `IssuerApiClient.inspectProposeIssue`. `proposeIssueVc` throws ISSUER_COMMUNICATION_FAILED if txId or refId is empty.

**Package**: `org.omnione.did.tas.model.response.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Issuer transaction ID; returned as ProposeIssueVcResult.externalTxId | M |
| refId | `String` | Issuer reference number | M |

<br>

## 7.5 GenerateIssueProfileApiRequest

Request of `IssuerApiClient.generateIssueProfile` (POST /issuer/api/v1/generate-issue-profile), built by `VcIssuanceProtocol.requestIssueProfile`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| txId | `String` | Issuer transaction ID from VcIssuanceDataProvider.getExternalTxId | M |
| holder | `GenerateIssueProfileApiRequest.ApiHolder` | Holder information. See 7.6 | M |

<br>

## 7.6 GenerateIssueProfileApiRequest.ApiHolder

Holder information in `GenerateIssueProfileApiRequest`, copied from `HolderInfo`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | Holder DID | M |
| pii | `String` | Holder PII (CI) | O |

<br>

## 7.7 GenerateIssueProfileApiResponse

Response of `IssuerApiClient.generateIssueProfile`. `requestIssueProfile` throws ISSUE_PROFILE_RETRIEVAL_FAILED if profile is null.

**Package**: `org.omnione.did.tas.model.response.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| profile | `Object` | Issue Profile. Returned unchanged as RequestIssueProfileResult.profile | M |

<br>

## 7.8 IssueVcApiRequest

Request of `IssuerApiClient.issueVc` (POST /issuer/api/v1/issue-vc), built by `VcIssuanceProtocol.requestIssueVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| txId | `String` | Issuer transaction ID from VcIssuanceDataProvider.getExternalTxId | M |
| accE2e | `AccE2e` | E2E acceptance from the wallet, copied from RequestIssueVcRequest.accE2e. See 7.18 | M |
| encReqVc | `String` | Encrypted VC request from the wallet, copied from RequestIssueVcRequest.encReqVc | M |

<br>

## 7.9 IssueVcApiResponse

Response of `IssuerApiClient.issueVc`. `requestIssueVc` throws VC_ISSUANCE_FAILED if e2e is null and VC_ENCRYPTION_FAILED if e2e.encVc is empty.

**Package**: `org.omnione.did.tas.model.response.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| e2e | `E2e` | E2E-encrypted VC. See 7.19 | M |

<br>

## 7.10 CompleteVcApiRequest

Request of `IssuerApiClient.completeVc` (POST /issuer/api/v1/complete-vc), built by `VcIssuanceProtocol.confirmIssueVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| txId | `String` | Issuer transaction ID from VcIssuanceDataProvider.getExternalTxId | M |
| vcId | `String` | ID of the issued VC (UUID) | M |

<br>

## 7.11 CompleteVcApiResponse

Response of `IssuerApiClient.completeVc`.

**Package**: `org.omnione.did.tas.model.response.issuer`

This class has no fields and no Lombok annotations; create it with `new CompleteVcApiResponse()`. The SDK ignores the returned object, so returning null is also accepted.

<br>

## 7.12 InspectProposeRevokeApiRequest

Request of `IssuerApiClient.inspectProposeRevoke` (POST /issuer/api/v1/inspect-propose-revoke), built by `VcRevocationProtocol.proposeRevokeVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| vcId | `String` | ID of the VC to revoke | M |

<br>

## 7.13 InspectProposeRevokeApiResponse

Response of `IssuerApiClient.inspectProposeRevoke`. `proposeRevokeVc` throws PROPOSE_REVOKE_VC_FAILED if issuerNonce is empty.

**Package**: `org.omnione.did.tas.model.response.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| issuerNonce | `String` | Issuer nonce (16 bytes, multibase-encoded) | M |
| authType | `Integer` | Required authentication type: 1 PIN, 2 BIO, 3 PIN or BIO, 4 PIN and BIO | O |

<br>

## 7.14 RevokeVcApiRequest

Request of `IssuerApiClient.revokeVc` (POST /issuer/api/v1/revoke-vc), built by `VcRevocationProtocol.requestRevokeVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| reqRevokeVc | `ReqRevokeVc` | Revocation request from the wallet, copied from RequestRevokeVcRequest.request. See 7.20 | M |

<br>

## 7.15 RevokeVcApiResponse

Response of `IssuerApiClient.revokeVc`.

**Package**: `org.omnione.did.tas.model.response.issuer`

This class has no fields and no Lombok annotations; create it with `new RevokeVcApiResponse()`. The SDK ignores the returned object, so returning null is also accepted.

<br>

## 7.16 CompleteRevokeApiRequest

Request of `IssuerApiClient.completeRevoke` (POST /issuer/api/v1/complete-revoke), built by `VcRevocationProtocol.confirmRevokeVc`.

**Package**: `org.omnione.did.tas.model.request.issuer`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it from the protocol request | O |
| txId | `String` | TAS transaction ID (ConfirmRevokeVcRequest.txId) | M |

<br>

## 7.17 CompleteRevokeApiResponse

Response of `IssuerApiClient.completeRevoke`.

**Package**: `org.omnione.did.tas.model.response.issuer`

This class has no fields and no Lombok annotations; create it with `new CompleteRevokeApiResponse()`. The SDK ignores the returned object, so returning null is also accepted.

<br>

## 7.18 AccE2e

The wallet's acceptance of the Issuer's E2E encryption request (P210 Step 6). Used in `RequestIssueVcRequest` and `IssueVcApiRequest`.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| publicKey | `String` | Wallet public key for E2E encryption (multibase-encoded) | M |
| iv | `String` | Initialization vector (multibase-encoded) | M |
| proof | `Proof` | Proof of the acceptance (`org.omnione.did.data.model.did.Proof`) | M |

<br>

## 7.19 E2e

E2E-encrypted VC returned by the Issuer (P210 Step 6). TAS relays it and cannot decrypt it. Used in `IssueVcApiResponse` and `RequestIssueVcResult`.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| iv | `String` | Initialization vector (multibase-encoded) | M |
| encVc | `String` | Encrypted VC that only the wallet can decrypt (multibase-encoded) | M |

<br>

## 7.20 ReqRevokeVc

VC revocation request from the wallet (P220 Step 4). Used in `RequestRevokeVcRequest` and `RevokeVcApiRequest`.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| vcId | `String` | ID of the VC to revoke. Must equal VcRevocationDataProvider.getVcId(txId) | M |
| issuerNonce | `String` | Issuer nonce received in Step 1. Must equal VcRevocationDataProvider.getIssuerNonce(txId) | M |
| proof | `Proof` | PIN signature with the assertionMethod purpose (`org.omnione.did.common.datamodel.data.signature.Proof`). type, verificationMethod, proofPurpose, and proofValue are required | M |
| proofs | `List<Proof>` | Additional proofs for combined authentication. The SDK does not check them | O |

<br>

## 7.21 EnrollTasRequest

Request of `TasEnrollmentProtocol.enrollTas` (P110). This class also has setters (`@Setter`). `toString()` masks request.

**Package**: `org.omnione.did.tas.model.request.protocol.tas`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| request | `EnrollTasRequest.Request` | Enrollment request information. See 7.22. If it is null, enrollTas throws TAS_ENROLLMENT_FAILED | M |

<br>

## 7.22 EnrollTasRequest.Request

Enrollment request information in `EnrollTasRequest`. This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.tas`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| password | `String` | TAS enrollment password. Must equal TasProvider.getTasPassword(); otherwise enrollTas throws PASSWORD_MISMATCH | M |

<br>

## 7.23 EnrollTasResponse

Response of `TasEnrollmentProtocol.enrollTas`.

**Package**: `org.omnione.did.tas.model.response.protocol.tas`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID. The SDK does not set it | O |
| certVcRef | `String` | URL of the published TAS Certificate VC, as returned by CertificateProvider.publishCertificateVc | M |

<br>

## 7.24 ProposeEnrollEntityRequest

Request of `EntityEnrollmentProtocol.proposeEnrollEntity` (P120 Step 1). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK only logs it | O |

<br>

## 7.25 ProposeEnrollEntityResponse

Response of `EntityEnrollmentProtocol.proposeEnrollEntity`. The SDK does not store these values; store them so that TransactionProvider.getTransaction(txId) returns the authNonce in Step 3.

**Package**: `org.omnione.did.tas.model.response.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID (UUID) generated by the SDK | M |
| authNonce | `String` | Authentication nonce for DID Auth in Step 3 (16 bytes, multibase-encoded) | M |

<br>

## 7.26 RequestEnrollEntityRequest

Request of `EntityEnrollmentProtocol.requestEnrollEntity` (P120 Step 3). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Used for TransactionProvider.getTransaction and the EcdhSessionProvider lookups | M |
| didAuth | `DidAuth` | DID Auth of the entity: did, authNonce, proof. This is `org.omnione.did.common.datamodel.data.did.DidAuth` with `org.omnione.did.data.model.did.Proof`, not the class in 7.66. The SDK checks the DID format, that proof.verificationMethod is a DID key URL, authNonce against Transaction.authNonce, and the signature with the key of verificationMethod; it does not check proofPurpose or that the verificationMethod DID equals did. The entity is loaded with EntityProvider.getEntityByDid(did). If didAuth is null, a NullPointerException is thrown | M |
| dn | `String` | Distinguished Name for the org.opendid.v1.subject claim of the Certificate VC. If empty, Entity.dn is used, and if that is also empty, "cn={name},o={role}" | O |

<br>

## 7.27 RequestEnrollEntityResponse

Response of `EntityEnrollmentProtocol.requestEnrollEntity`.

**Package**: `org.omnione.did.tas.model.response.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |
| iv | `String` | IV used to encrypt the Certificate VC (16 bytes, multibase-encoded) | M |
| encVc | `String` | Certificate VC encrypted with the ECDH session key, cipher, and padding of the txId (multibase-encoded) | M |
| vcUrl | `String` | URL returned by CertificateProvider.publishCertificateVc | M |
| vcId | `String` | ID of the issued Certificate VC. Store it with the transaction to check the vcId of Step 4. Internal to the server and not part of the API response | M |

<br>

## 7.28 ConfirmEnrollEntityRequest

Request of `EntityEnrollmentProtocol.confirmEnrollEntity` (P120 Step 4). This class also has setters (`@Setter`). The SDK only copies txId to the response; check the transaction state and vcId before the call.

**Package**: `org.omnione.did.tas.model.request.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Returned unchanged | M |
| vcId | `String` | ID of the Certificate VC received in Step 3. The SDK only logs it; compare it with RequestEnrollEntityResponse.vcId in the server | O |

<br>

## 7.29 ConfirmEnrollEntityResponse

Response of `EntityEnrollmentProtocol.confirmEnrollEntity`.

**Package**: `org.omnione.did.tas.model.response.protocol.entity`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.30 ProposeRegisterUserRequest

Request of `UserRegistrationProtocol.proposeRegisterUser` (P132 Step 1). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK only logs it | O |

<br>

## 7.31 ProposeRegisterUserResponse

Response of `UserRegistrationProtocol.proposeRegisterUser`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID (UUID) generated by the SDK. The SDK does not store it | M |

<br>

## 7.32 RetrieveKycRequest

Request of `UserRegistrationProtocol.retrieveKyc` (P132 Step 4). This class also has setters (`@Setter`). Provide kycToken or kycTxId; if both are empty, retrieveKyc throws KYC_TOKEN_AND_TXID_BOTH_MISSING.

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| kycToken | `String` | Token issued by the KYC system (Option A). If not empty, PII is read with KycProvider.getPiiFromKycToken and kycTxId is ignored | O |
| kycTxId | `String` | Transaction ID of the KYC system (Option B). Used with KycProvider.retrieveKycData when kycToken is empty | O |

<br>

## 7.33 RetrieveKycResponse

Response of `UserRegistrationProtocol.retrieveKyc`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |
| piiData | `PiiData` | PII returned by KycProvider. Excluded from JSON (`@JsonIgnore`); store it with the txId in the server and do not send it to the client. See 7.77 | M |

<br>

## 7.34 RequestRegisterUserRequest

Request of `UserRegistrationProtocol.requestRegisterUser` (P132 Step 5). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| signedDidDoc | `SignedDidDoc` | DID Document signed by the wallet. See 7.67. The DID of ownerDidDoc must not be registered yet (USER_DID_ALREADY_EXISTS) | M |

<br>

## 7.35 RequestRegisterUserResponse

Response of `UserRegistrationProtocol.requestRegisterUser`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.36 ConfirmRegisterUserRequest

Request of `UserRegistrationProtocol.confirmRegisterUser` (P132 Step 6). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken and returned unchanged | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |

<br>

## 7.37 ConfirmRegisterUserResponse

Response of `UserRegistrationProtocol.confirmRegisterUser`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.38 ProposeUpdateDidDocRequest

Request of `DidDocUpdateProtocol.proposeUpdateDidDoc` (P141 Step 1). Check that the user is ACTIVATED before the call, and store did with the transaction so that Step 4 can compare it with Transaction.did.

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| did | `String` | DID of the user whose DID Document is updated. The SDK only logs it | O |

<br>

## 7.39 ProposeUpdateDidDocResponse

Response of `DidDocUpdateProtocol.proposeUpdateDidDoc`. Store txId, did, and authNonce so that TransactionProvider.getTransaction(txId) returns them in Step 4.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID (UUID) generated by the SDK | M |
| authNonce | `String` | authNonce for DID Auth in Step 4 (16 bytes, multibase-encoded) | M |

<br>

## 7.40 RequestUpdateDidDocRequest

Request of `DidDocUpdateProtocol.requestUpdateDidDoc` (P141 Step 4).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken and TransactionProvider.getTransaction | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| signedDidDoc | `SignedDidDoc` | Updated DID Document signed by the wallet. See 7.67. versionId must be the current versionId + 1, updated must be later than the current value, and @context, id, controller, created, and deactivated must not change | M |
| didAuth | `DidAuth` | DID Auth of the user. See 7.66. did must equal the id of the updated DID Document | M |

<br>

## 7.41 RequestUpdateDidDocResponse

Response of `DidDocUpdateProtocol.requestUpdateDidDoc`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.42 ConfirmUpdateDidDocRequest

Request of `DidDocUpdateProtocol.confirmUpdateDidDoc` (P141 Step 5).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken and returned unchanged | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |

<br>

## 7.43 ConfirmUpdateDidDocResponse

Response of `DidDocUpdateProtocol.confirmUpdateDidDoc`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.44 ProposeRestoreDidDocRequest

Request of `DidDocRestoreProtocol.proposeRestoreDidDoc` (P142 Step 1). Check the offerId and that the user is DEACTIVATED before the call, and store did with the transaction so that Step 4 can compare it with Transaction.did.

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| offerId | `String` | Offer ID received in the offer step (push or email). The SDK does not read it | O |
| did | `String` | DID of the user whose DID Document is restored. The SDK only logs it | O |

<br>

## 7.45 ProposeRestoreDidDocResponse

Response of `DidDocRestoreProtocol.proposeRestoreDidDoc`. Store txId, did, and authNonce so that TransactionProvider.getTransaction(txId) returns them in Step 4.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID (UUID) generated by the SDK | M |
| authNonce | `String` | authNonce for DID Auth in Step 4 (16 bytes, multibase-encoded) | M |

<br>

## 7.46 RequestRestoreDidDocRequest

Request of `DidDocRestoreProtocol.requestRestoreDidDoc` (P142 Step 4).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken and TransactionProvider.getTransaction | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| didAuth | `DidAuth` | DID Auth of the user. See 7.66. The DID Document of its did is changed to ACTIVATED | M |

<br>

## 7.47 RequestRestoreDidDocResponse

Response of `DidDocRestoreProtocol.requestRestoreDidDoc`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.48 ConfirmRestoreDidDocRequest

Request of `DidDocRestoreProtocol.confirmRestoreDidDoc` (P142 Step 5).

**Package**: `org.omnione.did.tas.model.request.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| txId | `String` | Transaction ID from Step 1. Passed to TokenProvider.validateServerToken and returned unchanged | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |

<br>

## 7.49 ConfirmRestoreDidDocResponse

Response of `DidDocRestoreProtocol.confirmRestoreDidDoc`.

**Package**: `org.omnione.did.tas.model.response.protocol.user`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.50 ProposeIssueVcRequest

Request of `VcIssuanceProtocol.proposeIssueVc` (P210 Step 2).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to InspectIssueProposeApiRequest.id | O |
| vcPlanId | `String` | VC Plan ID. Looked up with VcIssuanceDataProvider.getVcPlan (VC_PLAN_NOT_FOUND if it returns null) and copied to InspectIssueProposeApiRequest.vcPlanId | M |
| issuer | `String` | Issuer DID. If empty, VcPlanInfo.issuerDid is used; ISSUER_NOT_FOUND if both are empty | O |
| offerId | `String` | Offer ID, when the user responds to an offer. Copied to InspectIssueProposeApiRequest.offerId | O |

<br>

## 7.51 ProposeIssueVcResult

Response of `VcIssuanceProtocol.proposeIssueVc`. The SDK does not store these values; store them with the txId so that VcIssuanceDataProvider returns them in later steps.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | TAS transaction ID (UUID) generated by the SDK | M |
| externalTxId | `String` | Issuer transaction ID (InspectIssueProposeApiResponse.txId). Return it from VcIssuanceDataProvider.getExternalTxId(txId) | M |
| refId | `String` | Issuer reference number (InspectIssueProposeApiResponse.refId) | M |

<br>

## 7.52 RequestIssueProfileRequest

Request of `VcIssuanceProtocol.requestIssueProfile` (P210 Step 5).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to GenerateIssueProfileApiRequest.id | O |
| txId | `String` | TAS transaction ID. Passed to TokenProvider.validateServerToken and to VcIssuanceDataProvider.getExternalTxId and getHolderInfo | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |

<br>

## 7.53 RequestIssueProfileResult

Response of `VcIssuanceProtocol.requestIssueProfile`.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |
| authNonce | `String` | New authNonce for DID Auth in Step 6 (16 bytes, multibase-encoded). Store it so that VcIssuanceDataProvider.getAuthNonce(txId) returns it | M |
| profile | `Object` | Issue Profile from the Issuer (GenerateIssueProfileApiResponse.profile), returned unchanged | M |

<br>

## 7.54 RequestIssueVcRequest

Request of `VcIssuanceProtocol.requestIssueVc` (P210 Step 6).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to IssueVcApiRequest.id | O |
| txId | `String` | TAS transaction ID. Passed to TokenProvider.validateServerToken and to VcIssuanceDataProvider.getAuthNonce and getExternalTxId | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| didAuth | `DidAuth` | DID Auth of the holder. See 7.66. Verified against VcIssuanceDataProvider.getAuthNonce(txId). The SDK does not compare did with HolderInfo.did | M |
| accE2e | `AccE2e` | E2E acceptance from the wallet. Forwarded to IssueVcApiRequest.accE2e without checks. See 7.18 | M |
| encReqVc | `String` | Encrypted VC request from the wallet. Forwarded to IssueVcApiRequest.encReqVc without checks | M |

<br>

## 7.55 RequestIssueVcResult

Response of `VcIssuanceProtocol.requestIssueVc`.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |
| e2e | `E2e` | E2E-encrypted VC from the Issuer (IssueVcApiResponse.e2e), returned unchanged. See 7.19 | M |

<br>

## 7.56 ConfirmIssueVcRequest

Request of `VcIssuanceProtocol.confirmIssueVc` (P210 Step 7).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to CompleteVcApiRequest.id | O |
| txId | `String` | TAS transaction ID. Passed to TokenProvider.validateServerToken and VcIssuanceDataProvider.getExternalTxId | M |
| serverToken | `String` | Server token. Validated with TokenProvider.validateServerToken(serverToken, txId) | M |
| vcId | `String` | ID of the issued VC. Must be a UUID (VC_ID_MISMATCH if empty or not a UUID). Copied to CompleteVcApiRequest.vcId | M |

<br>

## 7.57 ConfirmIssueVcResult

Response of `VcIssuanceProtocol.confirmIssueVc`.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.58 ProposeRevokeVcRequest

Request of `VcRevocationProtocol.proposeRevokeVc` (P220 Step 1).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to InspectProposeRevokeApiRequest.id | O |
| vcId | `String` | ID of the VC to revoke. Looked up with VcRevocationDataProvider.getVcMeta (VC_META_FETCH_FAILED if it returns null) and copied to InspectProposeRevokeApiRequest.vcId | M |

<br>

## 7.59 ProposeRevokeVcResult

Response of `VcRevocationProtocol.proposeRevokeVc`. Store txId, vcId, and issuerNonce so that VcRevocationDataProvider.getVcId(txId) and getIssuerNonce(txId) return them in Step 4.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | TAS transaction ID (UUID) generated by the SDK | M |
| issuerNonce | `String` | Issuer nonce (InspectProposeRevokeApiResponse.issuerNonce, 16 bytes, multibase-encoded) | M |
| authType | `Integer` | Required authentication type from the Issuer: 1 PIN, 2 BIO, 3 PIN or BIO, 4 PIN and BIO. Null if the Issuer does not return it | O |

<br>

## 7.60 RequestRevokeVcRequest

Request of `VcRevocationProtocol.requestRevokeVc` (P220 Step 4).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to RevokeVcApiRequest.id | O |
| txId | `String` | TAS transaction ID. Passed to VcRevocationDataProvider.getVcId and getIssuerNonce | M |
| serverToken | `String` | Server token. The SDK neither reads nor validates it; validate it in the server before the call | O |
| request | `ReqRevokeVc` | Revocation request from the wallet. See 7.20. Forwarded to RevokeVcApiRequest.reqRevokeVc after the checks. If it is null, requestRevokeVc throws SDK_UNKNOWN_ERROR | M |

<br>

## 7.61 RequestRevokeVcResult

Response of `VcRevocationProtocol.requestRevokeVc`.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.62 ConfirmRevokeVcRequest

Request of `VcRevocationProtocol.confirmRevokeVc` (P220 Step 5).

**Package**: `org.omnione.did.tas.model.request.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK copies it to CompleteRevokeApiRequest.id | O |
| txId | `String` | TAS transaction ID. Copied to CompleteRevokeApiRequest.txId and to the response | M |
| serverToken | `String` | Server token. The SDK neither reads nor validates it; validate it in the server before the call | O |

<br>

## 7.63 ConfirmRevokeVcResult

Response of `VcRevocationProtocol.confirmRevokeVc`.

**Package**: `org.omnione.did.tas.model.response.protocol.vc`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| txId | `String` | Transaction ID, copied from the request | M |

<br>

## 7.64 RegisterWalletRequest

Request of `WalletRegistrationProtocol.registerWallet` (P131). This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.request.protocol.wallet`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Message ID. The SDK does not read it | O |
| attestedDidDoc | `AttestedDidDoc` | Wallet DID Document attested by the Wallet Provider (`org.omnione.did.common.datamodel.data.did.AttestedDidDoc`): walletId, ownerDidDoc (multibase-encoded DID Document JSON with key proofs), provider (`Provider`: did, certVcRef), nonce, and proof (the provider's assertionMethod signature over the structure). provider.certVcRef is optional and is validated with CertificateProvider.validateCertificateVc when present. registerWallet throws SDK_FIELD_REQUIRED if attestedDidDoc is null | M |

<br>

## 7.65 RegisterWalletResponse

Response of `WalletRegistrationProtocol.registerWallet`.

**Package**: `org.omnione.did.tas.model.response.protocol.wallet`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| walletDid | `String` | DID of the registered wallet (id of the owner DID Document) | M |
| txId | `String` | Transaction ID. The SDK does not set it | O |

<br>

## 7.66 DidAuth

DID Auth used in `RequestUpdateDidDocRequest`, `RequestRestoreDidDocRequest`, and `RequestIssueVcRequest`. `RequestEnrollEntityRequest` uses `org.omnione.did.common.datamodel.data.did.DidAuth` instead. This class also has setters (`@Setter`). The SDK verifies the signature over this object without proof.proofValue.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | DID of the user. Must be a valid DID and equal the DID of proof.verificationMethod. In the DID Document protocols it must also equal Transaction.did when that is set | M |
| authNonce | `String` | authNonce received from TAS (16 bytes, multibase-encoded). Compared with the stored value by decoded bytes (AUTH_NONCE_MISMATCH) | M |
| proof | `Proof` | Signature with the user's authentication key (`org.omnione.did.common.datamodel.data.signature.Proof`). verificationMethod must be a DID key URL and proofPurpose must be authentication. PROOF_MISSING if null | M |

<br>

## 7.67 SignedDidDoc

DID Document signed by the wallet. Used in `RequestRegisterUserRequest` and `RequestUpdateDidDocRequest`. This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| ownerDidDoc | `String` | Multibase-encoded owner DID Document JSON with the proof of each key. The SDK decodes and parses it and verifies every key proof (OWNER_DID_DOC_KEY_PROOF_FAILED) | M |
| wallet | `Wallet` | Wallet that signed this object. See 7.68. wallet.did must equal the DID of proof.verificationMethod | M |
| nonce | `String` | Signing nonce (multibase-encoded). Part of the signed message; the SDK does not check its value | M |
| proof | `Proof` | Wallet signature over ownerDidDoc, wallet, and nonce (`org.omnione.did.common.datamodel.data.signature.Proof`). type, verificationMethod (DID key URL of the wallet DID), proofPurpose (assertionMethod), and proofValue are required | M |

<br>

## 7.68 Wallet

Wallet identification in `SignedDidDoc` and `SignedWalletInfo`. This class also has setters (`@Setter`).

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| id | `String` | Wallet ID registered with TAS. Part of the signed message; the SDK does not check its value | M |
| did | `String` | Wallet DID. In SignedDidDoc it must equal the DID of proof.verificationMethod (SDK_INVALID_PROOF in P132, DID_MISMATCH in P141) | M |

<br>

## 7.69 ServerTokenSeed

Server token seed that the wallet sends in the token creation step. No SDK method reads or returns this class; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| purpose | `ServerTokenPurpose` | Purpose of the server token, such as CREATE_DID (5) or CREATE_DID_AND_ISSUE_VC (13) in P132. See 4.2 | M |
| walletInfo | `SignedWalletInfo` | Wallet information signed by the wallet. See 7.70 | M |
| caAppInfo | `AttestedAppInfo` | CA app information attested by the CA Provider. See 7.71 | M |

<br>

## 7.70 SignedWalletInfo

Wallet information signed by the wallet, used in `ServerTokenSeed`. No SDK method reads or returns this class; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| wallet | `Wallet` | Wallet ID and DID. See 7.68 | M |
| nonce | `String` | Signing nonce for replay protection (multibase-encoded) | M |
| proof | `Proof` | Wallet signature over the wallet information (`org.omnione.did.common.datamodel.data.signature.Proof`) | M |

<br>

## 7.71 AttestedAppInfo

CA app information attested by the CA Provider, used in `ServerTokenSeed`. No SDK method reads or returns this class; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| appId | `String` | ID of the client app registered with the CA Provider | M |
| provider | `Provider` | DID and certVcRef of the CA Provider (`org.omnione.did.common.datamodel.data.provider.Provider`) | M |
| nonce | `String` | Signing nonce for replay protection (multibase-encoded) | M |
| proof | `Proof` | CA Provider signature over the app information (`org.omnione.did.common.datamodel.data.signature.Proof`) | M |

<br>

## 7.72 ServerTokenData

Server token data signed by TAS. This class also has setters (`@Setter`). Null fields are omitted from JSON (`@JsonInclude(NON_NULL)`). No SDK method reads or returns this class; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.data`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| purpose | `ServerTokenPurpose` | Purpose of the server token. See 4.2 | M |
| walletId | `String` | Wallet ID, from ServerTokenSeed.walletInfo.wallet.id | M |
| appId | `String` | CA app ID, from ServerTokenSeed.caAppInfo.appId | M |
| validUntil | `String` | Expiry of the token (ISO 8601, UTC) | M |
| provider | `Provider` | DID and certVcRef of the TAS that issues the token (`org.omnione.did.common.datamodel.data.provider.Provider`) | M |
| nonce | `String` | Signing nonce for replay protection (16 bytes, multibase-encoded) | M |
| proof | `Proof` | Signature with the TAS assert key (`org.omnione.did.common.datamodel.data.signature.Proof`). Build the object with a null proofValue to get the signing message, then set proofValue | M |

<br>

## 7.73 Tas

TAS information returned by `TasProvider.getTasInfo`. `TasEnrollmentProtocol.enrollTas` uses it to build the TAS Certificate VC.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | TAS DID. Used to load the TAS DID Document (StorageProvider.findDidDoc), as the issuer and subject of the Certificate VC, and for CertificateProvider.generateCertificateVcUrl | M |
| name | `String` | TAS name, used as the issuer name of the Certificate VC | M |
| status | `TasStatus` | TAS enrollment status. See 4.3. The SDK does not read it | O |
| subjectDn | `String` | Subject DN for the org.opendid.v1.subject claim of the Certificate VC. If it is null, enrollTas throws CERTIFICATE_VC_GENERATION_FAILED | M |
| version | `String` | TAS version. The SDK does not read it | O |

<br>

## 7.74 Entity

Entity information returned by `EntityProvider.getEntityByDid` and `getEntityByRole`. `EntityEnrollmentProtocol.requestEnrollEntity` uses it to build the Certificate VC, and `WalletRegistrationProtocol.registerWallet` uses it to check the Wallet Provider.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | Entity DID. Used as the subject of the Certificate VC and for CertificateProvider.generateCertificateVcUrl | M |
| name | `String` | Entity name, used in the default DN "cn={name},o={role}" | O |
| role | `RoleType` | Entity role (`org.omnione.did.data.model.enums.vc.RoleType`). Its raw value becomes the org.opendid.v1.role claim; registerWallet requires WALLET_PROVIDER | M |
| dn | `String` | Distinguished Name for the org.opendid.v1.subject claim, used when RequestEnrollEntityRequest.dn is empty | O |
| certVcRef | `String` | URL of the entity's Certificate VC. The SDK does not read it | O |

<br>

## 7.75 Transaction

Transaction information returned by `TransactionProvider.getTransaction(txId)`. The SDK reads it in `EntityEnrollmentProtocol.requestEnrollEntity` and in Step 4 of `DidDocUpdateProtocol` and `DidDocRestoreProtocol`. If it is null, requestEnrollEntity throws SDK_TRANSACTION_NOT_FOUND, and the DID Document protocols throw REQUEST_UPDATE_DIDDOC_FAILED or REQUEST_RESTORE_DIDDOC_FAILED.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| authNonce | `String` | authNonce returned in the Propose step (16 bytes, multibase-encoded). Compared with DidAuth.authNonce | M |
| did | `String` | DID submitted in the Propose step. In the DID Document protocols, DidAuth.did must equal it when it is set (DID_MISMATCH). The entity enrollment protocol does not read it | O |

<br>

## 7.76 User

User information returned by `UserProvider.findByDid`. No SDK protocol calls findByDid, so the SDK does not read these fields; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | User DID | M |
| status | `UserStatus` | User status: ACTIVATED, DEACTIVATED, REVOKED, or TERMINATED. See 4.4 | M |
| pii | `String` | User PII (CI) obtained through KYC. Mask it in logs | M |

<br>

## 7.77 PiiData

PII returned by `KycProvider.getPiiFromKycToken` and `retrieveKycData` and carried in `RetrieveKycResponse.piiData`. The SDK only checks that it is not null (KYC_DATA_RETRIEVAL_FAILED) and does not read the fields; M/O follows the source comments.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| pii | `String` | PII (CI) derived from the user's identity by the KYC system. The key for duplicate user detection | M |
| name | `String` | Full name | M |
| birthDate | `String` | Birth date (YYYYMMDD) | M |
| phoneNumber | `String` | Phone number | O |
| address | `String` | Address | O |
| additionalClaims | `Map<String, String>` | Additional identity attributes from the KYC system | O |

<br>

## 7.78 HolderInfo

Holder information returned by `VcIssuanceDataProvider.getHolderInfo(txId)`. `VcIssuanceProtocol.requestIssueProfile` copies it to `GenerateIssueProfileApiRequest.ApiHolder` (7.6).

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| did | `String` | Holder DID. requestIssueProfile throws HOLDER_NOT_FOUND if it is null | M |
| pii | `String` | Holder PII (CI, multibase-encoded). Copied to ApiHolder.pii | O |

<br>

## 7.79 VcPlanInfo

VC Plan information returned by `VcIssuanceDataProvider.getVcPlan(vcPlanId)`. `VcIssuanceProtocol.proposeIssueVc` throws VC_PLAN_NOT_FOUND if it is null.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| vcPlanId | `String` | VC Plan ID. The SDK does not read it | O |
| name | `String` | VC Plan name. The SDK does not read it | O |
| issuerDid | `String` | Issuer DID. Used when ProposeIssueVcRequest.issuer is empty; ISSUER_NOT_FOUND if both are empty | O |
| vcSchemaId | `String` | VC Schema ID. The SDK does not read it | O |

<br>

## 7.80 VcMeta

VC metadata returned by `VcRevocationDataProvider.getVcMeta(vcId)`. It is not `org.omnione.did.data.model.vc.VcMeta`, which `StorageProvider` uses. `VcRevocationProtocol.proposeRevokeVc` throws VC_META_FETCH_FAILED if it is null.

**Package**: `org.omnione.did.tas.model.domain`

| Field | Type | Description | M/O |
|-------|------|-------------|-----|
| vcId | `String` | VC ID. Used only in error messages | O |
| issuer | `String` | Issuer DID. proposeRevokeVc throws ISSUER_NOT_FOUND if it is empty | M |
| subject | `String` | Subject (holder) DID. The SDK does not read it | O |
| status | `String` | VC status: "ACTIVE", "REVOKED", or "EXPIRED". proposeRevokeVc requires "ACTIVE" and otherwise throws VC_ALREADY_REVOKED, VC_EXPIRED, or VC_STATUS_INVALID | M |
| issuedDate | `String` | Issue date (ISO 8601). The SDK does not read it | O |
| expirationDate | `String` | Expiry date (ISO 8601). The SDK does not read it | O |

<br>

---

<div style="page-break-after: always;"></div>

# 8. Auxiliary Utilities

These are auxiliary classes that help implement the SDK. If a main API does the same job, using the main API is recommended.

## 8.1 AuthNonceMatcher

Compares authNonce values by their decoded bytes. The protocols use it to check DID Auth.

**Package**: `org.omnione.did.tas.core`

| Method | Description |
|--------|-------------|
| `static boolean matches(String expected, String actual)` | Multibase-decodes both values and compares the bytes, so the same nonce in different multibase encodings matches. Returns false if either value is null or cannot be decoded. |

<br>

## 8.2 ClaimManager

Static helpers that build `ClaimInfo` objects and claim maps for `IssueVcParam`. For Certificate VCs, `VcBuilder.BuildContext.addClaim` does the same job.

**Package**: `org.omnione.did.tas.core`

| Method | Description |
|--------|-------------|
| `static ClaimInfo create(String code, String value)` | Creates a ClaimInfo with code and the UTF-8 bytes of value. A null value causes NullPointerException. |
| `static HashMap<String, ClaimInfo> build(String... codeValuePairs)` | Builds a code to ClaimInfo map from alternating code and value strings. Throws IllegalArgumentException for an odd number of arguments. A later pair with the same code replaces the earlier one. |
| `static void configure(IssueVcParam param, HashMap<String, ClaimInfo> claims)` | Sets claims as the claims (privacy) of param, replacing any existing ones. |

<br>
