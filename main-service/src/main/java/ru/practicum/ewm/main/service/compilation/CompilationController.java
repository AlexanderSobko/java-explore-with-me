package ru.practicum.ewm.main.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDto>> getAllCompilations(
            @RequestParam(defaultValue = "false") boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok(compilationService.getAllCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable @Positive int compId) {
        return ResponseEntity.ok(CompilationDto.mapToCompilationDto(compilationService.getCompilationById(compId)));
    }

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid CompilationCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.saveCompilation(dto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable @Positive int compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable @Positive int compId,
                                                            @RequestBody @Valid CompilationUpdateDto dto) {
        return ResponseEntity.ok().body(compilationService.updateCompilation(compId, dto));
    }

}
