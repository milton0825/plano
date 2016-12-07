package org.plano.repository;

import org.plano.repository.dynamodb.DynamoDBRepository;

/**
 * Factory for creating {@link Repository}
 */
public final class RepositoryFactory {

    /**
     * Create repository.
     * @param repositoryType type of repository.
     * @return {@link Repository}
     */
    public static Repository create(RepositoryType repositoryType) {
        Repository repository = null;
        switch (repositoryType) {
            case DYNAMODB:
                repository = new DynamoDBRepository();
                break;
        }

        return repository;
    }

    public RepositoryFactory() {} // prevent instantiation
}
