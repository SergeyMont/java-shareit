package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ExternalRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;


@Validated
@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createNewRequest(@Valid @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ExternalRequestDto requestDto) {
        return itemRequestService.addNewRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
        return itemRequestService.getAllRequestOrderByCreated(userId,pageable);
    }

    @GetMapping("{id}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemRequestService.getRequestById(userId, id);
    }
}
