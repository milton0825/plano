package org.plano.worker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.plano.data.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Created by ctsai on 11/19/16.
 */
public class HttpEndpointInvoker implements EndpointInvoker<HttpRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpEndpointInvoker.class);

    @Value("${http.connection.timeout.in.milliseconds}")
    private Integer httpConnectionTimeoutInMilliSeconds;

    @Value("${http.socket.timeout.in.milliseconds}")
    private Integer httpSocketTimeoutInMilliSeconds;

    private HttpClient httpClient;

    HttpEndpointInvoker(HttpClient httpClient) {
        if (httpClient == null) {
            throw new IllegalArgumentException("HttpClient can not be null.");
        }

        this.httpClient = httpClient;
    }

    @Override
    public boolean invoke(HttpRequest httpRequest) {
        boolean isSuccess = false;
        try {
            HttpUriRequest httpUriRequest= getHttpUriRequest(httpRequest);
            HttpResponse httpResponse =  httpClient.execute(httpUriRequest);
            isSuccess = httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (IOException | IllegalAccessException e) {
            LOG.error("Failed to send {0}, the exception is {1}", httpRequest, e);
        }

        return isSuccess;
    }

    private HttpUriRequest getHttpUriRequest(HttpRequest httpRequest)
            throws UnsupportedEncodingException, IllegalAccessException {
        if (httpRequest == null || !httpRequest.isValid()) {
            throw new IllegalArgumentException("Invalid HttpRequest.");
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpRequest.getSocketTimeoutMs())
                .setConnectTimeout(httpRequest.getConnectionTimeoutMs())
                .setConnectionRequestTimeout(httpRequest.getConnectionRequestTimeoutMs())
                .build();

        HttpUriRequest httpUriRequest;
        HttpMethod httpMethod = HttpMethod.resolve(httpRequest.getHttpMethod());
        switch (httpMethod) {
            case GET: {
                HttpGet httpGet = new HttpGet(httpRequest.getUri());
                httpGet.setConfig(requestConfig);
                httpUriRequest = httpGet;
                break;
            }
            case POST: {
                HttpPost httpPost = new HttpPost(httpRequest.getUri());
                httpPost.setConfig(requestConfig);
                String charset = httpRequest.getCharset();
                HttpEntity httpEntity = new StringEntity(httpRequest.getPayload(), charset);
                httpPost.setEntity(httpEntity);
                httpUriRequest = httpPost;
                break;
            }
            case PUT: {
                HttpPut httpPut = new HttpPut(httpRequest.getUri());
                httpPut.setConfig(requestConfig);
                String charset = httpRequest.getCharset();
                HttpEntity httpEntity = new StringEntity(httpRequest.getPayload(), charset);
                httpPut.setEntity(httpEntity);
                httpUriRequest = httpPut;
                break;
            }
            case DELETE: {
                HttpDelete httpDelete = new HttpDelete(httpRequest.getUri());
                httpDelete.setConfig(requestConfig);
                httpUriRequest = httpDelete;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid HttpRequest.");
            }
        }

        setHttpUriRequestHeaders(httpUriRequest, httpRequest.getHeaders());

        return httpUriRequest;
    }

    private void setHttpUriRequestHeaders(HttpUriRequest httpUriRequest,
            Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpUriRequest.setHeader(header.getKey(), header.getValue());
        }
    }
}
