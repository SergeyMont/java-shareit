package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.requests.dto.ExternalRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceTest {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    User user = new User(1L, "Simple User", "user@mail.ru");
    ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now());

    @Autowired
    public ItemRequestServiceTest(
            ItemRequestRepository itemRequestRepository,
            ItemRequestService itemRequestService,
            UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestService = itemRequestService;
        this.userService = userService;
        userService.createUserDto(UserMapper.toUserDto(user));
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void addNewRequest() {
        ExternalRequestDto externalRequestDto = ExternalRequestDto.builder().description("text").build();
        ItemRequestDto itemRequest1 = itemRequestService.addNewRequest(itemRequest.getRequester().getId(), externalRequestDto);
        assertEquals(RequestMapper.toItemRequestDto(itemRequestRepository.findById(itemRequest1.getId()).orElseThrow()), itemRequest1);
    }

    @Test
    void getAllByUserId() {
        assertEquals(itemRequestService.getAllByUserId(Long.valueOf(1)), List.of(RequestMapper.toItemRequestDto(itemRequest)));
    }

    @Test
    void getRequestById() {
        assertEquals(itemRequestService.getRequestById(itemRequest.getId(), user.getId()), RequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    void getAllRequestOrderByCreated() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());
        assertEquals(itemRequestService.getAllRequestOrderByCreated(user.getId(), pageable),
                List.of(RequestMapper.toItemRequestDto(itemRequest)));
    }
}