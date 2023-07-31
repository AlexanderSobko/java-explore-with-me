package ru.practicum.ewm.main.service.request.model;

import lombok.*;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.user.model.User;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
    @Column(nullable = false)
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User requester;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;

}
