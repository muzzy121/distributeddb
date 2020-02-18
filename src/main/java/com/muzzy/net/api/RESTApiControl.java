package com.muzzy.net.api;

import com.muzzy.configuration.ConfigLoader;
import com.muzzy.net.commands.StopMsg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Component
public class RESTApiControl {

    private RestTemplate restTemplate;
    private final ConfigLoader configLoader;

    public RESTApiControl(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }
    public void brakeMiningOnAllNodes(){
        StopMsg stopMsg = new StopMsg("1234");
        configLoader.getAddresses().forEach(address -> restTemplate.postForLocation(address + configLoader.getStop_mining(), stopMsg));
    }
}



//
//        HttpEntity<Foo> request = new HttpEntity<>(new Foo("bar"));
//        Foo foo = restTemplate.postForObject(fooResourceUrl, request, Foo.class);
//        assertThat(foo, notNullValue());
//        assertThat(foo.getName(), is("bar"));
//
//        5.2. The postForLocation API
//        Similarly, let's have a look at the operation that – instead of returning the full Resource, just returns the Location of that newly created Resource:
//
//        HttpEntity<Foo> request = new HttpEntity<>(new Foo("bar"));
//        URI location = restTemplate
//        .postForLocation(fooResourceUrl, request);
//        assertThat(location, notNullValue());
//        5.3. The exchange API
//        Let's have a look at how to do a POST with the more generic exchange API:
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<Foo> request = new HttpEntity<>(new Foo("bar"));
//        ResponseEntity<Foo> response = restTemplate
//        .exchange(fooResourceUrl, HttpMethod.POST, request, Foo.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
//
//        Foo foo = response.getBody();
//
//        assertThat(foo, notNullValue());
//        assertThat(foo.getName(), is("bar"));