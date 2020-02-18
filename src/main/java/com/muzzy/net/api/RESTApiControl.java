package com.muzzy.net.api;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.configuration.RestApiConfig;
import com.muzzy.configuration.RestTemplateConfig;
import com.muzzy.net.commands.StopMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Component
public class RESTApiControl {

    private RestTemplate restTemplate;
    private final ConfigLoader configLoader;
    private final RestTemplateConfig restTemplateConfig;

    public RESTApiControl(ConfigLoader configLoader, RestTemplateConfig restTemplateConfig) {
        this.configLoader = configLoader;
        this.restTemplateConfig = restTemplateConfig;
    }
    public void brakeMiningOnAllNodes(String blockHash){
        restTemplate = new RestTemplate(restTemplateConfig.getHttpRequestFactory());
        RestApiConfig restApiConfig = configLoader.getApi();
        HttpEntity<StopMsg> request = new HttpEntity<>(new StopMsg(blockHash));

        for (String address : configLoader.getAddresses()) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getStopEndpoint();
            System.out.println("Sending stop to: " + url);
            restTemplate.postForLocation(url, request);
        }
    }
}

