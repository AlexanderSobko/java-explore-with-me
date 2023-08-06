package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.user.dto.UserSimpleDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSimpleDto {

    int id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime eventDate;
    UserSimpleDto initiator;
    boolean paid;
    String title;
    long views;
    long rating;

}
