package ru.practicum.ewm.main.service.event.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetEventsDto {

    String text;
    List<Integer> categories;
    Boolean paid;
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime rangeEnd;
    boolean onlyAvailable;
    SortParam sort;
    int from = 0;
    int size = 10;

    public enum SortParam {

        EVENT_DATE, VIEWS

    }

}
