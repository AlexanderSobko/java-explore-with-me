package ru.practicum.ewm.main.service.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.category.service.CategoryService;
import ru.practicum.ewm.main.service.event.EventRepository;
import ru.practicum.ewm.main.service.event.LocationRepository;
import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.EventRequestCount;
import ru.practicum.ewm.main.service.event.model.Location;
import ru.practicum.ewm.main.service.exception.AlreadyExistsException;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.request.RequestRepository;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.user.service.UserService;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.StatsDto;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;
import static ru.practicum.ewm.main.service.event.dto.EventUpdateDto.StateAction.*;
import static ru.practicum.ewm.main.service.event.mapper.EventMapper.mapper;
import static ru.practicum.ewm.main.service.event.model.EventState.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {

    EventRepository repo;
    UserService userService;
    CategoryService categoryService;
    LocationRepository locationRepo;
    RequestRepository requestRepo;
    StatsClient client;

    @Override
    public List<EventSimpleDto> getAllUserEvents(int userId, int from, int size) {
        Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return from;
            }
        };
        User initiator = userService.getById(userId);
        List<Event> events = repo.findAllByInitiator(initiator, pageable);
        return setConfirmedRequestsAndViews(events).stream()
                .map(EventSimpleDto::mapToEventSimpleDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto save(int userId, EventCreateDto dto) {
        User initiator = userService.getById(userId);
        Category category = categoryService.getById(dto.getCategory());
        Location location = Location.mapToLocation(dto.getLocation());
        location = locationRepo.findByLatAndLon(location.getLat(), location.getLon())
                .orElse(locationRepo.save(location));
        Event event = Event.mapToEvent(dto);

        event.setCreatedOn(LocalDateTime.now().truncatedTo(SECONDS));
        event.setState(PENDING);
        event.setLocation(location);
        event.setInitiator(initiator);
        event.setCategory(category);

        event = repo.save(event);
        log.info("Событие добавлено! {}", event);
        return EventDto.mapToEventDto(event);
    }

    @Override
    public Event getByInitiatorAndId(int userId, int eventId) {
        User user = userService.getById(userId);
        Event event = repo.findByInitiatorAndId(user, eventId).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id(%d) не найдено или недоступно!", eventId)));
        event = setConfirmedRequestsAndViews(List.of(event)).get(0);
        return event;
    }

    @Override
    public EventDto updateEvent(int userId, int eventId, EventUpdateDto dto) {
        Event event = getByInitiatorAndId(userId, eventId);
        validateEventIsPublished(event);
        if (CANCEL_REVIEW.equals(dto.getStateAction())) {
            event.setState(CANCELED);
        } else if (SEND_TO_REVIEW.equals(dto.getStateAction())) {
            event.setState(PENDING);
        }
        event = repo.save(mapper.updateEventFromDto(dto, event));
        log.info("Пользователь обновил событие! {}", event);
        return EventDto.mapToEventDto(setConfirmedRequestsAndViews(List.of(event)).get(0));
    }

    @Override
    public List<EventDto> getAllEventsAdmin(GetEventsAdminDto params) {
        Pageable pageable = new PageRequest(0, params.getSize(), Sort.by(Sort.Order.asc("publishedOn"))) {
            @Override
            public long getOffset() {
                return params.getFrom();
            }
        };
        return setConfirmedRequestsAndViews(repo.findAll(getSpecAllEventsAdmin(params), pageable).getContent()).stream()
                .map(EventDto::mapToEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEventAdmin(int eventId, EventUpdateDto dto) {
        Event event = getById(eventId);
        if (dto.getStateAction() != null) {
            validateEventIsPublished(event);
            validateEventIsCanceled(event);
            if (PUBLISH_EVENT.equals(dto.getStateAction())) {
                event.setState(PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().truncatedTo(SECONDS));
            } else if (REJECT_EVENT.equals(dto.getStateAction())) {
                event.setState(CANCELED);
            }
        }
        if (dto.getCategory() != null && !event.getCategory().getId().equals(dto.getCategory())) {
            event.setCategory(categoryService.getById(dto.getCategory()));
        }
        event = repo.save(mapper.updateEventFromDto(dto, event));
        log.info("Событие обновлено админом! {}", event);
        return EventDto.mapToEventDto(setConfirmedRequestsAndViews(List.of(event)).get(0));
    }

    @Override
    public List<EventSimpleDto> getAllPublishedEvents(GetEventsDto params) {
        Comparator<EventSimpleDto> comparator;
        validateCategories(params);
        if (GetEventsDto.SortParam.VIEWS.equals(params.getSort())) {
            comparator = Comparator.comparingLong(EventSimpleDto::getViews);
        } else if (GetEventsDto.SortParam.EVENT_DATE.equals(params.getSort())) {
            comparator = Comparator.comparing(EventSimpleDto::getEventDate);
        } else {
            comparator = Comparator.comparingInt(EventSimpleDto::getId);
        }
        Pageable pageable = new PageRequest(0, params.getSize(), Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return params.getFrom();
            }
        };
        List<Event> events = setConfirmedRequestsAndViews(repo.findAll(getSpecAllEvents(params), pageable).getContent());
        if (params.isOnlyAvailable()) {
            events = events.stream()
                    .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(EventSimpleDto::mapToEventSimpleDto)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Event getEventById(int id) {
        Event event = repo.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id(%d) не найдено!", id)));
        event = setConfirmedRequestsAndViews(List.of(event)).get(0);
        return event;
    }

    @Override
    public EventDto getPublishedEventById(int eventId) {
        Event event = repo.findByIdAndState(eventId, PUBLISHED).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id(%d) не найдено или недоступно!", eventId)));
        event = setConfirmedRequestsAndViews(List.of(event)).get(0);
        return EventDto.mapToEventDto(event);
    }

    private void validateCategories(GetEventsDto params) {
        if (params.getCategories().stream().anyMatch(category -> category <= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Значение id категорий должно быть положительным!");
        }
    }

    private void validateEventIsPublished(Event event) {
        if (event.getState().equals(PUBLISHED)) {
            throw new AlreadyExistsException(String.format("Событие с id(%d) уже опубликовано!", event.getId()));
        }
    }

    private void validateEventIsCanceled(Event event) {
        if (CANCELED.equals(event.getState())) {
            throw new AlreadyExistsException(String.format("Событие с id(%d) уже отклонено!", event.getId()));
        }
    }

    private Specification<Event> getSpecAllEventsAdmin(GetEventsAdminDto params) {
        System.out.println(params);
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.getUsers() != null && !params.getUsers().isEmpty()) {
                Join<Event, User> userJoin = root.join("initiator");
                predicates.add(userJoin.get("id").in(params.getUsers()));
            }
            if (params.getStates() != null && params.getStates().isEmpty()) {
                Predicate inStates = root.get("state").in(params.getStates());
                predicates.add(inStates);
            }
            if (params.getCategories() != null && params.getCategories().isEmpty()) {
                Join<Event, Category> categoryJoin = root.join("category");
                predicates.add(categoryJoin.get("id").in(params.getCategories()));
            }
            if (params.getRangeStart() != null) {
                Predicate greaterThanRangeStart = cb.greaterThan(root.get("eventDate"), params.getRangeStart());
                predicates.add(greaterThanRangeStart);
            }
            if (params.getRangeEnd() != null) {
                Predicate lessThanRangeEnd = cb.lessThan(root.get("eventDate"), params.getRangeEnd());
                predicates.add(lessThanRangeEnd);
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<Event> getSpecAllEvents(GetEventsDto dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dto.getText() != null && !dto.getText().isBlank()) {
                Predicate likeAnnotation = cb.equal(root.get("annotation"), dto.getText());
                Predicate likeDescription = cb.like(root.get("description"), dto.getText());
                predicates.add(cb.or(likeAnnotation, likeDescription));
            }
            if (dto.getCategories() != null && !dto.getCategories().isEmpty()) {
                Join<Event, Category> categoryJoin = root.join("category");
                predicates.add(categoryJoin.get("id").in(dto.getCategories()));
            }
            if (dto.getPaid() != null) {
                Predicate isPaid = cb.equal(root.get("paid"), dto.getPaid());
                predicates.add(isPaid);
            }
            LocalDateTime rangeStart = dto.getRangeStart() == null ? LocalDateTime.now() : dto.getRangeStart();
            Predicate greaterThanRangeStart = cb.greaterThan(root.get("eventDate"), rangeStart);
            predicates.add(greaterThanRangeStart);
            if (dto.getRangeEnd() != null) {
                Predicate lessThanRangeEnd = cb.lessThan(root.get("eventDate"), dto.getRangeEnd());
                predicates.add(lessThanRangeEnd);
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public Event getById(int eventId) {
        return repo.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Событие с id(%d) не найдено!", eventId)));
    }

    private List<Event> setConfirmedRequestsAndViews(List<Event> events) {
        Map<Integer, Long> idsAndConfirmedRequests = requestRepo.countRequestByEventIn(events).stream()
                .collect(Collectors.toMap(EventRequestCount::getId, EventRequestCount::getCount));
        Map<Integer, Long> idsAndViews = getViews(events);
        events.forEach(event -> {
            long count = idsAndConfirmedRequests.getOrDefault(event.getId(), 0L);
            event.setConfirmedRequests((int) count);
            event.setViews(idsAndViews.getOrDefault(event.getId(), 0L));
        });
        return events;
    }

    private Map<Integer, Long> getViews(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%d", event.getId())).collect(Collectors.toList());
        List<StatsDto> stats = client.getStats(
                LocalDateTime.now().truncatedTo(SECONDS).minusYears(100),
                LocalDateTime.now().truncatedTo(SECONDS).plusYears(100),
                uris, true);
        Function<StatsDto, Integer> getId = dto -> Integer.parseInt(dto.getUri().replace("/events/", ""));
        return stats.stream().collect(Collectors.toMap(getId, StatsDto::getHits));
    }

}
