package com.example.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private IService service;

    @GetMapping("sayHi")
    public String sayHi() {
        return service.sayHi();
    }

    @PostMapping("sayHi")
    public Friend sayHi1() {
        Friend friend = new Friend();
        friend.setName("test");
        return service.sayHiPost(friend);
    }

    @GetMapping("retry")
    public String retry(Integer timeout) {
        return service.retry(timeout);
    }
}
