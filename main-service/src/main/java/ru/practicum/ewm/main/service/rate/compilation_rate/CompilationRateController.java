package ru.practicum.ewm.main.service.rate.compilation_rate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.rate.compilation_rate.service.CompilationRateService;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rates/{fanId}/compilation/{compId}")
public class CompilationRateController {

    private final CompilationRateService service;

    @PostMapping
    public ResponseEntity<Void> rateCompilation(@PathVariable @Positive int fanId,
                                                @PathVariable @Positive int compId,
                                                @RequestParam boolean isLike) {
        service.rateCompilation(isLike, fanId, compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateCompilationRate(@PathVariable @Positive int fanId,
                                                      @PathVariable @Positive int compId,
                                                      @RequestParam boolean isLike) {
        service.updateCompilationRate(isLike, fanId, compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCompilationRate(@PathVariable @Positive int fanId,
                                                      @PathVariable @Positive int compId) {
        service.deleteCompilationRate(fanId, compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
