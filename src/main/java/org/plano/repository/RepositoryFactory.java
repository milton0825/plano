package org.plano.repository;

import org.plano.repository.dynamodb.DynamoDBRepository;

/**
 * Created by ctsai on 11/26/16.
 */
public final class RepositoryFactory {

    private static Repository create(String type) {
        Repository repository = null;
        switch (RepositoryType.valueOf(type)) {
            case DYNAMODB:
                repository = new DynamoDBRepository();
                break;
        }

        return repository;
    }

    public RepositoryFactory() {}
}
