package org.ease.gamehok.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
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