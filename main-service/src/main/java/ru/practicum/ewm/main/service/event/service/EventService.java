package ru.practicum.ewm.main.service.event.service;

import ru.practicum.ewm.main.service.event.dto.*;
import ru.practicum.ewm.main.service.event.model.Event;

import java.util.List;

public interface EventService {

    List<EventSimpleDto> getAllUserEvents(int userId, int from, int size);

    EventDto save(int userId, EventCreateDto dto);

    Event getByInitiatorAndId(int userId, int eventId);

    EventDto updateEvent(int userId, int eventId, EventUpdateDto updateRequest);

    List<EventDto> getAllEventsAdmin(GetEventsAdminDto params);

    EventDto updateEventAdmin(int eventId, EventUpdateDto dto);

    List<EventSimpleDto> getAllPublishedEvents(GetEventsDto params);

    Event getEventById(int id);

    EventDto getPublishedEventById(int eventId);

    List<Event> findAllById(List<Integer> events);

    List<Event> setRates(List<Event> events);

}
