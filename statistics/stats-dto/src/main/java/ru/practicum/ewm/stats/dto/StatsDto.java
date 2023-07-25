package ru.practicum.ewm.stats.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {

    String app;
    String uri;
    long hits;

}
