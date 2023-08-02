package ru.practicum.ewm.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Validated
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate client;
    private final ObjectMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private final String url = "http://stats-server:9090";

    public String saveRequestLog(@NotNull RequestLogDto dto) {
        String path = url + "/hit";
        HttpEntity<RequestLogDto> requestEntity = new HttpEntity<>(dto, defaultHeaders());
        ResponseEntity<String> responseEntity = client.exchange(path, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody();
    }

    public List<StatsDto> getStats(@NotNull LocalDateTime start, @NotNull LocalDateTime end,
                                   List<String> uris, boolean isUnique) {
        HttpEntity<StatsDto> requestEntity = new HttpEntity<>(defaultHeaders());
        String path = UriComponentsBuilder.fromHttpUrl(url + "/stats")
                .queryParam("start", "{start}")
                .queryParam("end", "{end}")
                .queryParam("unique", "{unique}")
                .queryParam("uris", "{uris}")
                .encode()
                .toUriString();
        Map<String, Object> params = new HashMap<>();
        params.put("start", start.format(formatter));
        params.put("end", end.format(formatter));
        params.put("unique", String.valueOf(isUnique));
        params.put("uris", String.join(",", uris));
        System.out.println(params);
        ResponseEntity<Object> response = client.exchange(path, HttpMethod.GET, requestEntity, Object.class, params);
        return mapper.convertValue(response.getBody(), new TypeReference<>() {
        });
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}