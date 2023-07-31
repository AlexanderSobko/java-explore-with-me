package ru.practicum.ewm.main.service.request.service;

import ru.practicum.ewm.main.service.request.dto.RequestDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusResponseDto;
import ru.practicum.ewm.main.service.request.dto.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {

    RequestDto save(int userId, int eventId);

    RequestDto cancelRequest(int userId, int requestId);

    List<RequestDto> getAllUserRequests(int userId);

    RequestStatusResponseDto updateRequestsByInitiator(int userId, int eventId, RequestStatusUpdateDto request);

    List<RequestDto> getAllRequestsToUserEvent(int userId, int eventId);

}
