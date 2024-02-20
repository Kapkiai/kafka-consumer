package com.kapkiai.bigdata.kafka.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Publisher class responsible for publishing messages to Kafka.
 */
@RestController
public class Publisher {

    private static final Logger log = LogManager.getLogger(Publisher.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Endpoint to publish a message to Kafka.
     *
     * @param message The message to be published.
     */
    @PostMapping("/publish")
    public void publish(final @RequestBody Message message){
        log.info("Publishing message: {}", message);

        produce(message);
    }

    /**
     * Method to produce a message to Kafka.
     *
     * @param message The message to be produced.
     */
    public void produce(final Message message){
        ListenableFuture<SendResult<String, String>> sendResultListenableFuture = kafkaTemplate.send(message.getTopic(), message.getMessage());

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

    /**
     * Inner class to represent a message.
     */
    public static class Message {
        private String message;
        private String topic;

        /**
         * Constructor for Message.
         *
         * @param message The message content.
         * @param topic The topic to which the message will be published.
         */
        public Message(String message, String topic) {
            this.message = message;
            this.topic = topic;
        }

        /**
         * Getter for message.
         *
         * @return The message content.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Getter for topic.
         *
         * @return The topic to which the message will be published.
         */
        public String getTopic() {
            return topic;
        }

        /**
         * Setter for message.
         *
         * @param message The new message content.
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * Setter for topic.
         *
         * @param topic The new topic to which the message will be published.
         */
        public void setTopic(String topic) {
            this.topic = topic;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", topic='" + topic + '\'' +
                    '}';
        }
    }
}