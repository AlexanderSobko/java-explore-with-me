package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.service.anotation.DateAfterHoursOrNull;

import java.time.LocalDateTime;

import static ru.practicum.ewm.main.service.event.dto.EventCreateDto.DATE_AFTER_HOURS_MESSAGE;
import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {

    @Length(min = 3, max = 120)
    String title;
    @Length(min = 20, max = 7000)
    String description;
    @Length(min = 20, max = 2000)
    String annotation;
    @JsonFormat(pattern = DATE_FORMAT)
    @DateAfterHoursOrNull(hours = 2, message = DATE_AFTER_HOURS_MESSAGE)
    LocalDateTime eventDate;
    Integer category;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW

    }

}

