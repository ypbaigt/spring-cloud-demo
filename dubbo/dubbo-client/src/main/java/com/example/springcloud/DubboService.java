package com.example.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

@Service
@Slf4j
public class DubboService implements IDubboService {

    @Override
    public Product publish(Product product) {
        log.info("publish product:{}", product.getName());
        return product;
    }
}
