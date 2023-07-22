package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.RequestLogDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.RequestLogDto.DATE_FORMAT;

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
    public ResponseEntity<List<StatsDto>> getStatistics(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(value = "unique", required = false) boolean isUnique) {
        return ResponseEntity.ok(statService.getStatistics(start, end, uris, isUnique));
    }
}
