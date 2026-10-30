package org.omnione.did.tas.exception;

/**
 * Exception thrown when Issuer API communication fails.
 *
 * <p>This exception is thrown by {@link org.omnione.did.tas.client.IssuerApiClient}
 * implementations when HTTP communication with the Issuer server fails.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 * {@code
 * public class IssuerHttpClient implements IssuerApiClient {
 *     @Override
 *     public InspectIssueProposeApiResponse inspectProposeIssue(
 *         InspectIssueProposeApiRequest request
 *     ) throws IssuerApiException {
 *         try {
 *             return httpClient.post(url, request, InspectIssueProposeApiResponse.class);
 *         } catch (HttpException e) {
 *             throw new IssuerApiException("Issuer communication failed", e);
 *         }
 *     }
 * }
 * }
 * </pre>
 */
public class IssuerApiException extends Exception {

    /**
     * Constructs a new IssuerApiException with the specified detail message.
     *
     * @param message the detail message
     */
    public IssuerApiException(String message) {
        super(message);
    }

    /**
     * Constructs a new IssuerApiException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public IssuerApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
