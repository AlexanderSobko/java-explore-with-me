package ru.practicum.ewm.main.service.rate.user_rate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.rate.user_rate.service.UserRateService;

import javax.validation.constraints.Positive;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rates/{fanId}/user/{userId}")
public class UserRateController {

    private final UserRateService service;

    @PostMapping
    public ResponseEntity<Void> rateUser(@PathVariable @Positive int fanId,
                                         @PathVariable @Positive int userId,
                                         @RequestParam boolean isLike) {
        service.rateUser(isLike, fanId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateUserRate(@PathVariable @Positive int fanId,
                                               @PathVariable @Positive int userId,
                                               @RequestParam boolean isLike) {
        service.updateUserRate(isLike, fanId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserRate(@PathVariable @Positive int fanId,
                                               @PathVariable @Positive int userId) {
        service.deleteUserRate(fanId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
