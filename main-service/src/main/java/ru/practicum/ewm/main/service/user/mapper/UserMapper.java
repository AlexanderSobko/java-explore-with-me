package ru.practicum.ewm.main.service.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.dto.UserSimpleDto;
import ru.practicum.ewm.main.service.user.model.User;

@Mapper
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "rating", expression = "java(user.getLikes() - user.getDislikes())")
    UserDto mapToUserDto(User user);

    @Mapping(target = "rating", expression = "java(user.getLikes() - user.getDislikes())")
    UserSimpleDto mapToUserSimpleDto(User user);

}
