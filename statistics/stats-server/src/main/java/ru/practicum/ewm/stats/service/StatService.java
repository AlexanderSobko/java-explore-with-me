package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.repository.RequestLog;
import ru.practicum.ewm.stats.repository.RequestLogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatService {

    private final RequestLogRepository statsRepo;

    public String saveLog(RequestLogDto dto) {
        RequestLog entity = RequestLog.mapToLog(dto);
        entity.setTimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statsRepo.save(entity);
        String message = "Информация сохранена! ";
        log.info(message + entity);
        return message;
    }

    public List<StatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique) {
        uris = uris != null && uris.isEmpty() ? null : uris;
        log.debug("GetStats запрос с параметрами: start - {}, end - {}, uris - {}, isUnique - {}.", start, end, uris, isUnique);
        return statsRepo.getStats(start, end, uris, isUnique);
    }

}
