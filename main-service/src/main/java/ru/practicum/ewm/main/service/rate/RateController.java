package ru.practicum.ewm.main.service.rate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.rate.service.RateService;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rates/{fanId}")
public class RateController {

    private final RateService service;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> rateUser(@PathVariable @Positive int fanId,
                                         @PathVariable @Positive int userId,
                                         @RequestParam boolean isLike) {
        service.rateUser(isLike, fanId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<Void> updateUserRate(@PathVariable @Positive int fanId,
                                               @PathVariable @Positive int userId,
                                               @RequestParam boolean isLike) {
        service.updateUserRate(isLike, fanId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserRate(@PathVariable @Positive int fanId,
                                               @PathVariable @Positive int userId) {
        service.deleteUserRate(fanId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Void> rateEvent(@PathVariable @Positive int fanId,
                                          @PathVariable @Positive int eventId,
                                          @RequestParam boolean isLike) {
        service.rateEvent(isLike, fanId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/event/{eventId}")
    public ResponseEntity<Void> updateEventRate(@PathVariable @Positive int fanId,
                                                @PathVariable @Positive int eventId,
                                                @RequestParam boolean isLike) {
        service.updateEventRate(isLike, fanId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/event/{eventId}")
    public ResponseEntity<Void> deleteEventRate(@PathVariable @Positive int fanId,
                                                @PathVariable @Positive int eventId) {
        service.deleteEventRate(fanId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
