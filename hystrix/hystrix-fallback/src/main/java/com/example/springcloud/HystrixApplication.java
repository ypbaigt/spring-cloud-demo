package com.example.springcloud;

import feign.Feign;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class HystrixApplication {

    public static void main(String args[]) {

        //得到方法唯一签名
        /*try {
            System.out.println(Feign.configKey(MyService.class,
                    MyService.class.getMethod("retry", int.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
        new SpringApplicationBuilder(HystrixApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}