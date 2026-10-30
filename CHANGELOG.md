# Changelog

## v2.0.0 (2026-03-04)

### 🚀 New Features
- TA SDK Server
    - Protocol Implementations
        - P110: TAS Registration Protocol (`TasEnrollmentProtocol`)
        - P120: Entity Registration Protocol (`EntityEnrollmentProtocol`)
        - P131: Wallet Registration Protocol (`WalletRegistrationProtocol`)
        - P132: User Registration Protocol (`UserRegistrationProtocol`)
        - P141: DID Document Update Protocol (`DidDocUpdateProtocol`)
        - P142: DID Document Restore Protocol (`DidDocRestoreProtocol`)
        - P210: VC Issuance Protocol (`VcIssuanceProtocol`)
        - P220: VC Revocation Protocol (`VcRevocationProtocol`)
    - Provider Interfaces (16 interfaces)
        - `TransactionProvider`, `UserProvider`, `EntityProvider`, `TasProvider`
        - `WalletProvider`, `WalletRegistrationProvider`
        - `VcStorageProvider`, `VcIssuanceDataProvider`, `VcRevocationDataProvider`, `VcSchemaProvider`
        - `CertificateProvider`, `EcdhSessionProvider`, `TokenProvider`
        - `KycProvider`, `DidDocumentProvider`, `StorageProvider`
    - Core Services
        - `TaVcService`: VC issuance with TAS assertion key signing
        - `TaSignatureService`: DID Document and data structure signing
        - `TaEcdhService`: ECDH key agreement
        - `DidDocumentBuilder`: DID Document construction
        - `InvokedDidDocHandler`: TAS-invoked DID Document handling
        - `KeyManager`: TAS key management (assert, invoke, consent)
        - `BlockchainAdapter`: Blockchain DID/VC operations
        - `ClaimManager`: VC claim construction
    - Configuration
        - `TaSdkConfig`: Main SDK configuration (Builder pattern)
        - `WalletConfig`: Wallet file path and password
        - `CryptoConfig`: Cryptographic provider settings
        - `StorageConfig`: Storage backend configuration (Blockchain/LSS)
    - Exception Handling
        - `TaSdkException`: Single exception class (RuntimeException)
        - `TaSdkErrorCode`: Error code enum (80+ codes, SSDKTRA prefix)
        - `IssuerApiException`: Issuer API communication exception
    - Client Interface
        - `IssuerApiClient`: Issuer server HTTP communication interface
