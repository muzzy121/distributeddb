package com.muzzy.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RestTemplateConfig {
    private ClientHttpRequestFactory httpRequestFactory;

    private RestTemplateConfig(){
        this.httpRequestFactory = getClientHttpRequestFactory();
    }
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 2;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        int timeout = 5;
//        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(timeout)
//                .setConnectionRequestTimeout(timeout)
//                .setSocketTimeout(timeout)
//                .build();
//        CloseableHttpClient client = HttpClientBuilder
//                .create()
//                .setDefaultRequestConfig(config)
//                .build();
//        return new HttpComponentsClientHttpRequestFactory(client);
//    }
}
