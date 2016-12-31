package org.plano.worker;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for {@link HttpEndpointInvoker}. Set up
 * HttpClient for {@link HttpEndpointInvoker}.
 */
@Configuration
public class HttpEndpointInvokerConfiguration {

    @Value("${http.connection.timeout.ms}")
    private Integer httpConnectionTimeoutInMs;

    @Value("${http.socket.timeout.ms}")
    private Integer httpSocketTimeoutMs;

    @Value("${http.connection.request.timeout.ms}")
    private Integer httpConnectionRequestTimeoutMs;

    /**
     * Create a {@link HttpClient} with customized configuration.
     * @return {@link HttpClient}
     */
    @Bean
    public HttpClient createHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpSocketTimeoutMs)
                .setConnectTimeout(httpConnectionTimeoutInMs)
                .setConnectionRequestTimeout(httpConnectionRequestTimeoutMs)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Constructor. To pass checkstyle.
     */
    public HttpEndpointInvokerConfiguration() {}
}
