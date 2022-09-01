package com.kapkiai.bigdata.kafka.services;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Listener {
    private static final Logger log = LogManager.getLogger(Listener.class);

    int count = new AtomicInteger().incrementAndGet();
    // @KafkaListener(topics = "${kafka-topic}")


    @KafkaListener(topics = "#{'${kafka-topic}'.split(',')}")
    public void consume(ConsumerRecord<String, String> record){

        log.info("Message received -> Timestamp: {}, key: {}, Value: {}, Record Count: {}", new Object[]{record.timestamp(), record.key(), record.value(), count++});

    }
    
}
