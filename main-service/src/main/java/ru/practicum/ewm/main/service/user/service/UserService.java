package ru.practicum.ewm.main.service.user.service;

import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface UserService {

    UserDto save(UserDto dto);

    List<UserDto> getAll(List<Integer> ids, int from, int size);

    void deleteById(int userId);

    User getById(int userId);

}
