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

    AtomicInteger count = new AtomicInteger();


    // @KafkaListener(topics = "${kafka-topic}") -> Use this when consuming from a single topic

    @KafkaListener(topics = "#{'${kafka-topic}'.split(',')}") // -> Use this when cnsuming from multiple topics
    public void consume(ConsumerRecord<String, String> record){

        // Your Business logic goes here. The `record` object represents a message consumed from Kafka.
        // You can choose to do anything on it, write the record to a db, filter, enrich it.......

        log.info("Message received -> Timestamp: {}, key: {}, Value: {}, Record Count: {}", new Object[]{record.timestamp(), record.key(), record.value(), count.incrementAndGet()});

    }
    
}
