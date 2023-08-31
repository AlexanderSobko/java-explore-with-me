package ru.practicum.ewm.main.service.event.service;

import ru.practicum.ewm.main.service.event.model.Event;

import java.util.List;

public interface EventFindService {

    List<Event> findAllById(List<Integer> events);

}
