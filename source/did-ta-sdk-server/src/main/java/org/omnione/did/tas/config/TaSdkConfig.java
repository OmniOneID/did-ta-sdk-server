package org.omnione.did.tas.config;

import lombok.Getter;

/**
 * Main configuration class for TA SDK.
 *
 * <p>This class serves as a container for all SDK configurations including
 * wallet, cryptographic, and storage settings.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * // Create individual configurations
 * WalletConfig walletConfig = new WalletConfig("/path/to/wallet.dat", "password");
 * CryptoConfig cryptoConfig = new CryptoConfig();  // Use defaults
 * StorageConfig storageConfig = StorageConfig.forBlockchain(blockchainConfig);
 *
 * // Create SDK configuration
 * TaSdkConfig sdkConfig = TaSdkConfig.builder()
 *     .walletConfig(walletConfig)
 *     .cryptoConfig(cryptoConfig)
 *     .storageConfig(storageConfig)
 *     .build();
 * </pre>
 */
@Getter
public class TaSdkConfig {

    /**
     * Wallet configuration
     */
    private final WalletConfig walletConfig;

    /**
     * Cryptographic configuration
     */
    private final CryptoConfig cryptoConfig;

    /**
     * Storage configuration
     */
    private final StorageConfig storageConfig;

    /**
     * Create TA SDK configuration.
     *
     * @param walletConfig Wallet configuration (must not be null)
     * @param cryptoConfig Cryptographic configuration (optional, defaults will be used if null)
     * @param storageConfig Storage configuration (must not be null)
     * @throws IllegalArgumentException if walletConfig or storageConfig is null
     */
    public TaSdkConfig(WalletConfig walletConfig, CryptoConfig cryptoConfig, StorageConfig storageConfig) {
        validateWalletConfig(walletConfig);
        validateStorageConfig(storageConfig);

        this.walletConfig = walletConfig;
        this.cryptoConfig = cryptoConfig != null ? cryptoConfig : new CryptoConfig();
        this.storageConfig = storageConfig;
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
     * Validate wallet configuration.
     *
     * @param walletConfig Wallet configuration to validate
     * @throws IllegalArgumentException if walletConfig is null
     */
    private void validateWalletConfig(WalletConfig walletConfig) {
        if (walletConfig == null) {
            throw new IllegalArgumentException("WalletConfig must not be null");
        }
    }

    /**
     * Validate storage configuration.
     *
     * @param storageConfig Storage configuration to validate
     * @throws IllegalArgumentException if storageConfig is null
     */
    private void validateStorageConfig(StorageConfig storageConfig) {
        if (storageConfig == null) {
            throw new IllegalArgumentException("StorageConfig must not be null");
        }
    }

    /**
     * Builder for TaSdkConfig.
     */
    public static class Builder {
        private WalletConfig walletConfig;
        private CryptoConfig cryptoConfig;
        private StorageConfig storageConfig;

        private Builder() {
        }

        /**
         * Set wallet configuration.
         *
         * @param walletConfig Wallet configuration
         * @return Builder instance
         */
        public Builder walletConfig(WalletConfig walletConfig) {
            this.walletConfig = walletConfig;
            return this;
        }

        /**
         * Set cryptographic configuration.
         *
         * @param cryptoConfig Cryptographic configuration
         * @return Builder instance
         */
        public Builder cryptoConfig(CryptoConfig cryptoConfig) {
            this.cryptoConfig = cryptoConfig;
            return this;
        }

        /**
         * Set storage configuration.
         *
         * @param storageConfig Storage configuration
         * @return Builder instance
         */
        public Builder storageConfig(StorageConfig storageConfig) {
            this.storageConfig = storageConfig;
            return this;
        }

        /**
         * Build TaSdkConfig instance.
         *
         * @return TaSdkConfig instance
         * @throws IllegalArgumentException if validation fails
         */
        public TaSdkConfig build() {
            return new TaSdkConfig(walletConfig, cryptoConfig, storageConfig);
        }
    }
}
