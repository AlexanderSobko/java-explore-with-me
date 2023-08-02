package ru.practicum.ewm.main.service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.EventRequestCount;
import ru.practicum.ewm.main.service.request.model.Request;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("SELECT new ru.practicum.ewm.main.service.event.model.EventRequestCount(r.event.id, COUNT(r)) FROM Request r " +
            "WHERE r.event IN(:events) AND r.requestStatus = 'CONFIRMED' GROUP BY r.event.id")
    List<EventRequestCount> countRequestByEventIn(@Param("events") List<Event> events);

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEventAndIdIn(Event event, List<Integer> ids);

    boolean existsByRequesterAndEvent(User requester, Event event);

    Optional<Request> findByIdAndRequester(int requestId, User user);

}
