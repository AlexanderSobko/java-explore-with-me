package ru.practicum.ewm.main.service.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.service.event.model.EventState;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByIdAndState(int eventId, EventState state);

    int countByCategory(Category category);

    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findByInitiatorAndId(User user, int eventId);

}
