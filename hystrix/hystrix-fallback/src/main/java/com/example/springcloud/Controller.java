package com.example.springcloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private MyService myService;
    @Autowired
    private RequestCacheService requestCacheService;


    @GetMapping("/fallback")
    public String error() {
        return myService.error();
    }

    @GetMapping("/sayHi")
    public String sayHi() {
        return myService.sayHi();
    }

    @GetMapping("/timeout")
    public String timout(Integer timeout) {
        return myService.retry(timeout);
    }

    @GetMapping("/timeout2")
    @HystrixCommand(
            fallbackMethod = "timeoutFallback",
            commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
            }
    )
    public String timout2(Integer timeout) {
        return myService.retry(timeout);
    }

    public String timeoutFallback(Integer timeout) {
        return "success";
    }

    @GetMapping("/cache")
    public Friend cache(String name) {
        //lombok会在编译时插入变量名.close;
        @Cleanup HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();

        Friend friend = requestCacheService.requestCache(name);
        //name += "!";
        friend = requestCacheService.requestCache(name);
        return friend;
    }
}
