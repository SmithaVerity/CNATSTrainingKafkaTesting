import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableKafka
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Test
    public void testKafkaIntegration() throws InterruptedException {
        kafkaProducer.sendMessage("test-topic", "Hello, Kafka!");
        String receivedMessage = kafkaConsumer.getReceivedMessage();
        assertThat(receivedMessage).isEqualTo("Hello, Kafka!");
    }
}
