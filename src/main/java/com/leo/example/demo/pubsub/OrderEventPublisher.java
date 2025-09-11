package com.leo.example.demo.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class OrderEventPublisher {

    private final ObjectMapper mapper;
    private final String projectId;
    private final String topic;
    private volatile Publisher publisher; // cache

    public OrderEventPublisher(
            ObjectMapper mapper,
            @Value("${spring.cloud.gcp.project-id:${GOOGLE_CLOUD_PROJECT}}") String projectId,
            @Value("${app.order-created-topic:order-created}") String topic) {
        this.mapper = mapper;
        this.projectId = projectId;
        this.topic = topic;
    }

    private Publisher getPublisher() throws Exception {
        if (publisher == null) {
            synchronized (this) {
                if (publisher == null) {
                    var topicName = TopicName.of(projectId, topic);
                    publisher = Publisher.newBuilder(topicName).build();
                }
            }
        }
        return publisher;
    }

    public String publish(Object payload) throws Exception {
        var json = mapper.writeValueAsString(payload);
        var msg = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(json))
                .putAttributes("eventType", "ORDER_CREATED")
                .putAttributes("source", "cloud-run-app")
                .build();
        return getPublisher().publish(msg).get(10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void close() {
        if (publisher != null) {
            try { publisher.shutdown(); publisher.awaitTermination(5, TimeUnit.SECONDS); } catch (Exception ignore) {}
        }
    }
}
