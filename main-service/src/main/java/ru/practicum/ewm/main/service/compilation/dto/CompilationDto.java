package ru.practicum.ewm.main.service.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.event.dto.EventSimpleDto;

import java.util.List;
import java.util.stream.Collectors;

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

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents().stream()
                        .map(EventSimpleDto::mapToEventSimpleDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
