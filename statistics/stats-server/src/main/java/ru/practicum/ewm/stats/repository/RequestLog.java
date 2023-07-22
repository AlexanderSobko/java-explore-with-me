package ru.practicum.ewm.stats.repository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.stats.dto.RequestLogDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "request_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "app", nullable = false)
    String app;
    @Column(name = "uri", nullable = false)
    String uri;
    @Column(name = "ip", nullable = false)
    String ip;
    @Column(name = "time_stamp", nullable = false)
    LocalDateTime timeStamp;

    public static RequestLog mapToLog(RequestLogDto dto) {
        return RequestLog.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .build();
    }

}
