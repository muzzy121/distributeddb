package com.muzzy.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class RestApiConfig {
    private String dstPort;
    private String stopEndpoint;
    private String addBlockEndpoint;
}
