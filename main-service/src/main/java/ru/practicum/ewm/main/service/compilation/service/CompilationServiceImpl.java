package ru.practicum.ewm.main.service.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.compilation.CompilationRepository;
import ru.practicum.ewm.main.service.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.service.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.event.EventRepository;
import ru.practicum.ewm.main.service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository repo;
    EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return from;
            }
        };
        return repo.findAllByPinned(pinned, pageable).stream()
                .map(CompilationDto::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(int compId) {
        return repo.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборка с id(%d) не найдена или недоступна", compId)));
    }

    @Override
    public CompilationDto saveCompilation(CompilationCreateDto dto) {
        Compilation compilation = Compilation.mapToCompilation(dto);
        compilation.setEvents(dto.getEvents() == null ?
                new ArrayList<>() : eventRepository.findAllById(dto.getEvents()));
        compilation = repo.save(compilation);
        log.info("Подборка добавлена! {}", compilation);
        return CompilationDto.mapToCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(int compId) {
        Compilation compilation = getCompilationById(compId);
        repo.delete(compilation);
        log.info("Подборка удалена! {}", compilation);
    }

    @Override
    public CompilationDto updateCompilation(int compId, CompilationUpdateDto dto) {
        Compilation compilation = getCompilationById(compId);
        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));
        }
        compilation = repo.save(compilation);
        log.info("Подборка обновлена! {}", compilation);
        return CompilationDto.mapToCompilationDto(compilation);
    }

}
