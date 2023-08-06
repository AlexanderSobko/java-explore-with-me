package ru.practicum.ewm.main.service.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.event.dto.LocationDto;
import ru.practicum.ewm.main.service.event.model.Location;

@Mapper
public interface LocationMapper {

    LocationMapper LOCATION_MAPPER = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)
    Location mapToLocation(LocationDto dto);

    LocationDto mapToLocationDto(Location location);

}
