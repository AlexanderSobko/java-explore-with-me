package ru.practicum.ewm.main.service.rate.event_rate.service;

public interface EventRateService {

    void rateEvent(boolean isLike, int fanId, int eventId);

    void updateEventRate(boolean isLike, int fanId, int eventId);

    void deleteEventRate(int fanId, int eventId);

}
