package org.omnione.did.tas.config;

import lombok.Getter;
import org.omnione.did.common.util.blockchain.BlockChainConfig;
import org.omnione.did.common.util.lss.LssConfig;

/**
 * Configuration class for storage (blockchain or LSS) settings.
 *
 * <p>This class manages storage configuration for DID Document and VC metadata
 * persistence. Supports both blockchain (Fabric/EVM) and LSS (Ledger Service Server).</p>
 *
 * <p><b>Storage Types:</b></p>
 * <ul>
 *   <li>BLOCKCHAIN: Hyperledger Fabric or Ethereum-compatible blockchain</li>
 *   <li>LSS: Ledger Service Server (centralized alternative)</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * // Blockchain storage
 * BlockChainConfig bcConfig = new BlockChainConfig(
 *     "/path/to/blockchain.properties",
 *     RoleType.TAS,
 *     BlockChainConfig.BlockChainType.FABRIC
 * );
 * StorageConfig config = StorageConfig.forBlockchain(bcConfig);
 *
 * // LSS storage
 * LssConfig lssConfig = new LssConfig("https://lss.example.com", "api-key");
 * StorageConfig config = StorageConfig.forLss(lssConfig);
 * </pre>
 */
@Getter
public class StorageConfig {

    /**
     * Storage type enumeration
     */
    public enum StorageType {
        /**
         * Blockchain storage (Fabric or EVM)
         */
        BLOCKCHAIN,

        /**
         * LSS (Ledger Service Server) storage
         */
        LSS
    }

    /**
     * Storage type
     */
    private final StorageType storageType;

    /**
     * Blockchain configuration (only for BLOCKCHAIN type)
     */
    private final BlockChainConfig blockChainConfig;

    /**
     * LSS configuration (only for LSS type)
     */
    private final LssConfig lssConfig;

    /**
     * Create storage configuration for blockchain.
     *
     * @param blockChainConfig Blockchain configuration (must not be null)
     * @throws IllegalArgumentException if blockChainConfig is null
     */
    private StorageConfig(BlockChainConfig blockChainConfig) {
        if (blockChainConfig == null) {
            throw new IllegalArgumentException("BlockChainConfig must not be null");
        }

        this.storageType = StorageType.BLOCKCHAIN;
        this.blockChainConfig = blockChainConfig;
        this.lssConfig = null;
    }

    /**
     * Create storage configuration for LSS.
     *
     * @param lssConfig LSS configuration (must not be null)
     * @throws IllegalArgumentException if lssConfig is null
     */
    private StorageConfig(LssConfig lssConfig) {
        if (lssConfig == null) {
            throw new IllegalArgumentException("LssConfig must not be null");
        }

        this.storageType = StorageType.LSS;
        this.blockChainConfig = null;
        this.lssConfig = lssConfig;
    }

    /**
     * Create storage configuration for blockchain.
     *
     * @param blockChainConfig Blockchain configuration
     * @return StorageConfig instance
     * @throws IllegalArgumentException if blockChainConfig is null
     */
    public static StorageConfig forBlockchain(BlockChainConfig blockChainConfig) {
        return new StorageConfig(blockChainConfig);
    }

    /**
     * Create storage configuration for LSS.
     *
     * @param lssConfig LSS configuration
     * @return StorageConfig instance
     * @throws IllegalArgumentException if lssConfig is null
     */
    public static StorageConfig forLss(LssConfig lssConfig) {
        return new StorageConfig(lssConfig);
    }

    /**
     * Check if storage type is BLOCKCHAIN.
     *
     * @return true if storage type is BLOCKCHAIN
     */
    public boolean isBlockchain() {
        return storageType == StorageType.BLOCKCHAIN;
    }

    /**
     * Check if storage type is LSS.
     *
     * @return true if storage type is LSS
     */
    public boolean isLss() {
        return storageType == StorageType.LSS;
    }
}
