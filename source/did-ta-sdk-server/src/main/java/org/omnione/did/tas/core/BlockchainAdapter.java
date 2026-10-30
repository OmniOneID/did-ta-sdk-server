package org.omnione.did.tas.core;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.ContractApi;
import org.omnione.did.common.exception.CommonSdkException;
import org.omnione.did.common.util.blockchain.BaseBlockChainUtil;
import org.omnione.did.common.util.blockchain.BlockChainConfig;
import org.omnione.did.data.model.did.DidDocAndStatus;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.enums.did.DidDocStatus;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.tas.config.StorageConfig;
import org.omnione.did.tas.exception.TaSdkErrorCode;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.exception.BlockChainException;

/**
 * Blockchain adapter for TA SDK.
 *
 * <p>This class provides unified interface for blockchain (Fabric/Besu) operations
 * using OmniOne SDK's ContractApi.</p>
 *
 * <p><b>Supported Backends:</b></p>
 * <ul>
 *   <li>Hyperledger Fabric - did-blockchain-sdk-server (Fabric Gateway Java SDK)</li>
 *   <li>Hyperledger Besu (EVM) - did-blockchain-sdk-server (Web3j)</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * {@code
 * StorageConfig config = StorageConfig.forBlockchain(blockChainConfig);
 * BlockchainAdapter adapter = new BlockchainAdapter(config);
 * adapter.initialize();
 *
 * // Register DID Document
 * adapter.registerDidDocument(invokedDidDoc, RoleType.TAS);
 *
 * // Retrieve DID Document
 * DidDocument didDoc = adapter.getDidDocument(did);
 * }
 * </pre>
 */
@Slf4j
public class BlockchainAdapter {

    private final StorageConfig storageConfig;
    private ContractApi contractApi;
    private boolean initialized = false;

    public BlockchainAdapter(StorageConfig storageConfig) {
        if (!storageConfig.isBlockchain()) {
            throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_CONFIGURATION,
                "StorageConfig must be configured for blockchain");
        }
        this.storageConfig = storageConfig;
    }

    /**
     * Initialize blockchain connection.
     *
     * @throws TaSdkException if initialization fails
     */
    public void initialize() {
        if (initialized) {
            log.warn("BlockchainAdapter already initialized");
            return;
        }

        try {
            log.info("Initializing blockchain adapter");

            // Initialize ContractApi using ContractFactory
            // Note: TA Server configuration determines Fabric vs EVM
            this.contractApi = initializeContractApi();

            initialized = true;
            log.info("BlockchainAdapter initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize BlockchainAdapter", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_BLOCKCHAIN_CONNECTION_FAILED, e);
        }
    }

    /**
     * Initialize ContractApi from blockchain configuration.
     *
     * @return ContractApi instance
     */
    private ContractApi initializeContractApi() {
        try {
            BlockChainConfig config = storageConfig.getBlockChainConfig();
            return BaseBlockChainUtil.createContractApi(config);
        } catch (CommonSdkException e) {
            throw new TaSdkException(TaSdkErrorCode.SDK_BLOCKCHAIN_CONNECTION_FAILED, e);
        }
    }

    /**
     * Register DID Document to blockchain (using InvokedDidDoc).
     *
     * @param invokedDidDoc InvokedDidDoc containing DID Document and signature
     * @param roleType Role type (TAS, ISSUER, VERIFIER, etc.)
     * @throws TaSdkException if registration fails
     */
    public void registerDidDocument(InvokedDidDoc invokedDidDoc, RoleType roleType) {
        ensureInitialized();

        try {
            log.info("Registering DID Document");

            contractApi.registDidDoc(invokedDidDoc, roleType);

            log.info("DID Document registered successfully");

        } catch (BlockChainException e) {
            log.error("Failed to register DID Document", e);
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_REGISTRATION_FAILED, e);
        }
    }

    /**
     * Retrieve DID Document from blockchain.
     *
     * @param did DID identifier
     * @return DID Document
     * @throws TaSdkException if retrieval fails or DID not found
     */
    public DidDocument getDidDocument(String did) {
        ensureInitialized();

        try {
            log.debug("Retrieving DID Document: {}", did);

            DidDocAndStatus didDocAndStatus = (DidDocAndStatus) contractApi.getDidDoc(did);

            if (didDocAndStatus == null || didDocAndStatus.getDocument() == null) {
                throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND,
                    "DID Document not found: " + did);
            }

            log.debug("DID Document retrieved successfully: {}", did);
            return didDocAndStatus.getDocument();

        } catch (BlockChainException e) {
            log.error("Failed to retrieve DID Document: {}", did, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_NOT_FOUND, e);
        }
    }

    /**
     * Update DID Document status.
     *
     * @param did DID identifier
     * @param status New status
     * @throws TaSdkException if update fails
     */
    public void updateDidDocumentStatus(String did, DidDocStatus status) {
        ensureInitialized();

        try {
            log.info("Updating DID Document status: {} -> {}", did, status);

            contractApi.updateDidDocStatus(did, status);

            log.info("DID Document status updated successfully");

        } catch (BlockChainException e) {
            log.error("Failed to update DID Document status: {}", did, e);
            throw new TaSdkException(TaSdkErrorCode.SDK_DID_DOCUMENT_UPDATE_FAILED, e);
        }
    }

    /**
     * Check if DID Document exists.
     *
     * @param did DID identifier
     * @return true if DID Document exists
     */
    public boolean existsDidDocument(String did) {
        ensureInitialized();

        try {
            DidDocument didDoc = getDidDocument(did);
            return didDoc != null;
        } catch (TaSdkException e) {
            return false;
        }
    }

    /**
     * Ensure adapter is initialized.
     *
     * @throws TaSdkException if not initialized
     */
    private void ensureInitialized() {
        if (!initialized) {
            throw new TaSdkException(TaSdkErrorCode.SDK_NOT_INITIALIZED,
                "BlockchainAdapter not initialized. Call initialize() first.");
        }
    }

    /**
     * Get ContractApi instance (for advanced operations).
     *
     * @return ContractApi instance
     */
    public ContractApi getContractApi() {
        ensureInitialized();
        return contractApi;
    }
}
