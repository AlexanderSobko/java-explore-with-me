package ru.practicum.ewm.main.service.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {

    List<Integer> events;
    @Length(max = 50)
    String title;
    Boolean pinned;

}
