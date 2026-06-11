package org.ease.gamehok.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class MatchResultConsumer {

    @KafkaListener(
            topics = "match-result-topic",
            groupId = "gamehok-group"
    )
    public void consume(String message) {

        System.out.println(
                "Consumed Message: " + message
        );
    }
}