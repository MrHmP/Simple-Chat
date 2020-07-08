package chat.simple.sessions.controller;

import chat.simple.sessions.configuration.TopicConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private KafkaTemplate<String, String> kafkaTemplate;
    private TopicConfiguration topicConfiguration;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate, TopicConfiguration topicConfiguration) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicConfiguration = topicConfiguration;
    }

    @PostMapping(value = "/message/send")
    public String Test(@RequestBody String message) {
        kafkaTemplate.send(topicConfiguration.getTopic(), message);
        return Thread.currentThread().getName();
    }

}
