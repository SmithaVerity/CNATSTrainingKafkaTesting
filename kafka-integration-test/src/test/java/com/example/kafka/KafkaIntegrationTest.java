package com.example.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EmbeddedKafka;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@EnableKafka
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaConsumer kafkaConsumer;
    
    @Test
    public void testKafkaIntegration() throws InterruptedException {

        kafkaProducer.sendMessage("test-topic", "Hello, Kafka!");
        String receivedMessage = kafkaConsumer.getReceivedMessage();
        assertThat(receivedMessage).isEqualTo("Hello, Kafka!");

        
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
