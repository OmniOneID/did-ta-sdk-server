package org.omnione.did.tas.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.omnione.did.common.datamodel.data.signature.Proof;

import java.util.List;

/**
 * VC Revocation Request Information.
 *
 * <p>Contains VC ID, issuerNonce, and signature(s) for revocation.</p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqRevokeVc {

    /**
     * VC ID to revoke
     */
    private String vcId;

    /**
     * Issuer Nonce (received in Step 1)
     */
    private String issuerNonce;

    /**
     * PIN signature (assertionMethod purpose)
     */
    private Proof proof;

    /**
     * Multiple proofs (for complex authentication scenarios)
     */
    private List<Proof> proofs;
}
