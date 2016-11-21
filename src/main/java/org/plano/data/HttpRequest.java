package org.plano.data;

import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by ctsai on 11/6/16.
 */
public class HttpRequest {

    @NotNull
    private String uri;

    private String payload;

    private String charset;

    @NotNull
    private String httpMethod;

    private Map<String, String> headers;

    private Integer connectionTimeoutMs;

    private Integer socketTimeoutMs;

    private Integer connectionRequestTimeoutMs;

    public boolean isValid() throws IllegalAccessException {
        final Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(NotNull.class) && field.get(this) == null)
            {
                return false;
            }
        }

        if (HttpMethod.resolve(httpMethod) == null) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("uri={};", uri));
        sb.append(String.format("payload={};", payload));
        sb.append(String.format("charset={};", charset));
        sb.append(String.format("httpMethod={};", httpMethod));
        sb.append(String.format("headers={};", headers));
        sb.append(String.format("connectionTimeoutMs={}", connectionTimeoutMs));
        sb.append(String.format("socketTimeoutMs={};", socketTimeoutMs));
        sb.append(String.format("connectionRequestTimeoutMs={};", connectionRequestTimeoutMs));
        return sb.toString();
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
}
