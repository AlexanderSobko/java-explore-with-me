package ru.practicum.ewm.main.service.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.service.event.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByLatAndLon(float lat, float lon);

}
