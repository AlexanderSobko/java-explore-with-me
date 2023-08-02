package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.event.model.Event;
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

    public static EventSimpleDto mapToEventSimpleDto(Event event) {
        return EventSimpleDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDto.mapToCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserSimpleDto.mapToUserSimpleDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }

}
