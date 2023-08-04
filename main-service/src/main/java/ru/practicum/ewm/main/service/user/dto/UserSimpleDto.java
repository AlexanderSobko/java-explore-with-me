package ru.practicum.ewm.main.service.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.rate.dto.RateDto;
import ru.practicum.ewm.main.service.user.model.User;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSimpleDto {

    int id;
    String name;
    RateDto rate;

    public static UserSimpleDto mapToUserSimpleDto(User user) {
        return UserSimpleDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rate(user.getRate())
                .build();
    }

}
