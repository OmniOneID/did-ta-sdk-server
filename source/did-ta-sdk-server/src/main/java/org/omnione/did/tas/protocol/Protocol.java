package org.omnione.did.tas.protocol;

/**
 * Base interface for all TA SDK protocol implementations.
 *
 * <p>Each protocol (P110, P120, P131, etc.) extends this interface and
 * provides its code, name, and version via default methods.</p>
 */
public interface Protocol {

    /**
     * Protocol code (e.g., "P110", "P120").
     *
     * @return protocol code string
     */
    String getProtocolCode();

    /**
     * Human-readable protocol name.
     *
     * @return protocol name
     */
    String getProtocolName();

    /**
     * Protocol version.
     *
     * @return version string (e.g., "1.0")
     */
    String getVersion();
}
