package ru.practicum.ewm.main.service.rate.user_rate.service;

public interface UserRateService {

    void rateUser(boolean isLike, int fanId, int userId);

    void updateUserRate(boolean isLike, int fanId, int userId);

    void deleteUserRate(int fanId, int userId);

}
