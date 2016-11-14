package org.plano.data;

import org.springframework.http.HttpMethod;

import java.net.URL;
import java.util.Map;

/**
 * Created by ctsai on 11/6/16.
 */
public class HttpRequest {

    private URL endpoint;

    private String payload;

    private String charset;

    private HttpMethod httpMethod;

    private Map<String, String> headers;

    private Integer timeOutInMilliSeconds;
}
