package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.repository.StatsParamDto;
import ru.practicum.ewm.stats.service.StatService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<String> saveLog(@RequestBody @Valid RequestLogDto dto) {
        return ResponseEntity.status(201).body(statService.saveLog(dto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDto>> getStatistics(@Valid StatsParamDto dto) {
        return ResponseEntity.ok(statService.getStatistics(dto));
    }
}
