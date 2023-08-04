package ru.practicum.ewm.main.service.rate.event_rate;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "event_rates")
public class EventRate {

    @EmbeddedId
    EventRateIdClass idClass;
    boolean isLike;

}
