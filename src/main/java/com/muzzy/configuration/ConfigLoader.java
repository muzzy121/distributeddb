package com.muzzy.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "develop")
@Getter
@Setter
public class ConfigLoader {
    private List<String> addresses = new ArrayList<>();
    private Integer port;
    private Integer serverport;
}
