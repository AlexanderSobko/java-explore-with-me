package ru.practicum.ewm.main.service.rate.compilation_rate.model;

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
@Table(name = "compilation_rates")
public class CompilationRate {

    @EmbeddedId
    CompilationRateIdClass idClass;
    boolean isLike;

}
