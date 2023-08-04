package ru.practicum.ewm.main.service.rate.user_rate;

import lombok.*;
import ru.practicum.ewm.main.service.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserRateIdClass implements Serializable {

    @ManyToOne
    @JoinColumn(name = "fan_id", nullable = false)
    private User fan;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
