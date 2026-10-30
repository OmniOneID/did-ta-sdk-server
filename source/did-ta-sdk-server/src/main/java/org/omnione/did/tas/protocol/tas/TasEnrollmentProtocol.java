package org.omnione.did.tas.protocol.tas;

import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.tas.EnrollTasRequest;
import org.omnione.did.tas.model.response.protocol.tas.EnrollTasResponse;
import org.omnione.did.tas.protocol.Protocol;

/**
 * P110: TAS 등록 프로토콜 인터페이스
 *
 * <p>TAS self-enrollment protocol. TAS issues its own Certificate VC and
 * registers it to the blockchain. This is the simplest protocol — single
 * step, no ECDH, no server token, password-based auth only.</p>
 *
 * <p><b>Protocol characteristics:</b></p>
 * <ul>
 *   <li>Single step (no state machine)</li>
 *   <li>Self-enrollment: TAS registers itself</li>
 *   <li>Password-based authorization</li>
 *   <li>Transaction is created server-side (audit trail only)</li>
 * </ul>
 *
 * <p><b>Protocol flow:</b></p>
 * <pre>
 * Server calls enrollTas(request)
 *   → validate password
 *   → validate TAS status (PENDING only)
 *   → fetch TAS DID Document from blockchain
 *   → generate Certificate VC (Subject DN, Role=Tas)
 *   → sign Certificate VC (assert key)
 *   → publish Certificate VC (CertificateProvider)
 *   → register VcMeta to blockchain (StorageProvider)
 *   → return EnrollTasResponse(certVcRef)
 * Server then:
 *   → creates Transaction (audit)
 *   → updates TAS status to COMPLETED
 * </pre>
 *
 * <p><b>Usage (server-side):</b></p>
 * <pre>
 * {@code
 * EnrollTasResponse response = tasEnrollmentProtocol.enrollTas(
 *     EnrollTasRequest.builder().password(password).build()
 * );
 * // response.getCertVcRef() → URL of issued Certificate VC
 * }
 * </pre>
 */
public interface TasEnrollmentProtocol extends Protocol {

    /**
     * Execute TAS self-enrollment (P110).
     *
     * @param request Enrollment request containing the registration password
     * @return Response with the Certificate VC URL ({@code certVcRef})
     * @throws TaSdkException on password mismatch (SSDKTRA11001),
     *                        already registered (SSDKTRA11002),
     *                        DID Document not found (SSDKTRA11003),
     *                        VC generation failure (SSDKTRA11004),
     *                        VC signing failure (SSDKTRA11005),
     *                        VC publishing failure (SSDKTRA11006),
     *                        VcMeta registration failure (SSDKTRA11007),
     *                        unexpected error (SSDKTRA11008)
     */
    EnrollTasResponse enrollTas(EnrollTasRequest request) throws TaSdkException;

    @Override
    default String getProtocolCode() {
        return "P110";
    }

    @Override
    default String getProtocolName() {
        return "TAS Registration Protocol";
    }

    @Override
    default String getVersion() {
        return "1.0";
    }
}
