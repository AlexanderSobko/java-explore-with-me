package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.repository.RequestLog;
import ru.practicum.ewm.stats.repository.RequestLogRepository;
import ru.practicum.ewm.stats.repository.StatsParamDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final RequestLogRepository statsRepo;

    @Override
    public String saveLog(RequestLogDto dto) {
        RequestLog entity = RequestLog.mapToLog(dto);
        entity.setTimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statsRepo.save(entity);
        String message = "Информация сохранена! ";
        log.info(message + entity);
        return message;
    }

    @Override
    public List<StatsDto> getStatistics(StatsParamDto dto) {
        List<String> uriList = dto.getUris();
        if (dto.getStart().isAfter(dto.getEnd())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата начало не может быть после конца)");
        }
        dto.setUris(uriList != null && uriList.isEmpty() ? null : uriList);
        log.debug("GetStats запрос с параметрами: start - {}, end - {}, uris - {}, isUnique - {}.",
                dto.getStart(), dto.getEnd(), dto.getUris(), dto.isUnique());
        return statsRepo.getStats(dto.getStart(), dto.getEnd(), dto.getUris(), dto.isUnique());
    }

}
