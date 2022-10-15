package com.example.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller implements IService {

    @Value("${server.port}")
    private String port;

    @Override
    public String sayHi() {
        if (1==1) throw new RuntimeException("sayHi");
        return "this is " + port;
    }

    @Override
    public Friend sayHiPost(@RequestBody Friend friend) {
        log.info("you are " + friend.getName());
        friend.setPort(port);
        return friend;
    }

    @Override
    public String retry(@RequestParam(name = "timeout") int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("retry port:" + port);
        return port;
    }

    @Override
    public String error() {
        throw new RuntimeException("black sheep");
    }
}
