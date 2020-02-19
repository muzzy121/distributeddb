package com.muzzy.net.api;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.configuration.RestApiConfig;
import com.muzzy.domain.Block;
import com.muzzy.net.commands.StopMsg;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
@Setter
@Component
public class RESTApiControl {

    private final Logger LOG = LoggerFactory.getLogger(RESTApiControl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationContext context;

    private final ConfigLoader configLoader;
    private ExecutorService executorService;
    private RestApiConfig restApiConfig;

    public RESTApiControl(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.restApiConfig = configLoader.getApi();
    }
    public void brakeMiningOnAllNodes(String blockHash){

        HttpEntity<StopMsg> request = new HttpEntity<>(new StopMsg(blockHash));

        for (String address : configLoader.getAddresses()) {
            Callable send = new Callable() {
                @Override
                public Object call() throws Exception {
                    String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getStopEndpoint();
                    LOG.info(url);
                    restTemplate.postForLocation(url, request);
                    LOG.info("Can't connect!");
                    return null;
                }
            };
            executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(send);
            executorService.shutdown();
        }
    }
    public void sendBlockToAllNodes(Block block){
        RestApiConfig restApiConfig = configLoader.getApi();
        HttpEntity<Block> request = new HttpEntity<>(block);

        for (String address : configLoader.getAddresses()) {
            Callable send = new Callable() {
                @Override
                public Object call() throws Exception {
                    String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getAddBlockEndpoint();
                    LOG.info(url);
                    restTemplate.postForLocation(url, request);
                    LOG.info("Can't connect!");
                    return null;
                }
            };
            executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(send);
            executorService.shutdown();
        }
    }
}

