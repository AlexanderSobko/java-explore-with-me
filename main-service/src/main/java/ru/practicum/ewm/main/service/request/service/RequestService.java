package ru.practicum.ewm.main.service.request.service;

import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.request.dto.RequestDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusResponseDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusUpdateDto;
import ru.practicum.ewm.main.service.request.model.RequestStatus;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface RequestService {

    RequestDto save(int userId, int eventId);

    RequestDto cancelRequest(int userId, int requestId);

    List<RequestDto> getAllUserRequests(int userId);

    RequestStatusResponseDto updateRequestsByInitiator(int userId, int eventId, RequestStatusUpdateDto request);

    List<RequestDto> getAllRequestsToUserEvent(int userId, int eventId);

    boolean existByByRequesterAndEventAndStatus(User requester, Event event, RequestStatus status);

}
