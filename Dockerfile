FROM openjdk:11-jre-slim
LABEL maintainer="mathew.kapkiai@safaricom.et"
VOLUME /tmp
ADD target/kafka-client-0.0.1-SNAPSHOT.jar kafka-client.jar
RUN /bin/sh -c 'touch /kafka-client.jar'
ENV TZ=Africa/Nairobi
ENTRYPOINT ["java","-Xmx256m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom","-jar","/kafka-client.jar"]
