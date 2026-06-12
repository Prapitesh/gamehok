package org.ease.gamehok.controller;

import org.ease.gamehok.kafka.MatchResultProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired(required = false)
    private MatchResultProducer producer;

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam String message
    ) {

        if (producer != null) {
            producer.sendMatchResult(message);
            return "Message sent to Kafka";
        }

        return "Kafka is not enabled";
    }
}