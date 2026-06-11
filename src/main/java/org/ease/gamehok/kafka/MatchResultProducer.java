package org.ease.gamehok.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true", matchIfMissing = false)
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