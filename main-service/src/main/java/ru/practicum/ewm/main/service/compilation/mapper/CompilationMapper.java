package ru.practicum.ewm.main.service.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.event.mapper.EventMapper;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationMapper COMPILATION_MAPPER = Mappers.getMapper(CompilationMapper.class);

    @Mapping(target = "rating", expression = "java(compilation.getLikes() - compilation.getDislikes())")
    CompilationDto mapToCompilationDto(Compilation compilation);

}
