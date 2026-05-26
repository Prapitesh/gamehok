package org.ease.gamehok.kafka;

import org.ease.gamehok.event.MatchCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
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