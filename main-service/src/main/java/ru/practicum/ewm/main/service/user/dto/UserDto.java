package ru.practicum.ewm.main.service.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.service.rate.dto.RateDto;
import ru.practicum.ewm.main.service.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    int id;
    @NotBlank
    @Length(min = 2, max = 250)
    String name;
    @Email
    @NotBlank
    @Length(min = 6, max = 254)
    String email;
    RateDto rate;
    Long privateRating;

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .rate(user.getRate())
                .privateRating(user.getPrivateRating())
                .build();
    }

}
