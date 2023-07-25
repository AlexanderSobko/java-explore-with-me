package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.repository.StatsParamDto;

import java.util.List;

public interface StatService {

    String saveLog(RequestLogDto dto);

    List<StatsDto> getStatistics(StatsParamDto dto);

}
