package org.omnione.did.tas.protocol.wallet;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.wallet.RegisterWalletRequest;
import org.omnione.did.tas.model.response.protocol.wallet.RegisterWalletResponse;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P131: 월렛 등록 프로토콜 인터페이스.
 *
 * <p>Wallet Provider가 생성한 Wallet DID Document를 검증하고
 * 블록체인에 등록하는 단일 단계 프로토콜이다.</p>
 *
 * <p><b>Protocol characteristics:</b></p>
 * <ul>
 *   <li>1-step protocol (registerWallet)</li>
 *   <li>No ECDH (signature verification only)</li>
 *   <li>No server token</li>
 *   <li>3-layer signature verification (Provider → DID Keys → TAS)</li>
 * </ul>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * registerWallet()
 *   1. Validate Wallet Provider (EntityProvider)
 *      - Entity role == WALLET_PROVIDER
 *      - Entity status == COMPLETED
 *      - (optional) Certificate VC validation (CertificateProvider)
 *   2. Validate Attestation Proof (Provider signature)
 *      - DID format check
 *      - verificationMethod format check
 *      - proofPurpose == ASSERTION_METHOD
 *      - Signature verification with Provider's assert key
 *   3. Check WalletId duplicate (WalletRegistrationProvider)
 *   4. Parse and verify OwnerDidDoc
 *      - Multibase decode
 *      - Key Proofs verification (each key's self-signature)
 *   5. Create InvokedDidDoc (TAS signs the clean DID Document)
 *   6. Register DID Document to blockchain (StorageProvider)
 *   → Server saves Wallet info and creates Transaction (audit)
 * </pre>
 *
 * <p><b>Usage example (server-side):</b></p>
 * <pre>
 * {@code
 * RegisterWalletResponse response = walletRegistrationProtocol.registerWallet(
 *     RegisterWalletRequest.builder()
 *         .id("msg-001")
 *         .attestedDidDoc(attestedDidDoc)
 *         .build()
 * );
 * // Server saves Wallet(walletId, did=response.getWalletDid(), provider)
 * // Server creates Transaction(type=WALLET_REGISTRATION, status=COMPLETED)
 * }
 * </pre>
 */
public interface WalletRegistrationProtocol extends Protocol {

    /**
     * Register Wallet DID Document to blockchain.
     *
     * <p>Validates the Wallet Provider's attestation and the Wallet's DID Document,
     * then creates an InvokedDidDoc (TAS-signed) and registers it to the blockchain.</p>
     *
     * <p><b>SDK responsibilities:</b></p>
     * <ul>
     *   <li>Validate Wallet Provider entity (role, status)</li>
     *   <li>Optionally validate Provider Certificate VC</li>
     *   <li>Validate Attestation Proof (Provider's signature over AttestedDidDoc)</li>
     *   <li>Check WalletId duplicate</li>
     *   <li>Verify OwnerDidDoc key proofs (self-signatures)</li>
     *   <li>Create InvokedDidDoc with TAS signature</li>
     *   <li>Register to blockchain</li>
     * </ul>
     *
     * <p><b>Server responsibilities (after SDK returns):</b></p>
     * <ul>
     *   <li>Save Wallet record (walletId, walletDid, entityId, status=CREATED)</li>
     *   <li>Create Transaction (type=WALLET_REGISTRATION, status=COMPLETED)</li>
     * </ul>
     *
     * @param request RegisterWalletRequest containing the AttestedDidDoc
     * @return RegisterWalletResponse containing the registered Wallet DID
     * @throws TaSdkException
     *         <ul>
     *           <li>SSDKTRA13001 - Wallet Provider not registered or invalid role</li>
     *           <li>SSDKTRA13002 - Entity not in COMPLETED status</li>
     *           <li>SSDKTRA13003 - Certificate VC not found</li>
     *           <li>SSDKTRA13004 - Provider DID mismatch</li>
     *           <li>SSDKTRA13005 - Invalid Certificate VC issuer or expired</li>
     *           <li>SSDKTRA13006 - Attestation signature verification failed</li>
     *           <li>SSDKTRA13007 - WalletId already exists</li>
     *           <li>SSDKTRA13008 - DID Document key signature verification failed</li>
     *           <li>SSDKTRA13009 - InvokedDidDoc creation failed</li>
     *           <li>SSDKTRA13010 - DID Document blockchain registration failed</li>
     *         </ul>
     */
    RegisterWalletResponse registerWallet(RegisterWalletRequest request) throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P131";
    }

    @Override
    default String getProtocolName() {
        return "Wallet Registration Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
