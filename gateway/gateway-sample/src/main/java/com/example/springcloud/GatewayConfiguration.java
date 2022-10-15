package com.example.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;

import java.time.ZonedDateTime;

@Configuration
public class GatewayConfiguration {


    @Autowired
    //全局filter默认生效
    private TimerFilter timerFilter;
    @Autowired
    private AuthFilter authFilter;

    @Bean
    @Order
    public RouteLocator customizedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/java/**")
                        .and().method(HttpMethod.POST)
                        .and().header("name")

                        .filters(f -> f.stripPrefix(1)
                                .addResponseHeader("java-param", "gateway-config")
                                //添加自已定义filter
                                .filter(authFilter)
                        )
                        .uri("lb://FEIGN-CLIENT")
                )
                .route(r -> r.path("/seckill/**")
                        //某个时间点之后才生效（项目启动1分钟之后才允许访问接口）
                        .and().after(ZonedDateTime.now().plusMinutes(1))
                        //.and().before()
                        //.and().between()
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://FEIGN-CLIENT"))
                .build();
    }

}
