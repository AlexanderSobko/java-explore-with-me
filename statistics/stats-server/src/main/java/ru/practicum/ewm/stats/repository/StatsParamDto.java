package ru.practicum.ewm.stats.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsParamDto {

    @NotNull
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime start;
    @NotNull
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime end;
    List<String> uris;
    boolean unique;

}
