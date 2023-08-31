package ru.practicum.ewm.main.service.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {

    @NotNull
    Float lon;
    @NotNull
    Float lat;

}
