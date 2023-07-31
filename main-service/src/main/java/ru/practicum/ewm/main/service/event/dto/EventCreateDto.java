package ru.practicum.ewm.main.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.service.anotation.DateAfterHoursOrNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {

    public static final String DATE_AFTER_HOURS_MESSAGE =
            "Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента!";

    @NotBlank
    @Length(min = 3, max = 120)
    String title;
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT)
    @DateAfterHoursOrNull(hours = 2, message = DATE_AFTER_HOURS_MESSAGE)
    LocalDateTime eventDate;
    @NotNull
    @Positive
    Integer category;
    @NotNull
    LocationDto location;
    boolean paid;
    @PositiveOrZero
    int participantLimit;
    boolean requestModeration = true;

}
