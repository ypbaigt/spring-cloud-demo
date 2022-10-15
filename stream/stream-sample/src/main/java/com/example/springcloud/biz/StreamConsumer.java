package com.example.springcloud.biz;

import com.example.springcloud.topic.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@EnableBinding(value = {
        Sink.class,
        MyTopic.class,
        GroupTopic.class,
        DelayedTopic.class,
        ErrorTopic.class,
        RequeueTopic.class,
        DlqTopic.class,
        FallbackTopic.class
})
public class StreamConsumer {

    private AtomicInteger count = new AtomicInteger(1);

    @StreamListener(Sink.INPUT)
    public void consume(Object payload) {
        log.info("message consumed succesfully , payload = {}", payload);
    }

    @StreamListener(MyTopic.INPUT)
    public void consumeMyMessagge(Object payload) {
        log.info("message consumed succesfully , payload = {}", payload);
    }

    @StreamListener(GroupTopic.INPUT)
    public void consumeGroupMessagge(Object payload) {
        log.info("group message consumed succesfully , payload = {}", payload);
    }

    @StreamListener(DelayedTopic.INPUT)
    public void consumeDelayedMessagge(MessageBean bean) {
        log.info("delayed message consumed succesfully , payload = {}", bean.getPayload());
    }

    //异常重试（单机版）
    @StreamListener(ErrorTopic.INPUT)
    public void consumeErrorMessagge(MessageBean bean) {
        log.info("are you ok?");

        if (count.incrementAndGet() % 3 == 0) {
            log.info("fine thanks you. and you ?");
            count.set(0);
        } else {
            log.info("what's your problem?");
            throw new RuntimeException("I'm not OK");
        }
        log.info("delayed message consumed succesfully , payload = {}", bean.getPayload());
    }

    //异常重试（联机版-重新入列）
    @StreamListener(RequeueTopic.INPUT)
    public void requeueErrorMessagge(MessageBean bean) {
        log.info("are you ok?");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        throw new RuntimeException("I'm not OK");
    }

    //死信队列
    @StreamListener(DlqTopic.INPUT)
    public void consumeDlqMessagge(MessageBean bean) {
        log.info("Dlq are you ok?");

        if (count.incrementAndGet() % 3 == 0) {
            log.info("fine thanks you. and you ?");
        } else {
            log.info("what's your problem?");
            throw new RuntimeException("I'm not OK");
        }
    }

    //Fallback + 升级版本
    @StreamListener(FallbackTopic.INPUT)
    public void goodbyeBadGuy(MessageBean bean,
                              @Header("version") String version) {
        log.info("Fallback are you ok?");
        if ("1.0".equals(version)) {
            log.info("Fallback fine thanks you. and you ?");
        } else if ("2.0".equals(version)) {
            log.info("unsupport version");
            throw new RuntimeException("I'm not OK");
        } else {
            log.info("Fallback version={}", version);
        }
    }

    @ServiceActivator(inputChannel = "fallback-topic.fallback-group.errors")
    public void fallback(Message<?> message) {
        log.info("fallback entered");
    }
}
