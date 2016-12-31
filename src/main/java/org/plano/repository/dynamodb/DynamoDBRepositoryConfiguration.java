package org.plano.repository.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Spring configuration for {@link DynamoDBRepository}
 */
@Configuration
public class DynamoDBRepositoryConfiguration {
    @Bean(destroyMethod = "shutdown")
    public AmazonDynamoDB createAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }

    @Bean
    public DynamoDBMapper createDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        return dynamoDBMapper;
    }
}
