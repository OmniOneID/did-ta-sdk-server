package org.omnione.did.tas.provider;

import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.vc.VerifiableCredential;

/**
 * Provider interface for wallet signing operations.
 *
 * <p>The TA Server implements this interface to perform cryptographic signing
 * using the TAS wallet. The SDK delegates signing to the server to keep wallet
 * credentials (password, path) on the server side.</p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *   <li>P110 TAS 등록 - signs Certificate VC with TAS assert key</li>
 *   <li>P120 Entity 등록 - signs Entity Certificate VC with TAS assert key</li>
 *   <li>P131 Wallet 등록 - signs InvokedDidDoc with TAS invoke key</li>
 * </ul>
 *
 * <p><b>Server-side implementation example:</b></p>
 * <pre>
 * {@code
 * @Service
 * public class WalletProviderImpl implements WalletProvider {
 *     @Override
 *     public void signVc(VerifiableCredential vc, DidDocument didDoc, String keyPurpose) {
 *         fileWalletAdapter.ensureConnection();
 *         VerifiableCredential signed = BaseCoreVcUtil.signVerifiableCredential(
 *             new VcManager(), fileWalletAdapter.getWalletManager(), didDoc, keyPurpose, vc);
 *         vc.setProof(signed.getProof());
 *     }
 * }
 * }
 * </pre>
 */
public interface WalletProvider {

    /**
     * Sign a VC with the TAS private key.
     *
     * <p>Signs the VC in-place: after this call, {@code vc.getProof()} returns
     * the generated proof. The caller does not need to assign the return value.</p>
     *
     * @param vc         VC to sign (modified in-place with proof)
     * @param didDoc     DID Document of the issuer (TAS DID Document)
     * @param keyPurpose Key purpose to use for signing (e.g., "assert")
     * @throws org.omnione.did.tas.exception.TaSdkException if signing fails
     */
    void signVc(VerifiableCredential vc, DidDocument didDoc, String keyPurpose);

    /**
     * Sign an InvokedDidDoc with the TAS private key.
     *
     * <p>Signs the InvokedDidDoc using the specified key. The unsigned doc already contains
     * the proof header (type, purpose, verificationMethod) set by
     * {@link org.omnione.did.common.domain.did.DidDocCryptoHelper#generateInvokedDidDoc}.
     * This method fills in the proofValue.</p>
     *
     * <p><b>Server-side implementation example:</b></p>
     * <pre>
     * {@code
     * @Override
     * public InvokedDidDoc signInvokedDidDoc(InvokedDidDoc unsignedDoc, String keyId) {
     *     return DidDocCryptoHelper.signInvokedDidDoc(unsignedDoc, walletManager, keyId);
     * }
     * }
     * </pre>
     *
     * @param unsignedInvokedDidDoc Unsigned InvokedDidDoc (proof without proofValue)
     * @param keyId                 Key ID to sign with (e.g., "invoke")
     * @return Signed InvokedDidDoc with proofValue set
     * @throws org.omnione.did.tas.exception.TaSdkException if signing fails
     */
    InvokedDidDoc signInvokedDidDoc(InvokedDidDoc unsignedInvokedDidDoc, String keyId);

    /**
     * Sign arbitrary data with the TAS private key.
     *
     * <p>The SDK generates the signature message (canonical hash of the data),
     * then delegates the actual signing to the server via this method.
     * The server signs the raw bytes using the wallet's private key identified by keyId.</p>
     *
     * <p><b>Server-side implementation example:</b></p>
     * <pre>
     * {@code
     * @Override
     * public String sign(byte[] message, String keyId) {
     *     byte[] signature = walletManager.generateCompactSignature(keyId, message);
     *     return BaseMultibaseUtil.encode(signature);
     * }
     * }
     * </pre>
     *
     * @param message Data to sign (signature message bytes, typically SHA256 hash)
     * @param keyId   Key ID to sign with (e.g., "assert", "invoke")
     * @return multibase-encoded signature bytes (proofValue)
     * @throws org.omnione.did.tas.exception.TaSdkException if signing fails
     */
    String sign(byte[] message, String keyId);

    /**
     * Check wallet connection status.
     *
     * @return true if wallet is connected and ready for signing
     */
    boolean isWalletConnected();

    /**
     * Ensure wallet is connected before signing.
     *
     * <p>If the wallet is not connected, this method attempts to reconnect.
     * Throws if reconnection fails.</p>
     *
     * @throws org.omnione.did.tas.exception.TaSdkException if wallet connection fails
     */
    void ensureWalletConnection();
}
