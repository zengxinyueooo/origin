package com.navigation.config;

import lombok.Data;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "http", ignoreUnknownFields = true)
public class HttpClientConfig {

    private Integer maxTotal = 100; // 最大连接数
    private Integer defaultMaxPerRoute = 20; // 每个主机的最大连接数
    private Integer connectTimeout = 5000; // 连接超时时间
    private Integer connectionRequestTimeout = 5000; // 请求超时时间
    private Integer socketTimeout = 10000; // 响应超时时间

    /** HttpClient 连接池 */
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return connectionManager;
    }

    /** 请求配置 */
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }

    /** HttpClient */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager manager, RequestConfig config) {
        return HttpClientBuilder.create()
                .setConnectionManager(manager)
                .setDefaultRequestConfig(config)
                .build();
    }

    /** 请求工厂 */
    @Bean
    public ClientHttpRequestFactory requestFactory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    /** RestTemplate */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
        RestTemplate template = new RestTemplate(requestFactory);
        for (HttpMessageConverter<?> mc : template.getMessageConverters()) {
            if (mc instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) mc).setDefaultCharset(Charset.forName("UTF-8"));
            }
        }
        return template;
    }
}
