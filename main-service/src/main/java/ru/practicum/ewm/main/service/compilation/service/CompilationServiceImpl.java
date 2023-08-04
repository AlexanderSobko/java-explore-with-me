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
import ru.practicum.ewm.main.service.event.dto.SortParam;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRateRepository;
import ru.practicum.ewm.main.service.rate.dto.RateDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.event.dto.SortParam.RATING;
import static ru.practicum.ewm.main.service.rate.dto.RateDto.mapDbResultToIdAndRateDtoMap;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

    EventService eventService;
    CompilationRepository repo;
    CompilationRateRepository compilationRateRepo;

    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size, SortParam sortParam) {
        Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return from;
            }
        };
        Comparator<Compilation> comparator = Comparator.comparingLong(Compilation::getRating);
        if (RATING.equals(sortParam)) {
            comparator = Comparator.comparingLong(Compilation::getRating).reversed();
        }
        List<Compilation> compilations = setRates(repo.findAllByPinned(pinned, pageable));
        compilations.forEach(compilation -> compilation.setEvents(eventService.setRates(compilation.getEvents())));
        return compilations.stream()
                .sorted(comparator)
                .map(CompilationDto::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(int compId) {
        Compilation compilation = repo.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборка с id(%d) не найдена или недоступна", compId)));
        compilation = setRates(List.of(repo.save(compilation))).get(0);
        compilation.setEvents(eventService.setRates(compilation.getEvents()));
        return compilation;
    }

    @Override
    public CompilationDto saveCompilation(CompilationCreateDto dto) {
        Compilation compilation = Compilation.mapToCompilation(dto);
        compilation.setEvents(dto.getEvents() == null ?
                new ArrayList<>() : eventService.findAllById(dto.getEvents()));
        compilation = repo.save(compilation);
        compilation.setRate(new RateDto());
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
            compilation.setEvents(eventService.findAllById(dto.getEvents()));
        }
        compilation = setRates(List.of(repo.save(compilation))).get(0);
        log.info("Подборка обновлена! {}", compilation);
        return CompilationDto.mapToCompilationDto(compilation);
    }

    private List<Compilation> setRates(List<Compilation> compilations) {
        Map<Integer, RateDto> rates = mapDbResultToIdAndRateDtoMap(
                compilationRateRepo.getRatesByCompilationsIn(compilations));
        compilations.forEach(compilation ->
                compilation.setRate(rates.getOrDefault(compilation.getId(), new RateDto())));
        return compilations;
    }

}
