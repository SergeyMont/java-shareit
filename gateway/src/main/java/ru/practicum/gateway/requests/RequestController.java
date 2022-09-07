package ru.practicum.gateway.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping
    public Object getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getAllByOwnerId(userId);
    }

    @GetMapping("{id}")
    public Object get(@PathVariable @Positive long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequest(id, userId);
    }

    @PostMapping
    public Object create(@Valid @RequestBody RequestDto requestDto,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("create request {}", requestDto);
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping("/all")
    public Object getAll(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                         @RequestParam(defaultValue = "10") @Positive int size,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getAllRequests(userId, from, size);
    }
}
