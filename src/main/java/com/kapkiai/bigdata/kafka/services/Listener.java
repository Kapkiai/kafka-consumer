package com.kapkiai.bigdata.kafka.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class Listener {
    private static final Logger log = LogManager.getLogger(Listener.class);

    AtomicInteger count = new AtomicInteger();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.producer.topics:Test}")
    private String topicName;
    @Value("${kafka.producer.publish:false}")
    private Boolean shouldPublish;


    // @KafkaListener(topics = "${kafka-topic}") -> Use this when consuming from a single topic

    @KafkaListener(topics = "#{'${kafka.consumer.topics:test}'.split(',')}") // -> Use this when cnsuming from multiple topics
    public void consume(ConsumerRecord<String, String> record){

        // Your Business logic goes here. The `record` object represents a message consumed from Kafka.
        // You can choose to do anything on it, write the record to a db, filter, enrich it.......

        log.info("Message received -> Timestamp: {}, key: {}, Value: {}, Record Count: {}", new Object[]{record.timestamp(), record.key(), record.value(), count.incrementAndGet()});

        if (shouldPublish) {
            produce(record.value());
        }

    }

    public void produce(final String message){
        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafkaTemplate.send(topicName, message);

        try {
            SendResult<String, String> sendResult = sendResultListenableFuture.get(10, TimeUnit.SECONDS);
            log.info("Send Results Metadata: Partition=[{}] | Offset=[{}]", sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
        }
        catch (InterruptedException e) {
            log.error("Interrupt exception", e);
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException e) {
            log.error("Execution exception", e);
            Thread.currentThread().interrupt();
        }
        catch (TimeoutException e) {
            log.error("Timeout Exception", e);
            Thread.currentThread().interrupt();
        }
    }
    
}
