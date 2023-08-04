package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.event.dto.EventUpdateDto;
import ru.practicum.ewm.main.service.event.model.Event;

@Mapper
public interface EventMapper {

    EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "rate", ignore = true)
    @Mapping(target = "location.id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event updateEventFromDto(EventUpdateDto dto, @MappingTarget Event entity);

}
