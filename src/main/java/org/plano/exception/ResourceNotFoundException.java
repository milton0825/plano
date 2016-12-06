package org.plano.exception;

/**
 * This exception is thrown when resource is not found.
 */
public class ResourceNotFoundException extends PlanoException {
    /**
     * Constructor.
     * @param message {@link String}
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param cause {@link Throwable}
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message {@link String}
     * @param cause {@link Throwable}
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
