package org.plano.repository.dynamodb;

import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.data.SchedulePolicy;
import org.plano.repository.dynamodb.model.DynamoDBHttpRequest;
import org.plano.repository.dynamodb.model.DynamoDBPlanoRequest;
import org.plano.repository.dynamodb.model.DynamoDBSchedulePolicy;

/**
 * This class provides utility functions for DynamoDB layer.
 */
public final class DynamoDBUtils {

    /**
     * Create a {@link PlanoRequest} with {@link DynamoDBPlanoRequest}.
     * @param dynamoDBPlanoRequest {@link DynamoDBPlanoRequest}
     * @return {@link PlanoRequest}
     * @throws IllegalArgumentException when the {@link DynamoDBPlanoRequest} is invalid
     */
    public static PlanoRequest createPlanoRequest(DynamoDBPlanoRequest dynamoDBPlanoRequest)
            throws IllegalArgumentException {
        if (dynamoDBPlanoRequest == null) {
            throw new IllegalArgumentException("DynamoDBPlanoRequest is null.");
        }

        HttpRequest httpRequest = createHttpRequest(dynamoDBPlanoRequest.getDynamoDBHttpRequest());
        SchedulePolicy schedulePolicy = createSchedulePolicy(dynamoDBPlanoRequest.getDynamoDBSchedulePolicy());

        PlanoRequest planoRequest = new PlanoRequest();
        planoRequest.setExecutionTime(dynamoDBPlanoRequest.getExecutionTime());
        planoRequest.setRequestID(dynamoDBPlanoRequest.getRequestID());
        planoRequest.setHttpRequest(httpRequest);
        planoRequest.setSchedulePolicy(schedulePolicy);

        return planoRequest;
    }

    /**
     * Create a {@link DynamoDBPlanoRequest} with {@link PlanoRequest}.
     * @param planoRequest {@link PlanoRequest}
     * @return {@link DynamoDBPlanoRequest}
     * @throws IllegalArgumentException when {@link PlanoRequest} is invalid
     */
    public static DynamoDBPlanoRequest createDynamoDBPlanoRequest(PlanoRequest planoRequest)
            throws IllegalArgumentException {
        if (planoRequest == null) {
            throw new IllegalArgumentException("PlanoRequest is null.");
        }

        DynamoDBHttpRequest dynamoDBHttpRequest = createDynamoDBHttpRequest(planoRequest.getHttpRequest());
        DynamoDBSchedulePolicy dynamoDBSchedulePolicy = createDynamoDBSchedulePolicy(planoRequest.getSchedulePolicy());

        DynamoDBPlanoRequest dynamoDBPlanoRequest = new DynamoDBPlanoRequest();
        dynamoDBPlanoRequest.setExecutionTime(planoRequest.getExecutionTime());
        dynamoDBPlanoRequest.setRequestID(planoRequest.getRequestID());
        dynamoDBPlanoRequest.setDynamoDBHttpRequest(dynamoDBHttpRequest);
        dynamoDBPlanoRequest.setDynamoDBSchedulePolicy(dynamoDBSchedulePolicy);

        return dynamoDBPlanoRequest;
    }

    /**
     * Create {@link SchedulePolicy} with {@link DynamoDBSchedulePolicy}.
     * @param dynamoDBSchedulePolicy {@link DynamoDBSchedulePolicy}
     * @return {@link SchedulePolicy}
     * @throws IllegalArgumentException when {@link DynamoDBSchedulePolicy} is invalid
     */
    public static SchedulePolicy createSchedulePolicy(DynamoDBSchedulePolicy dynamoDBSchedulePolicy)
            throws IllegalArgumentException {
        if (dynamoDBSchedulePolicy == null) {
            throw new IllegalArgumentException("DynamoDBSchedulePolicy is null.");
        }

        SchedulePolicy schedulePolicy = new SchedulePolicy();
        schedulePolicy.setExecutionIntervalMs(dynamoDBSchedulePolicy.getExecutionIntervalMs());
        schedulePolicy.setMultiplier(dynamoDBSchedulePolicy.getMultiplier());
        schedulePolicy.setNumberOfExecutions(dynamoDBSchedulePolicy.getNumberOfExecutions());

        return schedulePolicy;
    }

    /**
     * Create {@link HttpRequest} with {@link DynamoDBHttpRequest}.
     * @param dynamoDBHttpRequest {@link DynamoDBHttpRequest}
     * @return {@link HttpRequest}
     * @throws IllegalArgumentException when {@link DynamoDBHttpRequest} is invalid
     */
    public static HttpRequest createHttpRequest(DynamoDBHttpRequest dynamoDBHttpRequest)
            throws IllegalArgumentException {
        if (dynamoDBHttpRequest == null) {
            throw new IllegalArgumentException("DynamoDBHttpRequest is null.");
        }

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setCharset(dynamoDBHttpRequest.getCharset());
        httpRequest.setHeaders(dynamoDBHttpRequest.getHeaders());
        httpRequest.setHttpMethod(dynamoDBHttpRequest.getHttpMethod());
        httpRequest.setPayload(dynamoDBHttpRequest.getPayload());
        httpRequest.setUri(dynamoDBHttpRequest.getUri());
        httpRequest.setConnectionTimeoutMs(dynamoDBHttpRequest.getConnectionTimeoutMs());
        httpRequest.setConnectionRequestTimeoutMs(dynamoDBHttpRequest.getConnectionRequestTimeoutMs());
        httpRequest.setSocketTimeoutMs(dynamoDBHttpRequest.getSocketTimeoutMs());

        return httpRequest;
    }

    /**
     * Create {@link DynamoDBHttpRequest} with {@link HttpRequest}.
     * @param httpRequest {@link HttpRequest}
     * @return {@link DynamoDBHttpRequest}
     * @throws IllegalArgumentException when {@link HttpRequest} is invalid
     */
    public static DynamoDBHttpRequest createDynamoDBHttpRequest(HttpRequest httpRequest)
            throws IllegalArgumentException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HttpRequest is null.");
        }

        DynamoDBHttpRequest dynamoDBHttpRequest = new DynamoDBHttpRequest();
        dynamoDBHttpRequest.setCharset(httpRequest.getCharset());
        dynamoDBHttpRequest.setHeaders(httpRequest.getHeaders());
        dynamoDBHttpRequest.setHttpMethod(httpRequest.getHttpMethod());
        dynamoDBHttpRequest.setPayload(httpRequest.getPayload());
        dynamoDBHttpRequest.setUri(httpRequest.getUri());
        dynamoDBHttpRequest.setConnectionTimeoutMs(httpRequest.getConnectionTimeoutMs());
        dynamoDBHttpRequest.setConnectionRequestTimeoutMs(httpRequest.getConnectionRequestTimeoutMs());
        dynamoDBHttpRequest.setSocketTimeoutMs(httpRequest.getSocketTimeoutMs());

        return dynamoDBHttpRequest;
    }

    /**
     * Create {@link DynamoDBSchedulePolicy} with {@link SchedulePolicy}.
     * @param schedulePolicy {@link SchedulePolicy}
     * @return {@link DynamoDBSchedulePolicy}
     * @throws IllegalArgumentException when {@link SchedulePolicy} is invalid
     */
    public static DynamoDBSchedulePolicy createDynamoDBSchedulePolicy(SchedulePolicy schedulePolicy)
            throws IllegalArgumentException {
        if (schedulePolicy == null) {
            throw new IllegalArgumentException("SchedulePolicy is null.");
        }

        DynamoDBSchedulePolicy dynamoDBSchedulePolicy = new DynamoDBSchedulePolicy();
        dynamoDBSchedulePolicy.setExecutionIntervalMs(schedulePolicy.getExecutionIntervalMs());
        dynamoDBSchedulePolicy.setMultiplier(schedulePolicy.getMultiplier());
        dynamoDBSchedulePolicy.setNumberOfExecutions(schedulePolicy.getNumberOfExecutions());

        return dynamoDBSchedulePolicy;
    }

    private DynamoDBUtils() {} // prevent instantiation
}
