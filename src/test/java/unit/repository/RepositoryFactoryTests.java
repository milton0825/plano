package unit.repository;

import org.junit.Assert;
import org.junit.Test;
import org.plano.repository.Repository;
import org.plano.repository.RepositoryFactory;
import org.plano.repository.RepositoryType;
import org.plano.repository.dynamodb.DynamoDBRepository;

public class RepositoryFactoryTests {
    @Test
    public void testCreateDynamoDBRepository() {
        Repository repository = RepositoryFactory.create(RepositoryType.DYNAMODB);
        Assert.assertTrue(repository instanceof DynamoDBRepository);
    }
}
