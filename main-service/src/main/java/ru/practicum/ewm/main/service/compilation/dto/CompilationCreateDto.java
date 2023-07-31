package ru.practicum.ewm.main.service.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationCreateDto {

    List<Integer> events;
    @NotBlank
    @Length(max = 50)
    String title;
    boolean pinned;

}
