package ru.practicum.ewm.main.service.rate.compilation_rate.model;

import lombok.*;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
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
public class CompilationRateIdClass implements Serializable {

    @ManyToOne
    @JoinColumn(name = "fan_id", nullable = false)
    private User fan;
    @ManyToOne
    @JoinColumn(name = "compilation_id", nullable = false)
    private Compilation compilation;

}
