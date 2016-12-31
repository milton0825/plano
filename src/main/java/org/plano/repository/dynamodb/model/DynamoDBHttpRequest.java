package org.plano.repository.dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

/**
 * DynamoDBHttpRequest POJO.
 */
@DynamoDBDocument
public class DynamoDBHttpRequest {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBHttpRequest.class);

    private String uri;
    private String payload;
    private String charset;
    private String httpMethod;
    private Map<String, String> headers;
    private Integer connectionTimeoutMs;
    private Integer socketTimeoutMs;
    private Integer connectionRequestTimeoutMs;

    /**
     * Check if {@link DynamoDBHttpRequest} is valid.
     * @return true if {@link DynamoDBHttpRequest} is valid
     */
    public boolean isValid() {
        final Field[] fields = this.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(NotNull.class) && field.get(this) == null) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (HttpMethod.resolve(httpMethod) == null) {
            return false;
        }

        return true;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getSocketTimeoutMs() {
        return socketTimeoutMs;
    }

    public void setSocketTimeoutMs(Integer socketTimeoutMs) {
        this.socketTimeoutMs = socketTimeoutMs;
    }

    public Integer getConnectionRequestTimeoutMs() {
        return connectionRequestTimeoutMs;
    }

    public void setConnectionRequestTimeoutMs(Integer connectionRequestTimeoutMs) {
        this.connectionRequestTimeoutMs = connectionRequestTimeoutMs;
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
        if (!(o instanceof DynamoDBHttpRequest)) {
            return false;
        }
        DynamoDBHttpRequest dynamoDBHttpRequest = (DynamoDBHttpRequest) o;

        return Objects.equals(uri, dynamoDBHttpRequest.getUri())
                && Objects.equals(payload, dynamoDBHttpRequest.getPayload())
                && Objects.equals(charset, dynamoDBHttpRequest.getCharset())
                && Objects.equals(httpMethod, dynamoDBHttpRequest.getHttpMethod())
                && Objects.equals(headers, dynamoDBHttpRequest.getHeaders())
                && Objects.equals(connectionTimeoutMs, dynamoDBHttpRequest.getConnectionTimeoutMs())
                && Objects.equals(socketTimeoutMs, dynamoDBHttpRequest.getSocketTimeoutMs())
                && Objects.equals(connectionRequestTimeoutMs, dynamoDBHttpRequest.getConnectionRequestTimeoutMs());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (uri == null ? 0 : uri.hashCode());
        result = result * 31 + (payload == null ? 0 : payload.hashCode());
        result = result * 31 + (charset == null ? 0 : charset.hashCode());
        result = result * 31 + (httpMethod == null ? 0 : httpMethod.hashCode());
        result = result * 31 + (headers == null ? 0 : headers.hashCode());
        result = result * 31 + (connectionTimeoutMs == null ? 0 : connectionTimeoutMs.hashCode());
        result = result * 31 + (socketTimeoutMs == null ? 0 : socketTimeoutMs.hashCode());
        result = result * 31 + (connectionRequestTimeoutMs == null ? 0 : connectionRequestTimeoutMs.hashCode());

        return result;
    }
}
