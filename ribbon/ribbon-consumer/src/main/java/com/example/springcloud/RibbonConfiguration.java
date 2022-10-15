package com.example.springcloud;

import com.example.springcloud.rules.MyRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClient(name = "eureka-client",configuration = MyRule.class)
public class RibbonConfiguration {

    /*@Bean
    public IRule defaultLBStrategy() {
        return new RandomRule();
    }*/

}
