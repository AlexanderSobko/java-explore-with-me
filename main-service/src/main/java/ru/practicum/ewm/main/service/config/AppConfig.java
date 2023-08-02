package ru.practicum.ewm.main.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.stats.client.StatsClient;

@Configuration
public class AppConfig {

    @Bean
    public StatsClient getClient() {
        return new StatsClient(new RestTemplate(), new ObjectMapper());
    }

}
