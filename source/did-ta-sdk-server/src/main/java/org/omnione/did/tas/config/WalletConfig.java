package org.omnione.did.tas.config;

import lombok.Getter;

/**
 * Configuration class for wallet file settings.
 *
 * <p>This class manages wallet file path and password configuration
 * required for cryptographic key operations in the TA SDK.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * WalletConfig config = new WalletConfig(
 *     "/path/to/wallet.dat",
 *     "wallet-password"
 * );
 *
 * // Or use builder
 * WalletConfig config = WalletConfig.builder()
 *     .filePath("/path/to/wallet.dat")
 *     .password("wallet-password")
 *     .build();
 * </pre>
 */
@Getter
public class WalletConfig {

    /**
     * Wallet file path (required)
     */
    private final String filePath;

    /**
     * Wallet password (required)
     */
    private final String password;

    /**
     * Create wallet configuration.
     *
     * @param filePath Wallet file path (must not be null or empty)
     * @param password Wallet password (must not be null or empty)
     * @throws IllegalArgumentException if filePath or password is null or empty
     */
    public WalletConfig(String filePath, String password) {
        validateFilePath(filePath);
        validatePassword(password);

        this.filePath = filePath;
        this.password = password;
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
     * Validate file path.
     *
     * @param filePath File path to validate
     * @throws IllegalArgumentException if filePath is null or empty
     */
    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet file path must not be null or empty");
        }
    }

    /**
     * Validate password.
     *
     * @param password Password to validate
     * @throws IllegalArgumentException if password is null or empty
     */
    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Wallet password must not be null or empty");
        }
    }

    /**
     * Builder for WalletConfig.
     */
    public static class Builder {
        private String filePath;
        private String password;

        private Builder() {
        }

        /**
         * Set wallet file path.
         *
         * @param filePath Wallet file path
         * @return Builder instance
         */
        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * Set wallet password.
         *
         * @param password Wallet password
         * @return Builder instance
         */
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Build WalletConfig instance.
         *
         * @return WalletConfig instance
         * @throws IllegalArgumentException if validation fails
         */
        public WalletConfig build() {
            return new WalletConfig(filePath, password);
        }
    }
}
