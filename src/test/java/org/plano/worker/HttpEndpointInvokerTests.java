package org.plano.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.plano.data.HttpRequest;

import java.io.File;
import java.io.IOException;

/**
 * Created by ctsai on 11/19/16.
 */
public class HttpEndpointInvokerTests {

    @Mock
    private HttpClient httpClient;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenHttpClientIsNull() {
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenHttpRequestIsNull() {
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        httpEndpointInvoker.invoke(null);
    }

    @Test
    public void testWhenHttpRequestGetIsValidInvokeSuccess() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        File file = new File("src/test/resources/HttpRequestGet.json");
        HttpRequest httpRequest = objectMapper.readValue(file, HttpRequest.class);

        HttpResponse httpResponse = createHttpResponse(HttpStatus.SC_OK);
        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        Assert.assertTrue(httpEndpointInvoker.invoke(httpRequest));

        ArgumentCaptor<HttpGet> httpGetArgumentCaptor = ArgumentCaptor.forClass(HttpGet.class);

        Mockito.verify(httpClient, Mockito.times(1)).execute(httpGetArgumentCaptor.capture());

        HttpGet httpGet = httpGetArgumentCaptor.getValue();

        Assert.assertNotNull(httpGet);
        Assert.assertEquals(httpRequest.getUri(), httpGet.getURI().toString());
        Assert.assertEquals(httpRequest.getConnectionTimeoutMs().longValue(),
                httpGet.getConfig().getConnectionRequestTimeout());
        Assert.assertEquals(httpRequest.getSocketTimeoutMs().longValue(),
                httpGet.getConfig().getSocketTimeout());
        Assert.assertEquals(httpRequest.getConnectionRequestTimeoutMs().longValue(),
                httpGet.getConfig().getConnectionRequestTimeout());
    }

    @Test
    public void testWhenHttpRequestPostIsValidInvokeSuccess() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        File file = new File("src/test/resources/HttpRequestPost.json");
        HttpRequest httpRequest = objectMapper.readValue(file, HttpRequest.class);

        HttpResponse httpResponse = createHttpResponse(HttpStatus.SC_OK);
        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        Assert.assertTrue(httpEndpointInvoker.invoke(httpRequest));

        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);

        Mockito.verify(httpClient, Mockito.times(1)).execute(httpPostArgumentCaptor.capture());

        HttpPost httpPost = httpPostArgumentCaptor.getValue();

        Assert.assertNotNull(httpPost);
        Assert.assertEquals(httpRequest.getUri(), httpPost.getURI().toString());
        Assert.assertEquals(httpRequest.getConnectionTimeoutMs().longValue(),
                httpPost.getConfig().getConnectionRequestTimeout());
        Assert.assertEquals(httpRequest.getSocketTimeoutMs().longValue(),
                httpPost.getConfig().getSocketTimeout());
        Assert.assertEquals(httpRequest.getConnectionRequestTimeoutMs().longValue(),
                httpPost.getConfig().getConnectionRequestTimeout());
        Assert.assertNotNull(httpPost.getEntity());
    }

    @Test
    public void testWhenHttpRequestPutIsValidInvokeSuccess() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        File file = new File("src/test/resources/HttpRequestPut.json");
        HttpRequest httpRequest = objectMapper.readValue(file, HttpRequest.class);

        HttpResponse httpResponse = createHttpResponse(HttpStatus.SC_OK);
        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        Assert.assertTrue(httpEndpointInvoker.invoke(httpRequest));

        ArgumentCaptor<HttpPut> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPut.class);

        Mockito.verify(httpClient, Mockito.times(1)).execute(httpPostArgumentCaptor.capture());

        HttpPut httpPut = httpPostArgumentCaptor.getValue();

        Assert.assertNotNull(httpPut);
        Assert.assertEquals(httpRequest.getUri(), httpPut.getURI().toString());
        Assert.assertEquals(httpRequest.getConnectionTimeoutMs().longValue(),
                httpPut.getConfig().getConnectionRequestTimeout());
        Assert.assertEquals(httpRequest.getSocketTimeoutMs().longValue(),
                httpPut.getConfig().getSocketTimeout());
        Assert.assertEquals(httpRequest.getConnectionRequestTimeoutMs().longValue(),
                httpPut.getConfig().getConnectionRequestTimeout());
        Assert.assertNotNull(httpPut.getEntity());
    }

    @Test
    public void testWhenHttpRequestDeleteIsValidInvokeSuccess() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        File file = new File("src/test/resources/HttpRequestDelete.json");
        HttpRequest httpRequest = objectMapper.readValue(file, HttpRequest.class);

        HttpResponse httpResponse = createHttpResponse(HttpStatus.SC_OK);
        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        Assert.assertTrue(httpEndpointInvoker.invoke(httpRequest));

        ArgumentCaptor<HttpDelete> httpGetArgumentCaptor = ArgumentCaptor.forClass(HttpDelete.class);

        Mockito.verify(httpClient, Mockito.times(1)).execute(httpGetArgumentCaptor.capture());

        HttpDelete httpDelete = httpGetArgumentCaptor.getValue();

        Assert.assertNotNull(httpDelete);
        Assert.assertEquals(httpRequest.getUri(), httpDelete.getURI().toString());
        Assert.assertEquals(httpRequest.getConnectionTimeoutMs().longValue(),
                httpDelete.getConfig().getConnectionRequestTimeout());
        Assert.assertEquals(httpRequest.getSocketTimeoutMs().longValue(),
                httpDelete.getConfig().getSocketTimeout());
        Assert.assertEquals(httpRequest.getConnectionRequestTimeoutMs().longValue(),
                httpDelete.getConfig().getConnectionRequestTimeout());
    }

    @Test
    public void testInvokeWhenIOExceptionIsThrown() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEndpointInvoker httpEndpointInvoker = new HttpEndpointInvoker(httpClient);
        File file = new File("src/test/resources/HttpRequestPost.json");
        HttpRequest httpRequest = objectMapper.readValue(file, HttpRequest.class);

        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class)))
                .thenThrow(IOException.class);

        Assert.assertFalse(httpEndpointInvoker.invoke(httpRequest));
    }

    private HttpResponse createHttpResponse(int statusCode) {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 0);
        StatusLine statusLine = new BasicStatusLine(protocolVersion, statusCode, "good");
        return new BasicHttpResponse(statusLine);
    }
}
