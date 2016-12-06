package org.plano.exception;

/**
 * This exception is thrown when the request is invalid.
 */
public class InvalidRequestException extends PlanoException {

    /**
     * Constructor
     * @param message {@link String}
     */
    public InvalidRequestException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param cause {@link Throwable}
     */
    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * @param message {@link String}
     * @param cause {@link Throwable}
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
