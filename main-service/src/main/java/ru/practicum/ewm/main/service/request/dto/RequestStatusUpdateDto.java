package ru.practicum.ewm.main.service.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdateDto {

    @NotNull
    List<Integer> requestIds;
    @NotNull
    String status;

}
