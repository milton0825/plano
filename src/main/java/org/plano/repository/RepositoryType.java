package org.plano.repository;

/**
 * This enum defines the type of repository.
 */
public enum RepositoryType {
    DYNAMODB("DynamoDB");

    private final String value;

    RepositoryType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
