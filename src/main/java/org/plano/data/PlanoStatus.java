package org.plano.data;

/**
 * Created by ctsai on 12/13/16.
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

    public String value() {
        return value;
    }
}
