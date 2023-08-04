package ru.practicum.ewm.main.service.rate.user_rate;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_rates")
public class UserRate {

    @EmbeddedId
    UserRateIdClass idClass;
    boolean isLike;

}
