package ru.practicum.ewm.main.service.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.category.CategoryRepository;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.model.Category;
import ru.practicum.ewm.main.service.event.EventRepository;
import ru.practicum.ewm.main.service.exception.AlreadyExistsException;
import ru.practicum.ewm.main.service.exception.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.category.mapper.CategoryMapper.CATEGORY_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    private final EventRepository eventRepo;

    @Override
    public CategoryDto save(CategoryDto dto) {
        validateName(dto.getName());
        Category category = Category.mapToCategory(dto);
        category = repo.save(category);
        log.info("Категория добавлена! {}", category);
        return CATEGORY_MAPPER.mapToCategoryDto(category);
    }

    @Override
    public void deleteById(int id) {
        Category category = getById(id);
        if (eventRepo.countByCategory(category) != 0) {
            throw new AlreadyExistsException("Категория с id(%d) уже связана с событием!");
        }
        repo.delete(category);
        log.info("Категория удалена! {}", category);
    }

    @Override
    public CategoryDto update(CategoryDto dto) {
        Category category = getById(dto.getId());
        if (!category.getName().equals(dto.getName())) {
            validateName(dto.getName());
            category.setName(dto.getName());
            category = repo.save(category);
            log.info("Данные категории изменены! {}", category);
        }
        return CATEGORY_MAPPER.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.desc("id"))) {
            @Override
            public long getOffset() {
                return from;
            }
        };
        return repo.findAll(pageable).stream()
                .map(CATEGORY_MAPPER::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Category getById(int id) {
        return repo.findById(id).orElseThrow(() ->
                new NotFoundException("Категория с id(%d) не найдена!"));
    }

    private void validateName(String name) {
        if (repo.existsByName(name)) {
            throw new AlreadyExistsException("Категория с таким именем уже существует!");
        }
    }

}
