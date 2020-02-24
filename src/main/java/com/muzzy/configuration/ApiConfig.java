package com.muzzy.configuration;

public enum ApiConfig {

    STOP_ENDPOINT("/api/chain/block/mining/stop"),
    ADD_BLOCK_ENDPOINT("/api/chain/block/add"),
    GET_ALL_FROM("/api/chain/block/allafter/"),
    GET_ALL("/api/chain/block/all"),
    GET_LAST_BLOCK("/api/chain/block/lasthash");

    String value;
    ApiConfig(String value) {
        this.value = value;
    }
}
