package ru.practicum.ewm.main.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.category.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.ewm.main.service.category.mapper.CategoryMapper.CATEGORY_MAPPER;

@Validated
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody @Valid CategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/categories/{categoryId}")
    public void deleteCategory(@PathVariable @Positive int categoryId) {
        service.deleteById(categoryId);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Positive int categoryId,
                                                      @RequestBody @Valid CategoryDto dto) {
        dto.setId(categoryId);
        return ResponseEntity.ok(service.update(dto));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                              @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(service.getAll(from, size));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int categoryId) {
        return ResponseEntity.ok(CATEGORY_MAPPER.mapToCategoryDto(service.getById(categoryId)));
    }

}
