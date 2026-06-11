package org.ease.gamehok.kafka;

import org.ease.gamehok.event.MatchCompletedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerService {

    @KafkaListener(
            topics = "match-completed-topic",
            groupId = "gamehok-group"
    )
    public void consume(
            MatchCompletedEvent event
    ) {

        System.out.println(
                "Kafka Event Received: " + event
        );
    }
}