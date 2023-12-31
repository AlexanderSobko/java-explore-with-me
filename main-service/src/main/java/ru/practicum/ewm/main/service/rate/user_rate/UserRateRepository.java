package ru.practicum.ewm.main.service.rate.user_rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.service.rate.dto.Rate;
import ru.practicum.ewm.main.service.rate.user_rate.model.UserRate;
import ru.practicum.ewm.main.service.rate.user_rate.model.UserRateIdClass;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface UserRateRepository extends JpaRepository<UserRate, UserRateIdClass> {

    @Query("SELECT new ru.practicum.ewm.main.service.rate.dto.Rate(" +
            "   ur.idClass.user.id," +
            "   COUNT(CASE WHEN ur.isLike = true THEN 1 END), " +
            "   COUNT(CASE WHEN ur.isLike = false THEN 1 END)) " +
            "FROM UserRate ur " +
            "WHERE ur.idClass.user IN(:users) " +
            "GROUP BY ur.idClass.user.id ")
    List<Rate> getRatesByUserIn(@Param("users") List<User> users);

}
