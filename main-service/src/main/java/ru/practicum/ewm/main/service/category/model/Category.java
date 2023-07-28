package ru.practicum.ewm.main.service.category.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, nullable = false)
    String name;

    public static Category mapToCategory(CategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

}
