package com.muzzy.net.api;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.configuration.RestApiConfig;
import com.muzzy.domain.BlockVerified;
import com.muzzy.domain.TransactionOutput;
import com.muzzy.net.commands.StopMsg;
import com.muzzy.service.TransactionOutputService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Component
public class RESTApiControl {

    private final Logger LOG = LoggerFactory.getLogger(RESTApiControl.class);

    private final RestTemplate restTemplate;
    private final TransactionOutputService transactionOutputService;
    private final ConfigLoader configLoader;
    private RestApiConfig restApiConfig;

    public RESTApiControl(ConfigLoader configLoader, RestTemplate restTemplate, TransactionOutputService transactionOutputService) {
        this.configLoader = configLoader;
        this.restApiConfig = configLoader.getApi();
        this.restTemplate = restTemplate;
        this.transactionOutputService = transactionOutputService;
    }

    public void brakeMiningOnAllNodes(String blockHash) {
        HttpEntity<StopMsg> request = new HttpEntity<>(new StopMsg(blockHash));

        for (String address : configLoader.getAddresses()) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getStopEndpoint();
            LOG.debug(url);
            try {
                restTemplate.postForLocation(url, request);
            } catch (RestClientException re) {
                LOG.debug("Can't connect!" + re.getMostSpecificCause());
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
            LOG.debug(url);
            try {
                ResponseEntity<LinkedHashSet<BlockVerified>> rateResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, null, new ParameterizedTypeReference<LinkedHashSet<BlockVerified>>() {
                                });
                return rateResponse.getBody();
            } catch (RestClientException re) {
                LOG.warn("Can't get Blockchain data!");
            }
        }
        return null;
    }

    public LinkedHashSet<BlockVerified> getBlocksFromNetwork() {
        for (String address : configLoader.getAddresses()
        ) {
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getGetAll();
            LOG.info("Downloading data from network...");
            LOG.debug(url);
            try {
                ResponseEntity<LinkedHashSet<BlockVerified>> rateResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, null, new ParameterizedTypeReference<LinkedHashSet<BlockVerified>>() {
                                });
                return rateResponse.getBody();
            } catch (RestClientException re) {
//                re.printStackTrace();
                LOG.warn("Can't get Blockchain data!");
            }
        }
        return null;
    }
    public Set<TransactionOutput> getAllUtxo() {
        Map<String,Set<TransactionOutput>> tempUTXO = new HashMap<>();

        for (String address: configLoader.getAddresses()){
            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getGetAllUtxo();
            LOG.info("Downloading data from network...");
            LOG.debug(url);
            try {
                ResponseEntity<Set<TransactionOutput>> rateResponse =
                        restTemplate.exchange(url,
                                HttpMethod.GET, null, new ParameterizedTypeReference<Set<TransactionOutput>>() {
                                });
                tempUTXO.put(address,rateResponse.getBody());
            } catch (RestClientException re) {
//                re.printStackTrace();
                LOG.warn("Can't get UTXO list!");
            }
        }
        /**
         * This is simple sum all UTXOs from network
         *
         */
        Set<TransactionOutput> transactionOutputs = transactionOutputService.getAll();
        if(!tempUTXO.isEmpty()){
            tempUTXO.values().stream().forEach(transactionOutputs::addAll);
            return transactionOutputs;
        } else
        return null;
    }

    public void sendTransactionsToAllNodes(BlockVerified block) {
        HttpEntity<BlockVerified> request = new HttpEntity<>(block);
//
//        for (String address : configLoader.getAddresses()) {
//            String url = "http://" + address + ":" + restApiConfig.getDstPort() + restApiConfig.getAddBlockEndpoint();
//            LOG.debug(url);
//            try {
//                restTemplate.postForLocation(url, request);
//            } catch (RestClientException re) {
//                LOG.debug("Can't connect!");
//            }
//        }
    }
}
