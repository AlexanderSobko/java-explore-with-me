package ru.practicum.ewm.main.service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.exception.AlreadyExistsException;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.user.UserRepository;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public UserDto save(UserDto dto) {
        validateEmail(dto.getEmail());
        User user = User.mapToUser(dto);
        user = repo.save(user);
        log.info("Пользователь зарегистрирован! {}", user);
        return UserDto.mapToUserDto(user);
    }

    @Override
    public void deleteById(int id) {
        User user = getById(id);
        repo.delete(user);
        log.info("Пользователь удален! {}", user);
    }

    @Override
    public List<UserDto> getAll(List<Integer> ids, int from, int size) {
        if (ids != null && !ids.isEmpty()) {
            return repo.findAllByIdIn(ids).stream()
                    .map(UserDto::mapToUserDto)
                    .collect(Collectors.toList());
        } else {
            Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
                @Override
                public long getOffset() {
                    return from;
                }
            };
            return repo.findAll(pageable).stream()
                    .map(UserDto::mapToUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public User getById(int userId) {
        return repo.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id(%d) не найден!", userId)));
    }

    private void validateEmail(String email) {
        if (repo.existsByEmail(email)) {
            throw new AlreadyExistsException(String.format("Пользователь с email(%s) уже зарегестрирован!", email));
        }
    }

}
