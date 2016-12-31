package utils;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.plano.data.HttpRequest;
import org.plano.data.PlanoRequest;
import org.plano.data.SchedulePolicy;

import java.util.Date;
import java.util.UUID;

/**
 * Utility class to create Plano POJOs.
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
        schedulePolicy.setMultiplier(1);
        schedulePolicy.setNumberOfExecutions(5);

        return schedulePolicy;
    }

    public static PlanoRequest createPlanoRequest() {
        PlanoRequest planoRequest = createPlanoRequestWithNullRequestID();
        planoRequest.setRequestID(UUID.randomUUID().toString());

        return planoRequest;
    }

    public static PlanoRequest createPlanoRequestWithNullRequestID() {
        PlanoRequest planoRequest = new PlanoRequest();
        planoRequest.setExecutionTime(new Date());
        planoRequest.setHttpRequest(createHttpRequest());
        planoRequest.setSchedulePolicy(createSchedulePolicy());

        return planoRequest;
    }

    public static HttpResponse createHttpResponse(int statusCode) {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 0);
        StatusLine statusLine = new BasicStatusLine(protocolVersion, statusCode, "good");
        return new BasicHttpResponse(statusLine);
    }
}
