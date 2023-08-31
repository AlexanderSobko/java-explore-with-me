package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.event.model.EventState;
import ru.practicum.ewm.main.service.user.dto.UserSimpleDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDto {

    int id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    int participantLimit;
    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime createdOn;
    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime publishedOn;
    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime eventDate;
    String description;
    UserSimpleDto initiator;
    LocationDto location;
    boolean paid;
    boolean requestModeration;
    EventState state;
    String title;
    long views;
    long likes;
    long dislikes;
    long rating;

}
