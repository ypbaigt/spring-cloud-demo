package com.example.springcloud.topic;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DelayedTopic {

    String INPUT = "delayed-consumer";

    String OUTPUT = "delayed-producer";

    //消费者
    @Input(INPUT)
    SubscribableChannel input();

    //生产者
    @Output(OUTPUT)
    MessageChannel output();
}
