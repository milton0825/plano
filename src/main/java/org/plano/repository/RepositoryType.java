package org.plano.repository;

/**
 * This enum defines the type of repository.
 */
public enum RepositoryType {
    DYNAMODB("dynamodb");

    private final String value;

    RepositoryType(String value) {
        this.value = value;
    }

    /**
     * @return the string value
     */
    public String value() {
        return value;
    }
}
