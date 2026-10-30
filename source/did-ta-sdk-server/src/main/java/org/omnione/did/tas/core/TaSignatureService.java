package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.did.ProofPurpose;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.service.impl.signature.BaseSignatureServiceImpl;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;

import java.time.Instant;

/**
 * TA SDK Signature Service.
 *
 * <p>Extends {@link BaseSignatureServiceImpl} to provide TA-specific signature operations
 * using the file-based wallet for key access.</p>
 *
 * <p><b>Inherited from BaseSignatureServiceImpl (no re-implementation needed):</b></p>
 * <ul>
 *   <li>{@code generateProof()} - Creates a Proof object by signing data</li>
 *   <li>{@code verifyProof()} - Verifies a Proof against a DID Document</li>
 *   <li>{@code verifyDidDocumentProof()} - Verifies all key proofs in a DID Document</li>
 * </ul>
 *
 * <p><b>ProofPurpose to Key ID mapping (TAS standard):</b></p>
 * <ul>
 *   <li>ASSERTION_METHOD      → {@code "assert"}</li>
 *   <li>AUTHENTICATION        → {@code "auth"}</li>
 *   <li>CAPABILITY_INVOCATION → {@code "invoke"}</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * TaSignatureService signatureService = new TaSignatureService(fileWalletAdapter);
 *
 * // Generate a proof for data
 * Proof proof = signatureService.generateProof(
 *     data,
 *     ProofPurpose.ASSERTION_METHOD,
 *     ProofType.SECP_256R1_SIGNATURE_2018,
 *     "did:omn:tas#assert"
 * );
 *
 * // Verify a proof
 * signatureService.verifyProof(data, proof, issuerDidDocument);
 * }
 * </pre>
 */
@Slf4j
public class TaSignatureService extends BaseSignatureServiceImpl {

    private final FileWalletAdapter fileWalletAdapter;

    /**
     * Create TaSignatureService with the TA's file wallet adapter.
     *
     * @param fileWalletAdapter Initialized file wallet adapter for signing operations
     */
    public TaSignatureService(FileWalletAdapter fileWalletAdapter) {
        this.fileWalletAdapter = fileWalletAdapter;
    }

    /**
     * Sign data using the wallet key corresponding to the given ProofPurpose.
     *
     * <p>Maps ProofPurpose to the TAS wallet key ID and delegates signing
     * to {@link FileWalletAdapter#sign(String, byte[])}.</p>
     *
     * <p>KEY_AGREEMENT is not a signing purpose and will throw an exception.</p>
     *
     * @param data          Data to sign (pre-hashed signature message)
     * @param proofPurpose  Purpose of the proof (determines which key to use)
     * @return Compact signature bytes
     * @throws CommonSdkException       if signing fails
     * @throws TaSdkException           if KEY_AGREEMENT is passed as proofPurpose
     */
    @Override
    protected byte[] signData(byte[] data, ProofPurpose proofPurpose) throws CommonSdkException {
        if (proofPurpose == ProofPurpose.KEY_AGREEMENT) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_PARAMETER,
                "KEY_AGREEMENT is not a signing purpose");
        }

        // ProofPurpose.toKeyId() maps: ASSERTION_METHOD→"assert", AUTHENTICATION→"auth",
        // CAPABILITY_INVOCATION→"invoke"
        String keyId = proofPurpose.toKeyId();
        log.debug("Signing data with key: {}", keyId);

        return fileWalletAdapter.sign(keyId, data);
    }

    /**
     * Get current UTC time in ISO-8601 format.
     *
     * @return Current UTC timestamp (e.g. "2026-02-19T06:30:00Z")
     */
    @Override
    protected String getCurrentUTCTime() {
        return Instant.now().toString();
    }
}
