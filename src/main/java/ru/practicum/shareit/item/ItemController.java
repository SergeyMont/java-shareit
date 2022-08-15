package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemCommentDto findItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemCommentDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size);
        return itemService.getAllByUserId(userId, pageable);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        return itemService.createItemDto(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        return itemService.updateItemDto(itemDto, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size);
        return itemService.searchItemByText(text, pageable);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@PathVariable Long id,
                                 @Valid @RequestBody ExternalCommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return CommentMapper.toCommentDto(itemService.addComment(id, commentDto, userId));
    }
}
