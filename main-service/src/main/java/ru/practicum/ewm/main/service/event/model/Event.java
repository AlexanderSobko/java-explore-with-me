package ru.practicum.ewm.main.service.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.event.dto.EventCreateDto;
import ru.practicum.ewm.main.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    String title;
    @Column(nullable = false, columnDefinition = "varchar")
    String annotation;
    @Column(nullable = false, columnDefinition = "varchar")
    String description;
    @Column(nullable = false)
    LocalDateTime eventDate;
    @Column(nullable = false)
    LocalDateTime createdOn;
    LocalDateTime publishedOn;
    @Column(nullable = false)
    boolean paid;
    @Column(nullable = false)
    boolean requestModeration;
    @Column(nullable = false)
    int participantLimit;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    EventState state;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    Location location;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    User initiator;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @Transient
    long views;
    @Transient
    int confirmedRequests;

    public static Event mapToEvent(EventCreateDto dto) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .annotation(dto.getAnnotation())
                .eventDate(dto.getEventDate())
                .location(Location.mapToLocation(dto.getLocation()))
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.isRequestModeration())
                .paid(dto.isPaid())
                .build();
    }

}
