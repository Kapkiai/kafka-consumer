# kafka-consumer & Producer
This is a sample spring kafka client that shows how to consume and produce messages to a Kafka topic in _Spring Way!!_

## Prerequisites
- Java 11 +
- Maven
- Kafka

## How to build
```bash
mvn clean install

# To skip tests
mvn clean install -DskipTests
```

## How to run the application. This starts the consumer and producer(interfaced by a REST endpoint)
```bash
# With properties file at resources
java -jar target/kafka-client-0.0.2.jar

# With properties file at custom location
java -jar target/kafka-client-0.0.2.jar --spring.config.location=file:/path/to/your/application.properties
```

## How to test
The application exposes a REST endpoint to produce messages to a Kafka topic. You can use the following curl command to produce messages to the topic.
```bash
curl -X POST -H "Content-Type: application/json" -d '{"topic": "test-topic", "message": "Hello Kafka!!"}' http://localhost:8080/publish
```
Consumer's topic is specified in the application.properties file. If a message is produced to the same topic, the consumer will consume the message and log it to the console.