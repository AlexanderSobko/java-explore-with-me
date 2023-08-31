package ru.practicum.ewm.main.service.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

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
    long likes;
    long dislikes;
    long rating;
    Long privateRating;

}
