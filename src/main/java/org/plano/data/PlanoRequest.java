package org.plano.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.plano.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

/**
 * PlanoRequest POJO.
 */
public class PlanoRequest {
    private static final Logger LOG = LoggerFactory.getLogger(PlanoRequest.class);

    private String requestID;
    @NotNull
    private HttpRequest httpRequest;

    private Date executionTime;

    @NotNull
    private SchedulePolicy schedulePolicy;

    public void updateForNextExecution() {
        Integer numberOfExecutions = schedulePolicy.getNumberOfExecutions();
        schedulePolicy.setNumberOfExecutions(numberOfExecutions-1);

        Long executionIntervalMs = schedulePolicy.getExecutionIntervalMs();
        Integer multiplier = schedulePolicy.getMultiplier();

        executionIntervalMs = executionIntervalMs * multiplier;
        executionIntervalMs = executionIntervalMs > Constants.MAX_EXECUTION_INTERVAL_MS ?
                Constants.MAX_EXECUTION_INTERVAL_MS : executionIntervalMs;

        schedulePolicy.setExecutionIntervalMs(executionIntervalMs);
        executionTime = new Date(System.currentTimeMillis() + executionIntervalMs);
    }

    public boolean isValid() {
        final Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(NotNull.class) && field.get(this) == null) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            String message = String.format("Failed when validating PlanoRequest: {0}", this);
            LOG.error(message, e.getCause());
        }
        return true;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public SchedulePolicy getSchedulePolicy() {
        return schedulePolicy;
    }

    public void setSchedulePolicy(SchedulePolicy schedulePolicy) {
        this.schedulePolicy = schedulePolicy;
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
        if (!(o instanceof PlanoRequest)) {
            return false;
        }
        PlanoRequest planoRequest = (PlanoRequest)o;

        return Objects.equals(requestID, planoRequest.getRequestID()) &&
                Objects.equals(httpRequest, planoRequest.getHttpRequest()) &&
                Objects.equals(executionTime, planoRequest.getExecutionTime()) &&
                Objects.equals(schedulePolicy, planoRequest.getSchedulePolicy());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (requestID == null ? 0 : requestID.hashCode());
        result = 31 * result + (httpRequest == null ? 0 : httpRequest.hashCode());
        result = 31 * result + (executionTime == null ? 0 : executionTime.hashCode());
        result = 31 * result + (schedulePolicy == null ? 0 : schedulePolicy.hashCode());

        return result;
    }
}
