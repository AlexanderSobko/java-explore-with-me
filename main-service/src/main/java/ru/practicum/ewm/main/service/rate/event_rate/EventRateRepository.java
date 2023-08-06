package ru.practicum.ewm.main.service.rate.event_rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.rate.dto.Rate;
import ru.practicum.ewm.main.service.rate.event_rate.model.EventRate;
import ru.practicum.ewm.main.service.rate.event_rate.model.EventRateIdClass;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface EventRateRepository extends JpaRepository<EventRate, EventRateIdClass> {

    @Query("SELECT new ru.practicum.ewm.main.service.rate.dto.Rate(" +
            "   er.idClass.event.id," +
            "   COUNT(CASE WHEN er.isLike = true THEN 1 END), " +
            "   COUNT(CASE WHEN er.isLike = false THEN 1 END)) " +
            "FROM EventRate er " +
            "WHERE er.idClass.event IN(:events) " +
            "GROUP BY er.idClass.event.id ")
    List<Rate> getRatesByEventsIn(@Param("events") List<Event> events);

    @Query("SELECT new ru.practicum.ewm.main.service.rate.dto.Rate(" +
            "   er.idClass.event.initiator.id," +
            "   COUNT(CASE WHEN er.isLike = true THEN 1 END), " +
            "   COUNT(CASE WHEN er.isLike = false THEN 1 END)) " +
            "FROM EventRate er " +
            "WHERE er.idClass.event.initiator IN(:users) " +
            "GROUP BY er.idClass.event.initiator ")
    List<Rate> getPrivateRatesByUserIn(@Param("users") List<User> users);

}
