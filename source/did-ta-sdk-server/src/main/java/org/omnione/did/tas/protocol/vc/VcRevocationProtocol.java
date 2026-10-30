package org.omnione.did.tas.protocol.vc;

import org.omnione.did.tas.client.IssuerApiClient;
import org.omnione.did.tas.exception.TaSdkException;
import org.omnione.did.tas.model.request.protocol.vc.ConfirmRevokeVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.ProposeRevokeVcRequest;
import org.omnione.did.tas.model.request.protocol.vc.RequestRevokeVcRequest;
import org.omnione.did.tas.model.response.protocol.vc.ConfirmRevokeVcResult;
import org.omnione.did.tas.model.response.protocol.vc.ProposeRevokeVcResult;
import org.omnione.did.tas.model.response.protocol.vc.RequestRevokeVcResult;
import org.omnione.did.tas.provider.VcRevocationDataProvider;

/**
 * P220 VC Revocation Protocol interface.
 *
 * <p>This interface defines the contract for P220 VC Revocation Protocol.
 * The TAS SDK provides the implementation, and the TA Server calls these methods
 * to execute the protocol steps.</p>
 *
 * <p><b>Protocol Steps:</b></p>
 * <ul>
 *   <li>Step 1: Propose Revoke VC</li>
 *   <li>Step 2: Request ECDH (Common component)</li>
 *   <li>Step 3: Request Create Token (Common component)</li>
 *   <li>Step 4: Request Revoke VC</li>
 *   <li>Step 5: Confirm Revoke VC</li>
 * </ul>
 *
 * <p><b>Design Notes:</b></p>
 * <ul>
 *   <li>SDK handles business logic (when to call Issuer, validation, crypto)</li>
 *   <li>Server provides IssuerApiClient implementation (HTTP communication)</li>
 *   <li>Server provides DataProvider implementation (DB and blockchain access)</li>
 *   <li>Server manages Transaction state (status, expiration, step sequencing)</li>
 * </ul>
 *
 * @see IssuerApiClient
 * @see VcRevocationDataProvider
 */
public interface VcRevocationProtocol {

    // ===== Core Protocol APIs =====

    /**
     * Step 1: Propose Revoke VC.
     *
     * <p>Start VC revocation process and validate proposal with Issuer.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Generate TAS txId</li>
     *   <li>Retrieve VC metadata from blockchain (via DataProvider)</li>
     *   <li>Validate VC status (must be ACTIVE)</li>
     *   <li>Call Issuer API: inspect-propose-revoke</li>
     *   <li>Return txId, issuerNonce, authType</li>
     * </ol>
     *
     * <p><b>Server responsibilities:</b></p>
     * <ul>
     *   <li>Save Transaction (txId, vcId, issuerDid, issuerNonce)</li>
     *   <li>Save SubTransaction (Step 1 completed)</li>
     * </ul>
     *
     * @param request Propose request (vcId)
     * @param issuerClient Issuer API client (Server provides implementation)
     * @param dataProvider Data provider (Server provides implementation)
     * @return Propose result (txId, issuerNonce, authType)
     * @throws TaSdkException if validation fails or Issuer communication fails
     */
    ProposeRevokeVcResult proposeRevokeVc(
        ProposeRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException;

    /**
     * Step 4: Request Revoke VC.
     *
     * <p>Validate PIN signature and request VC revocation from Issuer.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Validate ReqRevokeVc (vcId, issuerNonce match)</li>
     *   <li>Validate PIN signature (assertionMethod purpose)</li>
     *   <li>Call Issuer API: revoke-vc</li>
     *   <li>Return txId</li>
     * </ol>
     *
     * @param request Revoke VC request (serverToken, reqRevokeVc)
     * @param issuerClient Issuer API client
     * @param dataProvider Data provider
     * @return Revoke VC result (txId)
     * @throws TaSdkException if validation fails or Issuer communication fails
     */
    RequestRevokeVcResult requestRevokeVc(
        RequestRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException;

    /**
     * Step 5: Confirm Revoke VC.
     *
     * <p>Notify Issuer that VC revocation is complete.</p>
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Validate transaction state</li>
     *   <li>Call Issuer API: complete-revoke</li>
     * </ol>
     *
     * @param request Confirm request (txId, serverToken)
     * @param issuerClient Issuer API client
     * @param dataProvider Data provider
     * @return Confirm result (txId)
     * @throws TaSdkException if validation fails or Issuer communication fails
     */
    ConfirmRevokeVcResult confirmRevokeVc(
        ConfirmRevokeVcRequest request,
        IssuerApiClient issuerClient,
        VcRevocationDataProvider dataProvider
    ) throws TaSdkException;
}
