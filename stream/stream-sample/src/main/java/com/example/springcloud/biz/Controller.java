package com.example.springcloud.biz;

import com.example.springcloud.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private MyTopic producer;

    @Autowired
    private GroupTopic groupTopicProducer;

    @Autowired
    private DelayedTopic delayedTopicProducer;

    @Autowired
    private ErrorTopic errorTopicProducer;

    @Autowired
    private RequeueTopic requeueTopicProducer;

    @Autowired
    private DlqTopic dlqTopicProducer;

    @Autowired
    private FallbackTopic fallbackTopicProducer;

    @PostMapping("send")
    public void sendMessage(@RequestParam String body) {
        producer.output().send(MessageBuilder.withPayload(body).build());
    }

    @PostMapping("sendToGroup")
    public void sendMessageToGroup(@RequestParam String body) {
        groupTopicProducer.output().send(MessageBuilder.withPayload(body).build());
    }

    @PostMapping("sendDM")
    public void sendDelayedMessage(@RequestParam String body,
                                   @RequestParam Integer seconds) {
        MessageBean msg = new MessageBean();
        msg.setPayload(body);

        log.info("ready to send delayed message");

        delayedTopicProducer.output().send(
                MessageBuilder.withPayload(msg)
                        .setHeader("x-delay", 1000 * seconds)
                        .build());
    }

    //异常重试（单机版）
    @PostMapping("sendError")
    public void sendErrorMessage(@RequestParam String body) {
        MessageBean msg = new MessageBean();
        msg.setPayload(body);
        errorTopicProducer.output().send(MessageBuilder.withPayload(msg).build());
    }

    //异常重试（联机版-重新入列）
    @PostMapping("requeue")
    public void sendErrorMessageToMQ(@RequestParam String body) {
        MessageBean msg = new MessageBean();
        msg.setPayload(body);
        requeueTopicProducer.output().send(MessageBuilder.withPayload(msg).build());
    }

    //死信队列测试
    @PostMapping("dlq")
    public void sendMessageToDlq(@RequestParam String body) {
        MessageBean msg = new MessageBean();
        msg.setPayload(body);
        dlqTopicProducer.output().send(MessageBuilder.withPayload(msg).build());
    }

    // fallback + 升版
    @PostMapping("fallback")
    public void sendMessageToFallback(@RequestParam String body,
                                      @RequestParam(value = "version", defaultValue = "1.0") String version) {
        MessageBean msg = new MessageBean();
        msg.setPayload(body);
        fallbackTopicProducer.output().send(
                MessageBuilder.withPayload(msg)
                        .setHeader("version", version)
                        .build()
        );
    }
}
