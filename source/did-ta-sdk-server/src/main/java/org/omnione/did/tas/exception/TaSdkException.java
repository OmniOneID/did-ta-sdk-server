package org.omnione.did.tas.exception;

import lombok.Getter;

/**
 * TA SDK Exception
 *
 * <p>Single exception class for all SDK errors.</p>
 * <p>Uses TaSdkErrorCode to communicate error information.</p>
 * <p>Extends RuntimeException (unchecked exception) for cleaner code.</p>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>
 * // Method 1: Using TaSdkErrorCode (recommended)
 * throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID);
 *
 * // Method 2: Using TaSdkErrorCode with custom message (for additional context)
 * throw new TaSdkException(TaSdkErrorCode.SDK_INVALID_DID, "Invalid DID format: " + did);
 * </pre>
 */
@Getter
public class TaSdkException extends RuntimeException {

    /**
     * Error code (TaSdkErrorCode enum)
     */
    private TaSdkErrorCode errorCode;

    /**
     * Message formatting arguments
     */
    private Object[] args;

    /**
     * Create exception from TaSdkErrorCode
     *
     * @param errorCode Error code enum
     */
    public TaSdkException(TaSdkErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Create exception from TaSdkErrorCode with cause
     *
     * @param errorCode Error code enum
     * @param cause Underlying cause
     */
    public TaSdkException(TaSdkErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * Create exception from TaSdkErrorCode with custom message
     *
     * @param errorCode Error code enum
     * @param message Custom error message
     */
    public TaSdkException(TaSdkErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Create exception from TaSdkErrorCode with custom message and cause
     *
     * @param errorCode Error code enum
     * @param message Custom error message
     * @param cause Underlying cause
     */
    public TaSdkException(TaSdkErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Create exception from TaSdkErrorCode with arguments
     *
     * @param errorCode Error code enum
     * @param args Formatting arguments
     */
    public TaSdkException(TaSdkErrorCode errorCode, Object[] args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Create exception from TaSdkErrorCode with arguments and cause
     *
     * @param errorCode Error code enum
     * @param args Formatting arguments
     * @param cause Underlying cause
     */
    public TaSdkException(TaSdkErrorCode errorCode, Object[] args, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Create exception from TaSdkErrorCode with custom message and arguments
     *
     * @param errorCode Error code enum
     * @param args Formatting arguments
     * @param message Custom error message
     */
    public TaSdkException(TaSdkErrorCode errorCode, Object[] args, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Create exception from TaSdkErrorCode with custom message, arguments, and cause
     *
     * @param errorCode Error code enum
     * @param args Formatting arguments
     * @param message Custom error message
     * @param cause Underlying cause
     */
    public TaSdkException(TaSdkErrorCode errorCode, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Converts args array to a comma-separated string for logging/debugging purposes.
     *
     * @return String representation of args, or null if empty
     */
    public String argsToString() {
        if (args == null || args.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
