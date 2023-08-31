package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.category.mapper.CategoryMapper;
import ru.practicum.ewm.main.service.event.dto.EventCreateDto;
import ru.practicum.ewm.main.service.event.dto.EventDto;
import ru.practicum.ewm.main.service.event.dto.EventSimpleDto;
import ru.practicum.ewm.main.service.event.dto.EventUpdateDto;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.user.mapper.UserMapper;

@Mapper(uses = {LocationMapper.class, UserMapper.class, CategoryMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    EventMapper EVENT_MAPPER = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event mapToEvent(EventUpdateDto dto, @MappingTarget Event entity);

    @Mapping(target = "category", ignore = true)
    Event mapToEvent(EventCreateDto dto);

    @Mapping(target = "rating", expression = "java(event.getLikes() - event.getDislikes())")
    EventDto mapToEventDto(Event event);

    @Mapping(target = "rating", expression = "java(event.getLikes() - event.getDislikes())")
    EventSimpleDto mapToEventSimpleDto(Event event);

}
