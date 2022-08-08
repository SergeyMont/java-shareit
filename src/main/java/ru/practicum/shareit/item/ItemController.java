package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemCommentDto findItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemCommentDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByUserId(userId);
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
    public List<ItemDto> searchByText(@RequestParam String text) {
        return itemService.searchItemByText(text);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@PathVariable Long id,
                                 @Valid @RequestBody ExternalCommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return CommentMapper.toCommentDto(itemService.addComment(id, commentDto, userId));
    }
}
