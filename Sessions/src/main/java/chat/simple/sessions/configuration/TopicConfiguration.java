package chat.simple.sessions.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.lang.Short.*;

@Configuration
public class TopicConfiguration {

    private ProducerConfiguration producerConfiguration;

    @Value("${kafka.topic.name}")
    private String TOPIC;

    @Value("${kafka.topic.partitions}")
    private String partitions;

    @Value("${kafka.topic.replications}")
    private String replications;


    public TopicConfiguration(ProducerConfiguration producerConfiguration) {
        this.producerConfiguration = producerConfiguration;
    }

    @PostConstruct
    public void createKafkaTopic() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.producerConfiguration.getBrokerUrl());
        AdminClient kafkaAdminClient = KafkaAdminClient.create(properties);
        CreateTopicsResult result = kafkaAdminClient.createTopics(
                Arrays.asList(new NewTopic(TOPIC, Integer.valueOf(partitions), valueOf(replications)))
                        .stream().collect(Collectors.toList()));
        try {
            result.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Topic already exists");
        }
    }

    public String getTopic() {
        return TOPIC;
    }
}
