package ru.practicum.ewm.main.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.stats.client.StatsClient;
import ru.practicum.ewm.stats.dto.RequestLogDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.ewm.main.service.event.mapper.EventMapper.EVENT_MAPPER;

@Validated
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService service;
    private final StatsClient client;

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventSimpleDto>> getAllUserEvents(
            @PathVariable @Positive int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(service.getAllUserEvents(userId, from, size));
    }

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventDto> saveEvent(@PathVariable @Positive int userId,
                                              @RequestBody @Valid EventCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userId, dto));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getEventByIdAndUser(@PathVariable @Positive int userId,
                                                        @PathVariable @Positive int eventId) {
        return ResponseEntity.ok(EVENT_MAPPER.mapToEventDto(service.getByInitiatorAndId(userId, eventId)));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable @Positive int userId,
                                                @PathVariable @Positive int eventId,
                                                @RequestBody @Valid EventUpdateDto dto) {
        return ResponseEntity.ok(service.updateEvent(userId, eventId, dto));
    }

    @GetMapping("/admin/events")
    public ResponseEntity<List<EventDto>> getAllEventsAdmin(GetEventsAdminDto params) {
        return ResponseEntity.ok(service.getAllEventsAdmin(params));
    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventDto> updateEventAdmin(@PathVariable @Positive int eventId,
                                                     @RequestBody @Valid EventUpdateDto dto) {
        return ResponseEntity.ok().body(service.updateEventAdmin(eventId, dto));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventSimpleDto>> getAllPublishedEvents(GetEventsDto params, HttpServletRequest request) {
        logRequest(request);
        return ResponseEntity.ok(service.getAllPublishedEvents(params));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDto> getPublishedEventById(@PathVariable @Positive int eventId, HttpServletRequest request) {
        logRequest(request);
        return ResponseEntity.ok(service.getPublishedEventById(eventId));
    }

    private void logRequest(HttpServletRequest request) {
        client.saveRequestLog(RequestLogDto.builder()
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .app("main-service")
                .build());
    }

}
