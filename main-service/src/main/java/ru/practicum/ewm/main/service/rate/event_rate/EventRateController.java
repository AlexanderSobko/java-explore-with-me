package ru.practicum.ewm.main.service.rate.event_rate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.rate.event_rate.service.EventRateService;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rates/{fanId}/event/{eventId}")
public class EventRateController {

    private final EventRateService service;

    @PostMapping
    public ResponseEntity<Void> rateEvent(@PathVariable @Positive int fanId,
                                          @PathVariable @Positive int eventId,
                                          @RequestParam boolean isLike) {
        service.rateEvent(isLike, fanId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateEventRate(@PathVariable @Positive int fanId,
                                                @PathVariable @Positive int eventId,
                                                @RequestParam boolean isLike) {
        service.updateEventRate(isLike, fanId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEventRate(@PathVariable @Positive int fanId,
                                                @PathVariable @Positive int eventId) {
        service.deleteEventRate(fanId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
