package com.kapkiai.bigdata.kafka.kafkaclient;

import com.kapkiai.bigdata.kafka.services.Publisher;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TimeoutException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PublisherTest {

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private Publisher publisher;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterAll
	public static void tearDown() {
		// Clean up code here
	}

	@Test
	public void publishMessageSuccessfully() throws Exception {
		Publisher.Message message = new Publisher.Message("testMessage", "testTopic");
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		SendResult<String, String> sendResult = new SendResult<>(null, new RecordMetadata(new TopicPartition("testTopic", 1), 1, 1, 1, Long.valueOf(1), 1, 1));
		future.set(sendResult);

		when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(future);

		publisher.publish(message);
	}

	@Test
	public void publishMessageThrowsInterruptedException() throws Exception {
		Publisher.Message message = new Publisher.Message("testMessage", "testTopic");
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		future.setException(new InterruptedException());

		when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(future);

		publisher.publish(message);
	}

	@Test
	public void publishMessageThrowsExecutionException() throws Exception {
		Publisher.Message message = new Publisher.Message("testMessage", "testTopic");
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		future.setException(new Throwable());

		when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(future);

		publisher.publish(message);
	}

	@Test
	public void publishMessageThrowsTimeoutException() throws Exception {
		Publisher.Message message = new Publisher.Message("testMessage", "testTopic");
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		future.setException(new TimeoutException());

		when(kafkaTemplate.send(any(String.class), any(String.class))).thenReturn(future);

		publisher.publish(message);
	}
}