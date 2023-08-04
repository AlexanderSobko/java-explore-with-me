package ru.practicum.ewm.main.service.rate.event_rate;

import lombok.*;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.user.model.User;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EventRateIdClass implements Serializable {

    @ManyToOne
    @JoinColumn(name = "fan_id", nullable = false)
    private User fan;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
