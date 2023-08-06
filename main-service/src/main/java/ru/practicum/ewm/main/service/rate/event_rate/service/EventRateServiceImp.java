package ru.practicum.ewm.main.service.rate.event_rate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.event_rate.EventRateRepository;
import ru.practicum.ewm.main.service.rate.event_rate.model.EventRate;
import ru.practicum.ewm.main.service.rate.event_rate.model.EventRateIdClass;
import ru.practicum.ewm.main.service.request.service.RequestService;
import ru.practicum.ewm.main.service.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.practicum.ewm.main.service.request.model.RequestStatus.CONFIRMED;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventRateServiceImp implements EventRateService {

    EventRateRepository eventRateRepo;
    RequestService requestService;
    EventService eventService;
    UserService userService;

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

    @Override
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

}
