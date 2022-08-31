package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static ItemRequest toItemRequest(ExternalRequestDto request, User user) {
        return new ItemRequest(null, request.getDescription(), user, LocalDateTime.now());
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated());
    }
}
