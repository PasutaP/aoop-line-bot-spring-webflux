package com.example.lineapibackend;

import com.linecorp.bot.spring.boot.annotation.EnableLineMessaging;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@EnableLineMessaging
@SpringBootApplication
public class LineApiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(LineApiBackendApplication.class, args);
    }
}
