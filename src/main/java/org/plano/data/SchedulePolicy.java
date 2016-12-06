package org.plano.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * SchedulePolicy POJO.
 */
public class SchedulePolicy {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulePolicy.class);

    private Integer multiplier;
    private Long executionIntervalInSeconds;
    private Integer numberOfExecutions;

    public Integer getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public Long getExecutionIntervalInSeconds() {
        return executionIntervalInSeconds;
    }

    public void setExecutionIntervalInSeconds(Long executionIntervalInSeconds) {
        this.executionIntervalInSeconds = executionIntervalInSeconds;
    }

    public Integer getNumberOfExecutions() {
        return numberOfExecutions;
    }

    public void setNumberOfExecutions(Integer numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
    }

    @Override
    public String toString() {
        String s = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            s = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to marshall object to json. Cause is {}", e);
        }

        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SchedulePolicy)) {
            return false;
        }
        SchedulePolicy schedulePolicy = (SchedulePolicy)o;

        return Objects.equals(multiplier, schedulePolicy.getMultiplier()) &&
                Objects.equals(executionIntervalInSeconds,
                        schedulePolicy.getExecutionIntervalInSeconds()) &&
                Objects.equals(numberOfExecutions, schedulePolicy.getNumberOfExecutions());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (multiplier == null ? 0 : multiplier.hashCode());
        result = result * 31 + (executionIntervalInSeconds == null ? 0 : executionIntervalInSeconds.hashCode());
        result = result * 31 + (numberOfExecutions == null ? 0 : numberOfExecutions.hashCode());

        return result;
    }
}
