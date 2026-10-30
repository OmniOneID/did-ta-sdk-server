package org.omnione.did.tas.model.enums;

/**
 * TAS registration status.
 *
 * <p>State machine:</p>
 * <pre>
 * PENDING → (enrollTas success) → COMPLETED
 * PENDING → (enrollTas failure) → PENDING  (retryable)
 * COMPLETED → (enrollTas attempt) → error SSDKTRA11002
 * </pre>
 */
public enum TasStatus {

    /**
     * TAS is configured but not yet registered to the trust network.
     * P110 enrollment is allowed in this state.
     */
    PENDING,

    /**
     * TAS has successfully enrolled (Certificate VC issued and registered).
     * P110 enrollment is rejected in this state.
     */
    COMPLETED
}
