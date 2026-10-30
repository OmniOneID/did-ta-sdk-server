package org.omnione.did.tas.config;

import lombok.Getter;

/**
 * Configuration class for cryptographic algorithm settings.
 *
 * <p>This class manages cryptographic algorithm preferences
 * for signature generation, hashing, and key operations.</p>
 *
 * <p><b>Default Values:</b></p>
 * <ul>
 *   <li>Signature Algorithm: Secp256r1</li>
 *   <li>Hash Algorithm: SHA-256</li>
 *   <li>Key Size: 256 bits</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * // Use defaults
 * CryptoConfig config = new CryptoConfig();
 *
 * // Or customize
 * CryptoConfig config = CryptoConfig.builder()
 *     .signatureAlgorithm("Secp256k1")
 *     .hashAlgorithm("SHA-512")
 *     .keySize(512)
 *     .build();
 * </pre>
 */
@Getter
public class CryptoConfig {

    /**
     * Default signature algorithm
     */
    public static final String DEFAULT_SIGNATURE_ALGORITHM = "Secp256r1";

    /**
     * Default hash algorithm
     */
    public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";

    /**
     * Default key size (bits)
     */
    public static final int DEFAULT_KEY_SIZE = 256;

    /**
     * Signature algorithm
     */
    private final String signatureAlgorithm;

    /**
     * Hash algorithm
     */
    private final String hashAlgorithm;

    /**
     * Key size in bits
     */
    private final int keySize;

    /**
     * Create crypto configuration with default values.
     */
    public CryptoConfig() {
        this(DEFAULT_SIGNATURE_ALGORITHM, DEFAULT_HASH_ALGORITHM, DEFAULT_KEY_SIZE);
    }

    /**
     * Create crypto configuration with custom values.
     *
     * @param signatureAlgorithm Signature algorithm (e.g., "Secp256r1", "Secp256k1")
     * @param hashAlgorithm Hash algorithm (e.g., "SHA-256", "SHA-512")
     * @param keySize Key size in bits (must be positive)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public CryptoConfig(String signatureAlgorithm, String hashAlgorithm, int keySize) {
        validateSignatureAlgorithm(signatureAlgorithm);
        validateHashAlgorithm(hashAlgorithm);
        validateKeySize(keySize);

        this.signatureAlgorithm = signatureAlgorithm;
        this.hashAlgorithm = hashAlgorithm;
        this.keySize = keySize;
    }

    /**
     * Create a new builder.
     *
     * @return Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Validate signature algorithm.
     *
     * @param signatureAlgorithm Signature algorithm to validate
     * @throws IllegalArgumentException if signatureAlgorithm is null or empty
     */
    private void validateSignatureAlgorithm(String signatureAlgorithm) {
        if (signatureAlgorithm == null || signatureAlgorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("Signature algorithm must not be null or empty");
        }
    }

    /**
     * Validate hash algorithm.
     *
     * @param hashAlgorithm Hash algorithm to validate
     * @throws IllegalArgumentException if hashAlgorithm is null or empty
     */
    private void validateHashAlgorithm(String hashAlgorithm) {
        if (hashAlgorithm == null || hashAlgorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("Hash algorithm must not be null or empty");
        }
    }

    /**
     * Validate key size.
     *
     * @param keySize Key size to validate
     * @throws IllegalArgumentException if keySize is not positive
     */
    private void validateKeySize(int keySize) {
        if (keySize <= 0) {
            throw new IllegalArgumentException("Key size must be positive");
        }
    }

    /**
     * Builder for CryptoConfig.
     */
    public static class Builder {
        private String signatureAlgorithm = DEFAULT_SIGNATURE_ALGORITHM;
        private String hashAlgorithm = DEFAULT_HASH_ALGORITHM;
        private int keySize = DEFAULT_KEY_SIZE;

        private Builder() {
        }

        /**
         * Set signature algorithm.
         *
         * @param signatureAlgorithm Signature algorithm
         * @return Builder instance
         */
        public Builder signatureAlgorithm(String signatureAlgorithm) {
            this.signatureAlgorithm = signatureAlgorithm;
            return this;
        }

        /**
         * Set hash algorithm.
         *
         * @param hashAlgorithm Hash algorithm
         * @return Builder instance
         */
        public Builder hashAlgorithm(String hashAlgorithm) {
            this.hashAlgorithm = hashAlgorithm;
            return this;
        }

        /**
         * Set key size.
         *
         * @param keySize Key size in bits
         * @return Builder instance
         */
        public Builder keySize(int keySize) {
            this.keySize = keySize;
            return this;
        }

        /**
         * Build CryptoConfig instance.
         *
         * @return CryptoConfig instance
         * @throws IllegalArgumentException if validation fails
         */
        public CryptoConfig build() {
            return new CryptoConfig(signatureAlgorithm, hashAlgorithm, keySize);
        }
    }
}
