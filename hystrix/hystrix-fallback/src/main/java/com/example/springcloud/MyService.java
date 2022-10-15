package com.example.springcloud;

import com.example.springcloud.hystrix.Fallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * primary = true 有限注入该实现类
 */
@FeignClient(name = "feign-client", fallback = Fallback.class)
public interface MyService extends IService {
}
