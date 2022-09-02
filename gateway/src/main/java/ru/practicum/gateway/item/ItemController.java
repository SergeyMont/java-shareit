package ru.practicum.gateway.item;

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
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public Object getAllItems(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                              @RequestParam(defaultValue = "10") @Positive int size,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("{id}")
    public Object get(@PathVariable @Positive long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItem(id, userId);
    }

    @PostMapping
    public Object create(@Valid @RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("create item {}", itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("{id}")
    public Object update(@PathVariable @Positive long id,
                         @Valid @RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("update item {}", itemDto);
        return itemClient.updateItem(id, userId, itemDto);
    }

    @GetMapping("/search")
    public Object search(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                         @RequestParam(defaultValue = "10") @Positive int size,
                         @RequestParam(defaultValue = "") String text) {
        return itemClient.search(text, from, size);
    }

    @PostMapping("{id}/comment")
    public Object addComment(@PathVariable @Positive long id,
                             @Valid @RequestBody CommentDto commentDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createComment(id, userId, commentDto);
    }
}
