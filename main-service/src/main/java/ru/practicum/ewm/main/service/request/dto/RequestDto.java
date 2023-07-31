package ru.practicum.ewm.main.service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.service.request.model.Request;

import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime created;
    int event;
    int id;
    int requester;
    @Length(max = 16)
    String status;

    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getRequestStatus().toString())
                .build();
    }

}
