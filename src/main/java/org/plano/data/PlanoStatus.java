package org.plano.data;

/**
 * The status of each request processed by Plano.
 */
public enum PlanoStatus {
    SUCCESS("Success"),
    CREATED("Created"),
    NOT_FOUND("Resource not Found"),
    INVALID_INPUT("Invalid input");

    private final String value;

    PlanoStatus(String value) {
        this.value = value;
    }

    /**
     * @return string value
     */
    public String value() {
        return value;
    }
}
