package org.plano.repository.dynamodb.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.Objects;

/**
 * DynamoDBSchedulePolicy POJO.
 */
@DynamoDBDocument
public class DynamoDBSchedulePolicy {

    private Integer multiplier;
    private Long executionIntervalMs;
    private Integer numberOfExecutions;

    public Integer getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public Long getExecutionIntervalMs() {
        return executionIntervalMs;
    }

    public void setExecutionIntervalMs(Long executionIntervalMs) {
        this.executionIntervalMs = executionIntervalMs;
    }

    public Integer getNumberOfExecutions() {
        return numberOfExecutions;
    }

    public void setNumberOfExecutions(Integer numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DynamoDBSchedulePolicy)) {
            return false;
        }
        DynamoDBSchedulePolicy dynamoDBSchedulePolicy = (DynamoDBSchedulePolicy)o;

        return Objects.equals(multiplier, dynamoDBSchedulePolicy.getMultiplier()) &&
                Objects.equals(executionIntervalMs,
                        dynamoDBSchedulePolicy.getExecutionIntervalMs()) &&
                Objects.equals(numberOfExecutions, dynamoDBSchedulePolicy.getNumberOfExecutions());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (multiplier == null ? 0 : multiplier.hashCode());
        result = result * 31 + (executionIntervalMs == null ? 0 : executionIntervalMs.hashCode());
        result = result * 31 + (numberOfExecutions == null ? 0 : numberOfExecutions.hashCode());

        return result;
    }
}
