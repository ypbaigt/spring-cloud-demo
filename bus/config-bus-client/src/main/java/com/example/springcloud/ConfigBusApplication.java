package com.example.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConfigBusApplication {

    public static void main(String args[]) {
        new SpringApplicationBuilder(ConfigBusApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
