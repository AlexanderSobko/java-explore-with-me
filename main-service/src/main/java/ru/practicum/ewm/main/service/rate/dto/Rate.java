package ru.practicum.ewm.main.service.rate.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rate implements Serializable {

    int id;
    long likes;
    long dislikes;

    public static Map<Integer, Rate> mapDbResultToIdAndRateDtoMap(List<Rate> dbResult) {
        return dbResult.stream()
                .collect(Collectors.toMap(Rate::getId, rate -> rate));
    }

}
