package com.example.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
// GlobalFilter
public class TimerFilter implements GlobalFilter, Ordered {

    //Mono 异步模型
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //优美计时组件
        StopWatch timer = new StopWatch();
        timer.start(exchange.getRequest().getURI().getRawPath());

        exchange.getAttributes().put("requestTimeBegin", System.currentTimeMillis());

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    timer.stop();
                    log.info(timer.prettyPrint());
                })
        );
    }

    //数字越小 优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
