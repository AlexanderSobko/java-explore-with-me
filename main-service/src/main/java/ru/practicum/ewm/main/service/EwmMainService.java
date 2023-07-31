package ru.practicum.ewm.main.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.client.StatsClient;

@SpringBootApplication
public class EwmMainService {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainService.class, args);
    }

    @Bean
    public StatsClient getClient() {
        return new StatsClient(new RestTemplate(), new ObjectMapper());
    }

}