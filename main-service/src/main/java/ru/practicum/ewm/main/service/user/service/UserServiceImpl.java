package ru.practicum.ewm.main.service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.event.dto.SortParam;
import ru.practicum.ewm.main.service.exception.AlreadyExistsException;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.dto.Rate;
import ru.practicum.ewm.main.service.rate.event_rate.EventRateRepository;
import ru.practicum.ewm.main.service.rate.user_rate.UserRateRepository;
import ru.practicum.ewm.main.service.user.UserRepository;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.model.User;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.event.dto.SortParam.PRIVATE_RATING;
import static ru.practicum.ewm.main.service.event.dto.SortParam.RATING;
import static ru.practicum.ewm.main.service.rate.dto.Rate.mapDbResultToIdAndRateDtoMap;
import static ru.practicum.ewm.main.service.user.mapper.UserMapper.USER_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserRateRepository userRateRepo;
    private final EventRateRepository eventRateRepo;

    @Override
    public UserDto save(UserDto dto) {
        validateEmail(dto.getEmail());
        User user = User.mapToUser(dto);
        user = repo.save(user);
        log.info("Пользователь зарегистрирован! {}", user);
        return USER_MAPPER.mapToUserDto(user);
    }

    @Override
    public void deleteById(int id) {
        User user = getById(id);
        repo.delete(user);
        log.info("Пользователь удален! {}", user);
    }

    @Override
    public List<UserDto> getAll(List<Integer> ids, int from, int size, SortParam sortParam) {
        List<User> users;
        Comparator<UserDto> comparator;
        if (RATING.equals(sortParam)) {
            comparator = Comparator.comparing(UserDto::getRating).reversed();
        } else if (PRIVATE_RATING.equals(sortParam)) {
            comparator = Comparator.comparing(UserDto::getPrivateRating).reversed();
        } else {
            comparator = Comparator.comparingInt(UserDto::getId);
        }
        if (ids != null && !ids.isEmpty()) {
            users = repo.findAllByIdIn(ids);
        } else {
            Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
                @Override
                public long getOffset() {
                    return from;
                }
            };
            users = repo.findAll(pageable).getContent();
        }
        return setRates(users, true).stream()
                .map(USER_MAPPER::mapToUserDto)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public User getById(int userId) {
        User user = repo.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id(%d) не найден!", userId)));
        return setRates(List.of(user), false).get(0);
    }

    private void validateEmail(String email) {
        if (repo.existsByEmail(email)) {
            throw new AlreadyExistsException(String.format("Пользователь с email(%s) уже зарегестрирован!", email));
        }
    }

    private List<User> setRates(List<User> users, boolean isPrivate) {
        Map<Integer, Rate> rates = mapDbResultToIdAndRateDtoMap(userRateRepo.getRatesByUserIn(users));
        Map<Integer, Rate> privateRates = isPrivate ?
                mapDbResultToIdAndRateDtoMap(eventRateRepo.getPrivateRatesByUserIn(users))
                : Map.of();
        users.forEach(user -> {
            Rate rate = rates.getOrDefault(user.getId(), new Rate());
            user.setLikes(rate.getLikes());
            user.setDislikes(rate.getDislikes());
            Rate privateRate = privateRates.getOrDefault(user.getId(), new Rate());
            user.setPrivateRating(privateRate.getLikes() - privateRate.getDislikes());
        });
        return users;
    }

}
