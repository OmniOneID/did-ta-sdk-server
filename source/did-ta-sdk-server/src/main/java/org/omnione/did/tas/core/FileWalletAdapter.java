package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.common.datamodel.enums.crypto.EccCurveType;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.service.impl.wallet.BaseWalletServiceImpl;
import org.omnione.did.tas.config.WalletConfig;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;

/**
 * File-based wallet adapter for TA SDK.
 *
 * <p>This class extends did-common-sdk's BaseWalletServiceImpl and provides
 * TA-specific wallet initialization with default keys.</p>
 *
 * <p><b>Default Keys Initialized:</b></p>
 * <ul>
 *   <li>tas-assert-key - TAS assertion key</li>
 *   <li>tas-auth-key - TAS authentication key</li>
 *   <li>tas-keyagree-key - TAS key agreement key</li>
 *   <li>tas-invoke-key - TAS invocation key</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * WalletConfig config = WalletConfig.builder()
 *     .filePath("/path/to/wallet.dat")
 *     .password("wallet-password")
 *     .build();
 *
 * FileWalletAdapter adapter = new FileWalletAdapter(config);
 * adapter.initialize();
 *
 * // Sign data
 * byte[] signature = adapter.sign("tas-assert-key", dataToSign);
 * }
 * </pre>
 */
@Slf4j
public class FileWalletAdapter extends BaseWalletServiceImpl {

    private final WalletConfig walletConfig;

    /**
     * Create FileWalletAdapter with wallet configuration.
     *
     * @param walletConfig Wallet configuration (file path and password)
     */
    public FileWalletAdapter(WalletConfig walletConfig) {
        this.walletConfig = walletConfig;
    }

    /**
     * Initialize wallet (load existing wallet).
     *
     * <p>Loads existing wallet file. Wallet must be created beforehand.</p>
     *
     * @throws TaSdkException if initialization fails
     */
    public void initialize() {
        try {
            log.info("Initializing FileWalletAdapter: {}", walletConfig.getFilePath());

            this.walletManager = loadWallet(
                walletConfig.getFilePath(),
                walletConfig.getPassword()
            );

            log.info("Wallet loaded successfully: {}", walletConfig.getFilePath());

        } catch (CommonSdkException e) {
            log.error("Failed to load wallet: {}", walletConfig.getFilePath(), e);
            throw new TaSdkException(TaSdkErrorCode.SDK_WALLET_CONNECTION_FAILED, e);
        }
    }

    /**
     * Initialize default TAS keys.
     *
     * <p>This method is called automatically when creating a new wallet.</p>
     *
     * @throws CommonSdkException if key generation fails
     */
    @Override
    protected void initializeDefaultKeys() throws CommonSdkException {
        log.info("Initializing default TAS keys");

        // Generate TAS default keys (SECP256R1 curve)
        String[] defaultKeys = {"assert", "auth", "keyagree", "invoke"};

        for (String keyId : defaultKeys) {
            generateKey(keyId, EccCurveType.SECP256R1);
            log.debug("Generated default key: {}", keyId);
        }

        log.info("Default TAS keys initialized successfully");
    }

    /**
     * Get underlying WalletManagerInterface for low-level operations.
     *
     * <p>Required by components that need direct wallet access (e.g., DidDocumentBuilder
     * for key extraction and document signing via BaseCoreDidUtil).</p>
     *
     * @return WalletManagerInterface instance
     * @throws TaSdkException if wallet is not initialized
     */
    public org.omnione.did.wallet.key.WalletManagerInterface getWalletManager() {
        if (walletManager == null) {
            throw new TaSdkException(TaSdkErrorCode.SDK_NOT_INITIALIZED,
                "Wallet not initialized. Call initialize() first.");
        }
        return walletManager;
    }

    /**
     * Get wallet configuration.
     *
     * @return WalletConfig instance
     */
    public WalletConfig getWalletConfig() {
        return walletConfig;
    }
}
