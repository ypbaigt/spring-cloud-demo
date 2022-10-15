package com.example.springcloud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("eureka-client")
public interface IService {

    @GetMapping("sayHi")
    String sayHi();

}
