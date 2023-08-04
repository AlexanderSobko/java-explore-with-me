package ru.practicum.ewm.main.service.rate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {

    @JsonIgnore
    int id;
    long likes;
    long dislikes;

    public long getRating() {
        return likes - dislikes;
    }

    public static Map<Integer, RateDto> mapDbResultToIdAndRateDtoMap(List<RateDto> dbResult) {
        return dbResult.stream()
                .collect(Collectors.toMap(RateDto::getId, rate -> rate));
    }

}
