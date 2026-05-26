package org.ease.gamehok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class GamehokApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamehokApplication.class, args);
    }

}
