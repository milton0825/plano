package utils;

import org.plano.repository.dynamodb.model.DynamoDBHttpRequest;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import org.plano.repository.dynamodb.model.DynamoDBSchedulePolicy;

import java.util.Date;
import java.util.UUID;

public final class DynamoDBTestUtils {

    public static DynamoDBHttpRequest createDynamoDBHttpRequest() {
        DynamoDBHttpRequest dynamoDBHttpRequest = new DynamoDBHttpRequest();
        dynamoDBHttpRequest.setUri("http://uri");
        dynamoDBHttpRequest.setHttpMethod("POST");
        dynamoDBHttpRequest.setCharset("UTF-8");
        dynamoDBHttpRequest.setPayload("payload");
        dynamoDBHttpRequest.setConnectionRequestTimeoutMs(1000);
        dynamoDBHttpRequest.setConnectionTimeoutMs(1000);
        dynamoDBHttpRequest.setSocketTimeoutMs(1000);

        return dynamoDBHttpRequest;
    }

    public static DynamoDBSchedulePolicy createDynamoDBSchedulePolicy() {
        DynamoDBSchedulePolicy dynamoDBSchedulePolicy = new DynamoDBSchedulePolicy();
        dynamoDBSchedulePolicy.setExecutionIntervalMs(100L);
        dynamoDBSchedulePolicy.setMultiplier(2);
        dynamoDBSchedulePolicy.setNumberOfExecutions(10);

        return dynamoDBSchedulePolicy;
    }

    public static DynamoDBPlanoRequest createDynamoDBPlanoRequest() {
        DynamoDBPlanoRequest dynamoDBPlanoRequest = new DynamoDBPlanoRequest();
        dynamoDBPlanoRequest.setRequestID(UUID.randomUUID().toString());
        dynamoDBPlanoRequest.setExecutionTime(new Date());
        dynamoDBPlanoRequest.setDynamoDBHttpRequest(createDynamoDBHttpRequest());
        dynamoDBPlanoRequest.setDynamoDBSchedulePolicy(createDynamoDBSchedulePolicy());

        return dynamoDBPlanoRequest;
    }

    private DynamoDBTestUtils() {} // prevent instantiation
}
