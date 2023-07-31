package ru.practicum.ewm.main.service.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.main.service.event.model.EventState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetEventsAdminDto {

    List<Integer> users;
    List<EventState> states;
    List<Integer> categories;
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATE_FORMAT)
    LocalDateTime rangeEnd;
    @PositiveOrZero
    int from = 0;
    @Positive
    int size = 10;

}
