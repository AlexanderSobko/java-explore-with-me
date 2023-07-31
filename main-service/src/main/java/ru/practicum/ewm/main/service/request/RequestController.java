package ru.practicum.ewm.main.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.request.dto.RequestStatusUpdateDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusResponseDto;
import ru.practicum.ewm.main.service.request.dto.RequestDto;
import ru.practicum.ewm.main.service.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService service;

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getAllUserRequests(@PathVariable @Positive int userId) {
        return ResponseEntity.ok(service.getAllUserRequests(userId));
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<RequestDto> saveRequest(@PathVariable @Positive int userId,
                                                  @RequestParam @Positive int eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userId, eventId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable @Positive int userId,
                                                    @PathVariable @Positive int requestId) {
        return ResponseEntity.ok(service.cancelRequest(userId, requestId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestsToUserEvent(@PathVariable @Positive int userId,
                                                                      @PathVariable @Positive int eventId) {
        return ResponseEntity.ok(service.getAllRequestsToUserEvent(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestStatusResponseDto> updateRequestsByInitiator(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId,
            @RequestBody @Valid RequestStatusUpdateDto dto) {
        return ResponseEntity.ok(service.updateRequestsByInitiator(userId, eventId, dto));
    }

}
