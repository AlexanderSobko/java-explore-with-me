package ru.practicum.ewm.main.service.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.event.dto.LocationDto;

import javax.persistence.*;
import java.io.Serializable;

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

    public static Location mapToLocation(LocationDto dto) {
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

}

