package ru.practicum.ewm.main.service.rate.user_rate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.user_rate.UserRateRepository;
import ru.practicum.ewm.main.service.rate.user_rate.model.UserRate;
import ru.practicum.ewm.main.service.rate.user_rate.model.UserRateIdClass;
import ru.practicum.ewm.main.service.user.service.UserService;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRateServiceImp  implements UserRateService {

    UserRateRepository userRateRepo;
    UserService userService;

    @Override
    public void rateUser(boolean isLike, int fanId, int userId) {
        if (fanId == userId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Можно оценить только другого пользователя!");
        }
        UserRateIdClass idClass = getUserRateIdClass(fanId, userId);
        UserRate userRate = userRateRepo.save(UserRate.builder()
                .idClass(idClass)
                .isLike(isLike)
                .build());
        log.info("Оценка к пользователю с id({}) сохранена! {}", userId, userRate);
    }

    @Override
    public void updateUserRate(boolean isLike, int fanId, int userId) {
        UserRate userRate = getUserRate(fanId, userId);
        userRate.setLike(isLike);
        userRate = userRateRepo.save(userRate);
        log.info("Оценка к пользователю с id({}) изменена! {}", userId, userRate);
    }

    @Override
    public void deleteUserRate(int fanId, int userId) {
        UserRate userRate = getUserRate(fanId, userId);
        userRateRepo.delete(userRate);
        log.info("Оценка к пользователю с id({}) удалена! {}", userId, userRate);
    }

    private UserRate getUserRate(int fanId, int userId) {
        UserRateIdClass idClass = getUserRateIdClass(fanId, userId);
        return userRateRepo.findById(idClass).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Пользователь с id(%d) не оценивал пользователя с id(%d)!", fanId, userId)));
    }

    private UserRateIdClass getUserRateIdClass(int fanId, int userId) {
        return UserRateIdClass.builder()
                .fan(userService.getById(fanId))
                .user(userService.getById(userId))
                .build();
    }

}
