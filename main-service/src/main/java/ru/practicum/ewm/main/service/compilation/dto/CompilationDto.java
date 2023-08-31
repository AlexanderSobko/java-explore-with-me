package ru.practicum.ewm.main.service.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.event.dto.EventSimpleDto;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    int id;
    String title;
    boolean pinned;
    List<EventSimpleDto> events;
    long likes;
    long dislikes;
    long rating;

}
