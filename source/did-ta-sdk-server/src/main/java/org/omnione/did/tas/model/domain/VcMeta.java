package org.omnione.did.tas.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * VC metadata retrieved from blockchain.
 *
 * <p>This class represents VC metadata stored on the blockchain,
 * including issuer, subject, and status information.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VcMeta {

    /**
     * VC ID
     */
    private String vcId;

    /**
     * Issuer DID
     */
    private String issuer;

    /**
     * Subject DID (Holder)
     */
    private String subject;

    /**
     * VC Status (ACTIVE, REVOKED, EXPIRED)
     */
    private String status;

    /**
     * Issued date (ISO 8601 format)
     */
    private String issuedDate;

    /**
     * Expiration date (ISO 8601 format)
     */
    private String expirationDate;
}
