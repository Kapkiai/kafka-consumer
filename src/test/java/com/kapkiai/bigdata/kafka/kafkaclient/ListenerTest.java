package com.kapkiai.bigdata.kafka.kafkaclient;

import com.kapkiai.bigdata.kafka.services.Listener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.listener.MessageListener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ListenerTest {

    @Mock
    private MessageListener<String, String> messageListener;

    @InjectMocks
    private Listener listener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void consumeValidMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("testTopic", 1, 1, "testKey", "testValue");
        listener.consume(record);
        verify(messageListener, times(1)).onMessage(any(ConsumerRecord.class));
    }

    @Test
    public void consumeNullMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("testTopic", 1, 1, null, null);
        listener.consume(record);
        verify(messageListener, times(1)).onMessage(any(ConsumerRecord.class));
    }

    @Test
    public void consumeEmptyMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("testTopic", 1, 1, "", "");
        listener.consume(record);
        verify(messageListener, times(1)).onMessage(any(ConsumerRecord.class));
    }
}