package org.plano.repository.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for {@link DynamoDBRepository}.
 */
@Configuration
public class DynamoDBRepositoryConfiguration {

    /**
     * Create {@link AmazonDynamoDB} with customized configuration.
     * @return {@link AmazonDynamoDB}
     */
    @Bean(destroyMethod = "shutdown")
    public AmazonDynamoDB createAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }

    /**
     * Create {@link DynamoDBMapper} with customized configuration.
     * @param amazonDynamoDB {@link AmazonDynamoDB}
     * @return {@link DynamoDBMapper}
     */
    @Bean
    public DynamoDBMapper createDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        return dynamoDBMapper;
    }
}
