package com.example.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EmbeddedKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    @Test
    public void testKafkaFlow() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", null);
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", StringDeserializer.class);
        consumerProps.put("group.id", "test-group");

        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(List.of("test-topic"));

        producer.sendMessage("test-topic", "Integration Test Message");

        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "test-topic");
        assertEquals("Integration Test Message", record.value());
    }
}
