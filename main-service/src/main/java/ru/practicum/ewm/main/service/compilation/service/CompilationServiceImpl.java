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
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.service.EventFindService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRateRepository;
import ru.practicum.ewm.main.service.rate.dto.Rate;
import ru.practicum.ewm.main.service.rate.event_rate.EventRateRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.main.service.compilation.mapper.CompilationMapper.COMPILATION_MAPPER;
import static ru.practicum.ewm.main.service.event.dto.SortParam.RATING;
import static ru.practicum.ewm.main.service.rate.dto.Rate.mapDbResultToIdAndRateDtoMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationServiceImpl implements CompilationService {

    EventFindService eventFindService;
    CompilationRepository repo;
    CompilationRateRepository compilationRateRepo;
    EventRateRepository eventRateRepo;

    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size, SortParam sortParam) {
        Pageable pageable = new PageRequest(0, size, Sort.by(Sort.Order.asc("id"))) {
            @Override
            public long getOffset() {
                return from;
            }
        };
        Comparator<CompilationDto> comparator = RATING.equals(sortParam) ?
                Comparator.comparingLong(CompilationDto::getRating).reversed()
                : Comparator.comparingLong(CompilationDto::getId);
        List<Compilation> compilations = setRates(repo.findAllByPinned(pinned, pageable));
        return compilations.stream()
                .map(COMPILATION_MAPPER::mapToCompilationDto)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(int compId) {
        Compilation compilation = repo.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Подборка с id(%d) не найдена или недоступна", compId)));
        compilation = setRates(List.of(repo.save(compilation))).get(0);
        return compilation;
    }

    @Override
    public CompilationDto saveCompilation(CompilationCreateDto dto) {
        Compilation compilation = Compilation.mapToCompilation(dto);
        compilation.setEvents(dto.getEvents() == null ?
                new ArrayList<>() : eventFindService.findAllById(dto.getEvents()));
        compilation = repo.save(compilation);
        log.info("Подборка добавлена! {}", compilation);
        return COMPILATION_MAPPER.mapToCompilationDto(compilation);
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
            compilation.setEvents(eventFindService.findAllById(dto.getEvents()));
        }
        compilation = setRates(List.of(repo.save(compilation))).get(0);
        log.info("Подборка обновлена! {}", compilation);
        return COMPILATION_MAPPER.mapToCompilationDto(compilation);
    }

    private List<Compilation> setRates(List<Compilation> compilations) {
        Map<Integer, Rate> rates = mapDbResultToIdAndRateDtoMap(
                compilationRateRepo.getRatesByCompilationsIn(compilations));
        List<Event> events = compilations.stream()
                .flatMap(compilation -> compilation.getEvents().stream())
                .collect(Collectors.toList());
        Map<Integer, Rate> eventIdsAndRating = mapDbResultToIdAndRateDtoMap(
                eventRateRepo.getRatesByEventsIn(events));
        compilations.forEach(compilation -> {
            Rate rate = rates.getOrDefault(compilation.getId(), new Rate());
            compilation.setLikes(rate.getLikes());
            compilation.setDislikes(rate.getDislikes());
            compilation.getEvents().forEach(event -> {
                Rate eventRate = eventIdsAndRating.getOrDefault(event.getId(), new Rate());
                event.setLikes(eventRate.getLikes());
                event.setDislikes(eventRate.getDislikes());
            });
        });

        return compilations;
    }

}
