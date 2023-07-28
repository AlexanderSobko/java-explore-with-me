package ru.practicum.ewm.main.service.category.service;

import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto dto);

    void deleteById(int id);

    CategoryDto update(CategoryDto dto);

    List<CategoryDto> getAll(int from, int size);

    Category getById(int id);

}
