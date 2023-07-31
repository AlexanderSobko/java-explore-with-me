package ru.practicum.ewm.main.service.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusResponseDto {

    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;

}
