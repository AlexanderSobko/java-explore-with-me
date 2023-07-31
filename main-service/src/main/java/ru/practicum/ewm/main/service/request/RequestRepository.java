package ru.practicum.ewm.main.service.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.request.model.Request;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("SELECT r.event.id, COUNT(r) FROM Request r " +
            "WHERE r.event IN(:events) AND r.requestStatus = 'CONFIRMED' GROUP BY r.event.id")
    List<Object[]> countRequestByEventIn(@Param("events") List<Event> events);

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEventAndIdIn(Event event, List<Integer> ids);

    boolean existsByRequesterAndEvent(User requester, Event event);

    Optional<Request> findByIdAndRequester(int requestId, User user);

}
