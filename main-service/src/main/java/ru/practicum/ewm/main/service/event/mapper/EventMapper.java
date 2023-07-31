package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.event.dto.EventUpdateDto;
import ru.practicum.ewm.main.service.event.model.Event;

@Mapper
public interface EventMapper {

    EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event updateEventFromDto(EventUpdateDto dto, @MappingTarget Event entity);

}
