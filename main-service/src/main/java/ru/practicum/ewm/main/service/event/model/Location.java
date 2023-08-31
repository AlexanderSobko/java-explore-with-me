package ru.practicum.ewm.main.service.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    float lat;
    @Column(nullable = false)
    float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(lat, location.lat) == 0 && Float.compare(lon, location.lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}

