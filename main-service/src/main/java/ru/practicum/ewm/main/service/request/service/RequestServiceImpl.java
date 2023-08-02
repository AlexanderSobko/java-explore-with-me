package ru.practicum.ewm.main.service.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.event.model.EventState;
import ru.practicum.ewm.main.service.event.service.EventService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.request.RequestRepository;
import ru.practicum.ewm.main.service.request.dto.RequestDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusResponseDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusUpdateDto;
import ru.practicum.ewm.main.service.request.model.Request;
import ru.practicum.ewm.main.service.request.model.RequestStatus;
import ru.practicum.ewm.main.service.user.model.User;
import ru.practicum.ewm.main.service.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImpl implements RequestService {

    RequestRepository repo;
    UserService userService;
    EventService eventService;

    @Override
    public RequestDto save(int userId, int eventId) {
        User requester = userService.getById(userId);
        Event event = eventService.getEventById(eventId);
        validateRequestExistsOrUserIsInitiator(requester, event);
        validateEventPublishedAndLimit(event);
        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .requestStatus(event.isRequestModeration() && !(event.getParticipantLimit() == 0) ?
                        RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        request = repo.save(request);
        log.info("Заявка на участие создана! {}", request);
        return RequestDto.mapToRequestDto((request));
    }

    @Override
    public RequestDto cancelRequest(int userId, int requestId) {
        User user = userService.getById(userId);
        Request request = repo.findByIdAndRequester(requestId, user).orElseThrow(() ->
                new NotFoundException(String.format("Запрос с id(%d) не найден или недоступен!", requestId)));
        request.setRequestStatus(RequestStatus.CANCELED);
        log.info("Пользователь отменил заявку! {}", request);
        return RequestDto.mapToRequestDto(repo.save(request));
    }

    @Override
    public List<RequestDto> getAllUserRequests(int userId) {
        User user = userService.getById(userId);
        return repo.findAllByRequester(user).stream()
                .map(RequestDto::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestStatusResponseDto updateRequestsByInitiator(int userId, int eventId,
                                                              RequestStatusUpdateDto dto) {
        Event event = eventService.getByInitiatorAndId(userId, eventId);
        int spotsAvailable = event.getParticipantLimit() - event.getConfirmedRequests();
        List<Request> requests = repo.findAllByEventAndIdIn(event, dto.getRequestIds());
        if (RequestStatus.CONFIRMED.name().equals(dto.getStatus()) && spotsAvailable == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Достигнут лимит заявок на участие!");
        }
        if (!requests.stream().allMatch(r -> r.getRequestStatus().equals(RequestStatus.PENDING))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Статус можно изменить только у заявок, находящихся в состоянии ожидания!");
        }
        for (Request request : requests) {
            if (spotsAvailable == 0 || RequestStatus.REJECTED.name().equals(dto.getStatus())) {
                request.setRequestStatus(RequestStatus.REJECTED);
            } else {
                request.setRequestStatus(RequestStatus.CONFIRMED);
                spotsAvailable--;
            }
        }
        requests = repo.saveAll(requests);
        log.info("Статус заявок изменен! {}", requests);
        return RequestStatusResponseDto.builder()
                .confirmedRequests(requests.stream()
                        .filter(request -> request.getRequestStatus().equals(RequestStatus.CONFIRMED))
                        .map(RequestDto::mapToRequestDto)
                        .collect(Collectors.toList()))
                .rejectedRequests(requests.stream()
                        .filter(request -> request.getRequestStatus().equals(RequestStatus.REJECTED))
                        .map(RequestDto::mapToRequestDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<RequestDto> getAllRequestsToUserEvent(int userId, int eventId) {
        Event event = eventService.getByInitiatorAndId(userId, eventId);
        return repo.findAllByEvent(event).stream()
                .map(RequestDto::mapToRequestDto)
                .collect(Collectors.toList());
    }

    private void validateRequestExistsOrUserIsInitiator(User requester, Event event) {
        if (repo.existsByRequesterAndEvent(requester, event)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Нельзя добавить повторный запрос!");
        } else if (event.getInitiator().getId() == requester.getId()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Инициатор события не может добавить запрос на участие в своём событии!");
        }
    }

    private void validateEventPublishedAndLimit(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Нельзя участвовать в неопубликованном событии!");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Достигнут лимит запросов на участие!");
        }
    }

}
