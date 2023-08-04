package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.EventState;
import ru.practicum.ewm.main.service.rate.dto.RateDto;
import ru.practicum.ewm.main.service.user.dto.UserSimpleDto;
import lombok.experimental.FieldDefaults;

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
    RateDto rate;



    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoryDto.mapToCategoryDto(event.getCategory()))
                .initiator(UserSimpleDto.mapToUserSimpleDto(event.getInitiator()))
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .location(LocationDto.mapToLocationDto(event.getLocation()))
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .paid(event.isPaid())
                .rate(event.getRate())
                .build();
    }


}
