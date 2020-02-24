package com.muzzy.net.api;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.configuration.RestApiConfig;
import com.muzzy.domain.BlockVerified;
import com.muzzy.net.commands.StopMsg;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashSet;

@Getter
@Setter
@Component
public class RESTApiControl {

    private final Logger LOG = LoggerFactory.getLogger(RESTApiControl.class);

    @Autowired
    private RestTemplate restTemplate;

    private final ConfigLoader configLoader;
    private RestApiConfig restApiConfig;

    public RESTApiControl(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.restApiConfig = configLoader.getApi();
    }

    public void brakeMiningOnAllNodes(String blockHash) {
        HttpEntity<StopMsg> request = new HttpEntity<>(new StopMsg(blockHash));

        for (String address : configLoader.getAddresses()) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getStopEndpoint();
            LOG.info(url);
            try {
                restTemplate.postForLocation(url, request);
            } catch (RestClientException re) {
                LOG.info("Can't connect!" + re.getMostSpecificCause());
            }
        }
    }

    public void sendBlockToAllNodes(BlockVerified block) {
        HttpEntity<BlockVerified> request = new HttpEntity<>(block);

        for (String address : configLoader.getAddresses()) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getAddBlockEndpoint();
            LOG.debug(url);
            try {
                restTemplate.postForLocation(url, request);
            } catch (RestClientException re) {
                LOG.debug("Can't connect!");
            }
        }
    }

    public LinkedHashSet<BlockVerified> getBlocksFromNetwork(String hash) {
        for (String address : configLoader.getAddresses()
        ) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getGetAllFrom() + hash;
            LOG.info("Downloading data from network...");
            LOG.info(url);
            try {
                ResponseEntity<LinkedHashSet<BlockVerified>> rateResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, null, new ParameterizedTypeReference<LinkedHashSet<BlockVerified>>() {
                                });
                return rateResponse.getBody();
            } catch (RestClientException re) {
                LOG.info("Can't get Blockchain data!");
            }
        }
        return null;
    }

    public LinkedHashSet<BlockVerified> getBlocksFromNetwork() {
        for (String address : configLoader.getAddresses()
        ) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getGetAll();
            LOG.info("Downloading data from network...");
            LOG.info(url);
            try {
                ResponseEntity<LinkedHashSet<BlockVerified>> rateResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, null, new ParameterizedTypeReference<LinkedHashSet<BlockVerified>>() {
                                });
                return rateResponse.getBody();
            } catch (RestClientException re) {
                re.printStackTrace();
                LOG.info("Can't get Blockchain data!");
            }
        }
        return null;
    }
}
