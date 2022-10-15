package com.example.springcloud.hystrix;

import com.example.springcloud.Friend;
import com.example.springcloud.MyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Fallback implements MyService {

    /*@Override
    public String error() {
        log.info("Fallback: I'm not sheep");
        return "Fallback: I'm not sheep";
    }*/

    @Override
    @HystrixCommand(fallbackMethod = "fallback2")
    public String error() {
        log.info("Fallback: I'm not sheep");
        throw new RuntimeException("first fallback");
    }

    @HystrixCommand(fallbackMethod = "fallback3")
    public String fallback2() {
        log.info("fallback again");
        throw new RuntimeException("fallback2");
    }

    public String fallback3() {
        log.info("fallback again and again");
        return "success";
    }

    @Override
    public String sayHi() {
        return null;
    }

    @Override
    public Friend sayHiPost(Friend friend) {
        return null;
    }

    @Override
    public String retry(int timeout) {
        return "you are late!";
    }

}
