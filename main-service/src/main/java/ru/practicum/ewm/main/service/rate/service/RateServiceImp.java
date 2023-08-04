package ru.practicum.ewm.main.service.rate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRate;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRateIdClass;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRateRepository;
import ru.practicum.ewm.main.service.rate.event_rate.EventRate;
import ru.practicum.ewm.main.service.rate.event_rate.EventRateIdClass;
import ru.practicum.ewm.main.service.rate.event_rate.EventRateRepository;
import ru.practicum.ewm.main.service.rate.user_rate.UserRate;
import ru.practicum.ewm.main.service.rate.user_rate.UserRateIdClass;
import ru.practicum.ewm.main.service.rate.user_rate.UserRateRepository;
import ru.practicum.ewm.main.service.request.service.RequestService;
import ru.practicum.ewm.main.service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.practicum.ewm.main.service.request.model.RequestStatus.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateServiceImp implements RateService {

    UserRateRepository userRateRepo;
    EventRateRepository eventRateRepo;
    CompilationRateRepository compilationRateRepo;
    UserService userService;
    EventService eventService;
    RequestService requestService;
    CompilationService compilationService;

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

    @Override
    public void rateEvent(boolean isLike, int fanId, int eventId) {
        EventRateIdClass idClass = getEventRateIdClass(fanId, eventId);
        if (idClass.getEvent().getEventDate().isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Событие еще не началось! Можно оценить только посещенное событие!");
        }
        if (!requestService.existByByRequesterAndEventAndStatus(idClass.getFan(), idClass.getEvent(), CONFIRMED)) {
            throw new NotFoundException(
                    "Пользователь не участвовал в этом событии! Можно оценить только посещенное событие!");
        }
        EventRate eventRate = eventRateRepo.save(EventRate.builder()
                .idClass(idClass)
                .isLike(isLike)
                .build());
        log.info("Оценка к событию с id({}) сохранена! {}", eventId, eventRate);
    }

    public void updateEventRate(boolean isLike, int fanId, int eventId) {
        EventRate eventRate = getEventRate(fanId, eventId);
        eventRate.setLike(isLike);
        eventRate = eventRateRepo.save(eventRate);
        log.info("Оценка к событию с id({}) изменена! {}", eventId, eventRate);
    }

    @Override
    public void deleteEventRate(int fanId, int eventId) {
        EventRate eventRate = getEventRate(fanId, eventId);
        eventRateRepo.delete(eventRate);
        log.info("Оценка к событию с id({}) удалена! {}", eventId, eventRate);
    }

    private EventRate getEventRate(int fanId, int eventId) {
        EventRateIdClass idClass = getEventRateIdClass(fanId, eventId);
        return eventRateRepo.findById(idClass).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Пользователь с id(%d) не оценивал событие с id(%d)!", fanId, eventId)));
    }

    private EventRateIdClass getEventRateIdClass(int fanId, int eventId) {
        return EventRateIdClass.builder()
                .fan(userService.getById(fanId))
                .event(eventService.getEventById(eventId))
                .build();
    }

    @Override
    public void rateCompilation(boolean isLike, int fanId, int compId) {
        CompilationRateIdClass idClass = getCompilationRateIdClass(fanId, compId);
        CompilationRate compilationRate = compilationRateRepo.save(CompilationRate.builder()
                .idClass(idClass)
                .isLike(isLike)
                .build());
        log.info("Оценка к подборке событий с id({}) сохранена! {}", compId, compilationRate);
    }

    @Override
    public void updateCompilationRate(boolean isLike, int fanId, int compId) {
        CompilationRate compilationRate = getCompilationRate(fanId, compId);
        compilationRate.setLike(isLike);
        compilationRate = compilationRateRepo.save(compilationRate);
        log.info("Оценка к подборке событий с id({}) изменена! {}", compId, compilationRate);
    }

    @Override
    public void deleteCompilationRate(int fanId, int compId) {
        CompilationRate compilationRate = getCompilationRate(fanId, compId);
        compilationRateRepo.delete(compilationRate);
        log.info("Оценка к подборке событий с id({}) удалена! {}", compId, compilationRate);
    }

    private CompilationRate getCompilationRate(int fanId, int compId) {
        CompilationRateIdClass idClass = getCompilationRateIdClass(fanId, compId);
        return compilationRateRepo.findById(idClass).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Пользователь с id(%d) не оценивал подборку событий с id(%d)!", fanId, compId)));
    }

    private CompilationRateIdClass getCompilationRateIdClass(int fanId, int compId) {
        return CompilationRateIdClass.builder()
                .fan(userService.getById(fanId))
                .compilation(compilationService.getCompilationById(compId))
                .build();
    }

}
