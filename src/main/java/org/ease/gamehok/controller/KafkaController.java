package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.kafka.MatchResultProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final MatchResultProducer producer;

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam String message
    ) {

        producer.sendMatchResult(message);

        return "Message sent to Kafka";
    }
}