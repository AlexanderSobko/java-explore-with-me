package ru.practicum.ewm.main.service.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSimpleDto {

    int id;
    String name;
    long likes;
    long dislikes;
    long rating;

}
