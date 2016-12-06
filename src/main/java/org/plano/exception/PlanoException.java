package org.plano.exception;

/**
 * Base class for user-defined exception in Plano.
 */
public class PlanoException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param message {@link String}
     */
    public PlanoException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param cause {@link Throwable}
     */
    public PlanoException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * @param message {@link String}
     * @param cause {@link Throwable}
     */
    public PlanoException(String message, Throwable cause) {
        super(message, cause);
    }
}
