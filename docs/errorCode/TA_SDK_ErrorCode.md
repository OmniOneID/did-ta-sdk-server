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

TA SDK Server Error
==

- Date: 2026-03-04
- Version: v2.0.0

| Version | Date       | History       |
| ------- | ---------- | ------------- |
| v2.0.0  | 2026-03-04 | Initial       |

<div style="page-break-after: always;"></div>

# Table of Contents

- [Model](#model)
  - [Error Response](#error-response)
- [Error Code](#error-code)
  - [1. SDK Common](#1-sdk-common)
    - [1-1. Validation (001001 ~ 001099)](#1-1-validation-001001--001099)
    - [1-2. Protocol (002001 ~ 002099)](#1-2-protocol-002001--002099)
    - [1-3. Crypto (003001 ~ 003099)](#1-3-crypto-003001--003099)
    - [1-4. Blockchain (004001 ~ 004099)](#1-4-blockchain-004001--004099)
    - [1-5. Wallet (005001 ~ 005099)](#1-5-wallet-005001--005099)
    - [1-6. Provider (006001 ~ 006099)](#1-6-provider-006001--006099)
    - [1-7. General (009001 ~ 009999)](#1-7-general-009001--009999)
  - [2. Protocol-Specific](#2-protocol-specific)
    - [2-1. P110 TAS Enrollment (011001 ~ 011099)](#2-1-p110-tas-enrollment-011001--011099)
    - [2-2. P131 Wallet Registration (013001 ~ 013099)](#2-2-p131-wallet-registration-013001--013099)
    - [2-3. P132 User Registration (032001 ~ 032099)](#2-3-p132-user-registration-032001--032099)
    - [2-4. P141 DID Document Update (017001 ~ 017099)](#2-4-p141-did-document-update-017001--017099)
    - [2-5. P142 DID Document Restore (020001 ~ 020099)](#2-5-p142-did-document-restore-020001--020099)
    - [2-6. P210 VC Issuance (021001 ~ 021099)](#2-6-p210-vc-issuance-021001--021099)
    - [2-7. P220 VC Revocation (022001 ~ 022099)](#2-7-p220-vc-revocation-022001--022099)

# Model

## Error Response

### Description

```
Error struct for TA SDK Server. It has code and message pair.
Code starts with SSDKTRA.
```

### Declaration

```java
public class ErrorResponse {
    private final String code;
    private final String message;
}
```

### Property

| Name    | Type   | Description                         | M/O | Note |
|---------|--------|-------------------------------------|-----|------|
| code    | String | Error code. It starts with SSDKTRA  | M   |      |
| message | String | Error description                   | M   |      |

<br>

# Error Code

## 1. SDK Common

### 1-1. Validation (001001 ~ 001099)

| Error Code      | Error Message                   | Description | Action Required |
|-----------------|---------------------------------|-------------|-----------------|
| SSDKTRA001001   | Invalid DID format.             | -           | Verify the DID conforms to the W3C DID specification (e.g., `did:omn:...`). |
| SSDKTRA001002   | Invalid proof.                  | -           | Check the proof object structure; ensure proofValue, verificationMethod, and proofPurpose are present. |
| SSDKTRA001003   | Invalid signature.              | -           | Verify the signature was generated with the correct private key and matches the signed data. |
| SSDKTRA001004   | Invalid multibase encoding.     | -           | Ensure the value is correctly encoded in multibase format (e.g., base58btc with `z` prefix). |
| SSDKTRA001005   | Required field is missing.      | -           | Check the request object and supply all mandatory fields. |
| SSDKTRA001006   | Invalid transaction.            | -           | Verify the txId is correct and that the transaction has not expired. |
| SSDKTRA001007   | Invalid parameter.              | -           | Review the method parameters and correct any null, empty, or out-of-range values. |
| SSDKTRA001008   | Invalid configuration.          | -           | Check `TaSdkConfig`, `WalletConfig`, and `StorageConfig` for missing or incorrect values. |

### 1-2. Protocol (002001 ~ 002099)

| Error Code      | Error Message                          | Description | Action Required |
|-----------------|----------------------------------------|-------------|-----------------|
| SSDKTRA002001   | Invalid protocol step.                 | -           | Call protocol methods in the correct order as defined by the protocol specification. |
| SSDKTRA002002   | Invalid transaction status.            | -           | Ensure the transaction is in the expected status before calling the next protocol step. |
| SSDKTRA002003   | Invalid sub-transaction status.        | -           | Check sub-transaction state; a prior step may have failed or been skipped. |
| SSDKTRA002004   | Transaction has expired.               | -           | The transaction TTL has elapsed. Restart the protocol from the first step. |
| SSDKTRA002005   | Transaction not found.                 | -           | Verify the txId is correct; the transaction may have been deleted or never created. |

### 1-3. Crypto (003001 ~ 003099)

| Error Code      | Error Message                        | Description | Action Required |
|-----------------|--------------------------------------|-------------|-----------------|
| SSDKTRA003001   | Failed to generate signature.        | -           | Check that the wallet is connected and the key ID is valid. |
| SSDKTRA003002   | Failed to verify signature.          | -           | Verify the public key and signed data are correct. |
| SSDKTRA003003   | ECDH key agreement failed.           | -           | Ensure the client public key and curve type are valid and compatible. |
| SSDKTRA003004   | Encryption failed.                   | -           | Check the session key, IV, and cipher algorithm. |
| SSDKTRA003005   | Decryption failed.                   | -           | Check the session key, IV, and cipher algorithm. Ensure the ciphertext has not been tampered with. |
| SSDKTRA003006   | Failed to issue VC.                  | -           | Check VC template, claims data, and signing key availability. |

### 1-4. Blockchain (004001 ~ 004099)

| Error Code      | Error Message                              | Description | Action Required |
|-----------------|--------------------------------------------|-------------|-----------------|
| SSDKTRA004001   | DID Document not found.                    | -           | The DID is not registered on the blockchain or LSS. Register the DID Document first. |
| SSDKTRA004002   | Failed to register DID Document.           | -           | Check blockchain/LSS connectivity and that the InvokedDidDoc is correctly signed. |
| SSDKTRA004003   | Failed to update DID Document.             | -           | Verify the DID exists and the update payload is valid. |
| SSDKTRA004004   | VC Metadata not found.                     | -           | The VC ID is not registered on the blockchain or LSS. |
| SSDKTRA004005   | Blockchain connection failed.              | -           | Check the blockchain/LSS configuration and network connectivity. |
| SSDKTRA004006   | VC has been revoked.                       | -           | The VC is no longer valid. Do not proceed with operations on a revoked VC. |

### 1-5. Wallet (005001 ~ 005099)

| Error Code      | Error Message                       | Description | Action Required |
|-----------------|-------------------------------------|-------------|-----------------|
| SSDKTRA005001   | Failed to connect to wallet.        | -           | Check the wallet file path, password, and read permissions in `WalletConfig`. |
| SSDKTRA005002   | Wallet is not connected.            | -           | Call `ensureWalletConnection()` or verify wallet initialization before signing. |
| SSDKTRA005003   | Failed to create wallet.            | -           | Check write permissions for the wallet file path. |
| SSDKTRA005004   | Key not found in wallet.            | -           | Ensure the key ID exists in the wallet file. |
| SSDKTRA005005   | Failed to generate key.             | -           | Check key generation parameters and available cryptographic providers. |

### 1-6. Provider (006001 ~ 006099)

| Error Code      | Error Message                       | Description | Action Required |
|-----------------|-------------------------------------|-------------|-----------------|
| SSDKTRA006001   | User not found.                     | -           | Verify the user DID is correct and the user has been registered. |
| SSDKTRA006002   | Entity not found.                   | -           | Verify the entity DID or role type and ensure entity enrollment is complete. |
| SSDKTRA006003   | Wallet information not found.       | -           | The wallet has not been registered via P131. Complete wallet registration first. |
| SSDKTRA006004   | ECDH session not found.             | -           | The ECDH session for this transaction does not exist or has expired. |
| SSDKTRA006005   | Token not found.                    | -           | The server token for this transaction does not exist or has expired. |
| SSDKTRA006006   | VC Schema not found.                | -           | Verify the schema ID and ensure it is registered in the `VcSchemaProvider`. |

### 1-7. General (009001 ~ 009999)

| Error Code      | Error Message                  | Description | Action Required |
|-----------------|--------------------------------|-------------|-----------------|
| SSDKTRA009001   | SDK not initialized.           | -           | Ensure `TaSdkConfig` is built and provided to the Protocol implementation constructor. |
| SSDKTRA009999   | Unknown SDK error occurred.    | -           | Inspect the full exception stack trace for root cause details. |

---

## 2. Protocol-Specific

### 2-1. P110 TAS Enrollment (011001 ~ 011099)

| Error Code      | Error Message                                   | Description | Action Required |
|-----------------|-------------------------------------------------|-------------|-----------------|
| SSDKTRA011001   | Password mismatch.                              | -           | The provided password does not match the configured TAS password. Check `TasProvider.getTasPassword()`. |
| SSDKTRA011003   | TAS DID Document not found.                     | -           | The TAS DID Document has not been registered on the blockchain. Register the DID Document before enrolling TAS. |
| SSDKTRA011004   | Failed to generate certificate VC.              | -           | Check the VC template and claims data. Ensure the wallet is connected. |
| SSDKTRA011005   | Failed to sign certificate VC.                  | -           | Verify the signing key exists in the wallet and the VC structure is valid. |
| SSDKTRA011006   | Failed to publish certificate VC.               | -           | Check the `CertificateProvider.publishCertificateVc()` implementation and database connectivity. |
| SSDKTRA011007   | Failed to register VcMeta to blockchain.        | -           | Check blockchain/LSS connectivity and the VcMeta structure. |
| SSDKTRA011008   | Failed to enroll TAS.                           | -           | An unexpected error occurred during TAS enrollment. Inspect the stack trace. |
| SSDKTRA011009   | TAS password not configured.                    | -           | `TasProvider.getTasPassword()` returned null or empty. Configure the TAS password. |

### 2-2. P131 Wallet Registration (013001 ~ 013099)

| Error Code      | Error Message                                           | Description | Action Required |
|-----------------|---------------------------------------------------------|-------------|-----------------|
| SSDKTRA013001   | Wallet Provider not registered.                         | -           | The entity with `WALLET_PROVIDER` role is not found. Register the wallet provider entity first (P120). |
| SSDKTRA013002   | Entity is not in COMPLETED status.                      | -           | The wallet provider entity has not completed enrollment. Complete P120 first. |
| SSDKTRA013003   | Certificate VC not found.                               | -           | The Certificate VC referenced by `certVcRef` does not exist. Ensure TAS enrollment (P110) is complete. |
| SSDKTRA013004   | Provider DID mismatch in Certificate VC.                | -           | The DID in the Certificate VC subject does not match the signing DID. Check entity enrollment. |
| SSDKTRA013005   | Invalid Certificate VC issuer or VC expired.            | -           | The Certificate VC was not issued by the TAS or has passed its expiry date. Re-issue via P120. |
| SSDKTRA013006   | Attestation signature verification failed.              | -           | The `attestedAppInfo` proof is invalid. Verify the CA App signing key. |
| SSDKTRA013007   | WalletId already exists.                                | -           | A wallet with the same `walletId` is already registered. Use a unique walletId. |
| SSDKTRA013008   | DID Document key signature verification failed.         | -           | The key proof in the submitted DID Document is invalid. Check device key signing. |
| SSDKTRA013009   | Failed to create InvokedDidDoc.                         | -           | An error occurred when building the InvokedDidDoc. Inspect the stack trace. |
| SSDKTRA013010   | Failed to register DID Document to blockchain.          | -           | Check blockchain/LSS connectivity and the InvokedDidDoc signature validity. |

### 2-3. P132 User Registration (032001 ~ 032099)

| Error Code      | Error Message                                           | Description | Action Required |
|-----------------|---------------------------------------------------------|-------------|-----------------|
| SSDKTRA032001   | WalletInfo proof signature verification failed.         | -           | The wallet signature on `signedWalletInfo` is invalid. Check the wallet signing key. |
| SSDKTRA032002   | CaAppInfo proof signature verification failed.          | -           | The CA App signature on `attestedAppInfo` is invalid. Check the CA App signing key. |
| SSDKTRA032003   | Failed to sign ServerTokenData.                         | -           | Wallet is not connected or the signing key is not found. |
| SSDKTRA032004   | Failed to encrypt server token data.                    | -           | Check the ECDH session key and encryption parameters. |
| SSDKTRA032005   | Server token is invalid or expired.                     | -           | The `serverToken` does not match the transaction or has expired. Restart from Step 1. |
| SSDKTRA032006   | Failed to retrieve KYC data.                            | -           | Check the KYC server connectivity and `KycProvider` implementation. |
| SSDKTRA032007   | Either kycToken or kycTxId must be provided.            | -           | Supply either `kycToken` or `kycTxId` in the `RetrieveKycRequest`. |
| SSDKTRA032008   | User DID already registered.                            | -           | The DID is already registered. Do not re-register the same DID. |
| SSDKTRA032009   | User with same PII already registered.                  | -           | A user with the same CI value already exists. Each person can only register once. |
| SSDKTRA032010   | SignedDidDoc wallet proof verification failed.          | -           | The wallet signature within `signedDidDoc` is invalid. |
| SSDKTRA032011   | OwnerDidDoc key proof verification failed.              | -           | The owner DID Document key proof is invalid. Check the device signing key. |
| SSDKTRA032012   | Failed to generate User VC.                             | -           | Check the VC claims data, wallet connection, and signing key. |
| SSDKTRA032013   | Failed to register User VC metadata to blockchain.      | -           | Check blockchain/LSS connectivity and VcMeta structure. |

### 2-4. P141 DID Document Update (017001 ~ 017099)

| Error Code      | Error Message                                       | Description | Action Required |
|-----------------|-----------------------------------------------------|-------------|-----------------|
| SSDKTRA017002   | User not found.                                     | -           | The DID supplied in the request is not registered. Verify the user DID. |
| SSDKTRA017003   | User status must be ACTIVATED.                      | -           | The user is not in `ACTIVATED` status. Only active users may update their DID Document. |
| SSDKTRA017011   | Failed to propose update DID Document.              | -           | An unexpected error occurred in `proposeUpdateDidDoc`. Inspect the stack trace. |
| SSDKTRA017012   | Failed to request update DID Document.              | -           | An unexpected error occurred in `requestUpdateDidDoc`. Inspect the stack trace. |
| SSDKTRA017013   | Failed to confirm update DID Document.              | -           | An unexpected error occurred in `confirmUpdateDidDoc`. Inspect the stack trace. |
| SSDKTRA016520   | AuthNonce mismatch.                                 | -           | The `authNonce` in the DID Auth does not match the value issued in Step 1. Restart the protocol. |
| SSDKTRA016516   | DID Document key proof verification failed.         | -           | The key proof in the updated DID Document is invalid. Check device key signing. |
| SSDKTRA012006   | Failed to parse DID Document.                       | -           | The submitted DID Document JSON is malformed. Validate its structure. |
| SSDKTRA012009   | Invalid DID Document version.                       | -           | The `versionId` in the updated DID Document is not greater than the current version. |
| SSDKTRA018006   | Invalid DID Document updated time.                  | -           | The `updated` timestamp in the DID Document is invalid or earlier than the current value. |
| SSDKTRA018007   | Invalid DID Document context.                       | -           | The `@context` field is missing or does not match the required value. |
| SSDKTRA018008   | Invalid DID Document id.                            | -           | The `id` field does not match the user's registered DID. |
| SSDKTRA018009   | Invalid DID Document controller.                    | -           | The `controller` field is missing or does not match the expected DID. |
| SSDKTRA018014   | Invalid DID Document created time.                  | -           | The `created` timestamp does not match the original registration time. |
| SSDKTRA018015   | Invalid DID Document deactivated status.            | -           | The `deactivated` flag is set incorrectly for this operation. |
| SSDKTRA017020   | Failed to generate nonce.                           | -           | An unexpected error occurred during nonce generation. |
| SSDKTRA017021   | Failed to decode multibase.                         | -           | A multibase-encoded field contains an invalid value. |
| SSDKTRA017022   | Proof is missing.                                   | -           | The proof object is absent from the DID Auth or DID Document. |
| SSDKTRA017023   | Invalid verification method.                        | -           | The `verificationMethod` URL in the proof does not exist in the DID Document. |
| SSDKTRA017024   | Invalid proof purpose.                              | -           | The `proofPurpose` in the proof is not one of the accepted values. |
| SSDKTRA017025   | DID mismatch.                                       | -           | The DID extracted from the proof does not match the expected DID. |
| SSDKTRA017026   | DID Document not found.                             | -           | The DID Document could not be retrieved from the blockchain or LSS. |
| SSDKTRA017027   | Public key not found.                               | -           | The public key referenced by the verification method ID is not present in the DID Document. |

### 2-5. P142 DID Document Restore (020001 ~ 020099)

| Error Code      | Error Message                                          | Description | Action Required |
|-----------------|--------------------------------------------------------|-------------|-----------------|
| SSDKTRA020002   | User not found for restore.                            | -           | The DID is not registered. Verify the user DID. |
| SSDKTRA020003   | User status must be DEACTIVATED.                       | -           | Only users in `DEACTIVATED` status can be restored. Check the user's current status. |
| SSDKTRA020004   | Transaction not found for restore.                     | -           | The transaction has expired or does not exist. Restart from `proposeRestoreDidDoc`. |
| SSDKTRA020005   | Failed to create offer.                                | -           | An error occurred while creating the restore Offer. Inspect the stack trace. |
| SSDKTRA020006   | Failed to send push notification.                      | -           | The push notification could not be delivered. Check FCM configuration and the push token. |
| SSDKTRA020007   | Failed to send email.                                  | -           | The email could not be sent. Check SMTP configuration and the recipient address. |
| SSDKTRA020008   | Offer not found.                                       | -           | The Offer referenced by the request does not exist or has been deleted. |
| SSDKTRA020009   | Offer code mismatch.                                   | -           | The offer code submitted by the user does not match the issued code. |
| SSDKTRA020010   | Offer has expired.                                     | -           | The Offer TTL has elapsed. Request a new Offer by restarting the restore protocol. |
| SSDKTRA020011   | Offer has already been used.                           | -           | The Offer has already been consumed. It cannot be reused. |
| SSDKTRA020012   | Deactivated flag must be false for restore.            | -           | The `deactivated` field in the restored DID Document must be `false`. |
| SSDKTRA020013   | Offer transaction ID mismatch.                         | -           | The txId embedded in the Offer does not match the current transaction. |
| SSDKTRA020014   | Invalid offer type.                                    | -           | The `offerType` is not a valid `OfferType` enum value (`PUSH` or `EMAIL`). |
| SSDKTRA020015   | Failed to send offer.                                  | -           | The Offer delivery failed for both PUSH and EMAIL channels. |
| SSDKTRA020021   | Failed to propose restore DID Document.                | -           | An unexpected error occurred in `proposeRestoreDidDoc`. Inspect the stack trace. |
| SSDKTRA020022   | Failed to request issue offer restore DID Document.    | -           | An unexpected error occurred when issuing the restore Offer. Inspect the stack trace. |
| SSDKTRA020023   | Failed to verify offer restore DID Document.           | -           | An unexpected error occurred when verifying the Offer. Inspect the stack trace. |
| SSDKTRA020024   | Failed to request restore DID Document.                | -           | An unexpected error occurred in `requestRestoreDidDoc`. Inspect the stack trace. |
| SSDKTRA020025   | Failed to confirm restore DID Document.                | -           | An unexpected error occurred in `confirmRestoreDidDoc`. Inspect the stack trace. |

### 2-6. P210 VC Issuance (021001 ~ 021099)

| Error Code      | Error Message                            | Description | Action Required |
|-----------------|------------------------------------------|-------------|-----------------|
| SSDKTRA021001   | Transaction not found.                   | -           | The txId does not correspond to any active transaction. Restart from `proposeIssueVc`. |
| SSDKTRA021002   | Transaction invalid state.               | -           | The transaction is not in the expected state for this step. |
| SSDKTRA021003   | Transaction expired.                     | -           | The transaction TTL has elapsed. Restart the VC issuance flow. |
| SSDKTRA021004   | Invalid previous step.                   | -           | The previous protocol step was not completed successfully. Follow the correct step order. |
| SSDKTRA021005   | VC Plan not found.                       | -           | The `vcPlanId` does not exist. Verify the VC Plan is registered in `VcIssuanceDataProvider`. |
| SSDKTRA021006   | VC Plan invalid.                         | -           | The VC Plan configuration is missing required fields or has an invalid state. |
| SSDKTRA021007   | Issuer not found.                        | -           | The Issuer entity for this VC Plan is not registered. Register the Issuer entity (P120). |
| SSDKTRA021009   | Issuer communication failed.             | -           | The HTTP call to the Issuer server failed. Check Issuer server URL and network connectivity. |
| SSDKTRA021015   | Server token validation failed.          | -           | The `serverToken` is invalid or does not match the transaction. |
| SSDKTRA021016   | Issue profile retrieval failed.          | -           | The `GenerateIssueProfile` API call to the Issuer returned an error. Check Issuer server logs. |
| SSDKTRA021017   | VC schema validation failed.             | -           | The submitted claim data does not conform to the VC Schema. Check required claim fields. |
| SSDKTRA021018   | VP decryption failed.                    | -           | The encrypted VP could not be decrypted. Check the E2E session key. |
| SSDKTRA021019   | VP signature verification failed.        | -           | The VP holder signature is invalid. Check holder signing key. |
| SSDKTRA021020   | VC issuance failed.                      | -           | The Issuer returned an error during VC issuance. Check Issuer server logs. |
| SSDKTRA021021   | VC encryption failed.                    | -           | The issued VC could not be encrypted for the holder. Check E2E session key. |
| SSDKTRA021022   | VC ID mismatch.                          | -           | The VC ID returned by the Issuer does not match the expected value. |
| SSDKTRA021023   | VC metadata registration failed.         | -           | Failed to register VC metadata on the blockchain or LSS. |
| SSDKTRA021024   | Holder not found.                        | -           | The holder DID is not registered. The user must complete P132 first. |
| SSDKTRA021025   | Push token not found.                    | -           | No FCM push token is registered for this holder. The user must register a push token. |

### 2-7. P220 VC Revocation (022001 ~ 022099)

| Error Code      | Error Message                                        | Description | Action Required |
|-----------------|------------------------------------------------------|-------------|-----------------|
| SSDKTRA022001   | VC is already revoked.                               | -           | The VC has already been revoked. No further action is needed. |
| SSDKTRA022002   | VC is expired.                                       | -           | The VC has passed its expiry date and cannot be revoked through this flow. |
| SSDKTRA022003   | VC status is invalid.                                | -           | The VC is in an unexpected state. Check the VC metadata on the blockchain. |
| SSDKTRA022004   | Failed to fetch VC metadata from blockchain.         | -           | Check blockchain/LSS connectivity and that the VC ID is correct. |
| SSDKTRA022005   | Issuer nonce mismatch.                               | -           | The `issuerNonce` in the revocation request does not match the value issued by the Issuer. |
| SSDKTRA022006   | Signature is invalid.                                | -           | The PIN-based signature on the revocation request is invalid. Check the holder PIN and nonce. |
| SSDKTRA022007   | Failed to propose revoke VC.                         | -           | An unexpected error occurred in `proposeRevokeVc`. Inspect the stack trace. |
| SSDKTRA022008   | Failed to request revoke VC.                         | -           | An unexpected error occurred in `requestRevokeVc`. Inspect the stack trace. |
| SSDKTRA022009   | Failed to confirm revoke VC.                         | -           | An unexpected error occurred in `confirmRevokeVc`. Inspect the stack trace. |
