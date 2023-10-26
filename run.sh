mvn clean package -DskipTests
java -jar target/kafka-client-0.0.1-SNAPSHOT.jar --spring.config.location=./application.properties