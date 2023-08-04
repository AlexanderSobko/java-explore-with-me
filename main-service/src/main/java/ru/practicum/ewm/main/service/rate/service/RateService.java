package ru.practicum.ewm.main.service.rate.service;

public interface RateService {

    void rateUser(boolean isLike, int fanId, int userId);

    void deleteUserRate(int fanId, int userId);

    void rateEvent(boolean isLike, int fanId, int eventId);

    void updateEventRate(boolean isLike, int fanId, int eventId);

    void deleteEventRate(int fanId, int eventId);

    void rateCompilation(boolean isLike, int fanId, int compId);

    void deleteCompilationRate(int fanId, int compId);

    void updateUserRate(boolean isLike, int fanId, int userId);

    void updateCompilationRate(boolean isLike, int fanId, int compId);

}
