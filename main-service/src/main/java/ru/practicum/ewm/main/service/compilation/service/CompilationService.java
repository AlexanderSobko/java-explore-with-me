package ru.practicum.ewm.main.service.compilation.service;

import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAllCompilations(boolean pinned, int from, int size);
    Compilation getCompilationById(int compId);
    CompilationDto saveCompilation(CompilationCreateDto dto);
    void deleteCompilation(int compId);
    CompilationDto updateCompilation(int compId, CompilationUpdateDto dto);

}
