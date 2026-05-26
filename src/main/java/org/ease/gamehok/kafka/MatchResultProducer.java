package org.ease.gamehok.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchResultProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC =
            "match-result-topic";

    public void sendMatchResult(String message) {

        kafkaTemplate.send(TOPIC, message);

        System.out.println(
                "Produced Message: " + message
        );
    }
}