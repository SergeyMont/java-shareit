package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ExternalRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    public ItemRequestDto addNewRequest(Long userId, ExternalRequestDto requestDto) {
        validateUser(userId);
        User user = UserMapper.toUser(userService.getUserDtoById(userId));
        return RequestMapper.toItemRequestDto(itemRequestRepository.save(RequestMapper.toItemRequest(requestDto, user)));
    }

    public List<ItemRequestDto> getAllByUserId(Long userId) {
        validateUser(userId);
        return itemRequestRepository.getAllByRequesterId(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    public ItemRequestDto getRequestById(Long userId, Long id) {
        validateUser(userId);
        return RequestMapper.toItemRequestDto(itemRequestRepository.findById(id).get());
    }

    public List<ItemRequestDto> getAllRequestOrderByCreated(Long userId, Pageable pageable) {
        validateUser(userId);
        return itemRequestRepository.findAll(pageable).stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    private void validateUser(Long userId) {
        if (!userService.isUserCreated(userId)) {
            throw new UserNotFoundException("User in not created");
        }
    }
}
