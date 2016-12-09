package utils;

import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.data.SchedulePolicy;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ctsai on 11/28/16.
 */
public class DataTestUtils {
    public static HttpRequest createHttpRequest() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUri("http://uri");
        httpRequest.setHttpMethod("POST");
        httpRequest.setCharset("UTF-8");
        httpRequest.setPayload("payload");
        httpRequest.setConnectionRequestTimeoutMs(1000);
        httpRequest.setConnectionTimeoutMs(1000);
        httpRequest.setSocketTimeoutMs(1000);

        return httpRequest;
    }

    public static SchedulePolicy createSchedulePolicy() {
        SchedulePolicy schedulePolicy = new SchedulePolicy();
        schedulePolicy.setExecutionIntervalMs(100L);
        schedulePolicy.setMultiplier(2);
        schedulePolicy.setNumberOfExecutions(10);

        return schedulePolicy;
    }

    public static PlanoRequest createPlanoRequest() {
        PlanoRequest planoRequest = new PlanoRequest();
        planoRequest.setRequestID(UUID.randomUUID().toString());
        planoRequest.setExecutionTime(new Date());
        planoRequest.setHttpRequest(createHttpRequest());
        planoRequest.setSchedulePolicy(createSchedulePolicy());

        return planoRequest;
    }
}
