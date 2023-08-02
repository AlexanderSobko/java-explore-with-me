package ru.practicum.ewm.main.service.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByName(String name);

}
