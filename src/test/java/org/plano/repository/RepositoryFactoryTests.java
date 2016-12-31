package org.plano.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.plano.PlanoApplication;
import org.plano.repository.dynamodb.DynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlanoApplication.class,
        loader = SpringBootContextLoader.class)
public class RepositoryFactoryTests {
    @Autowired
    private RepositoryFactory repositoryFactory;

    @Test
    public void testCreateDynamoDBRepository() {
        Repository repository = repositoryFactory.create(RepositoryType.DYNAMODB);
        Assert.assertTrue(repository instanceof DynamoDBRepository);
    }
}
