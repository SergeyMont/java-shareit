package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable("itemId") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByUserId(userId);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        if (!userService.isUserCreated(userId)) {
            throw new UserNotFoundException("User in not created");
        }
        if (itemDto.getName().isEmpty() || itemDto.getDescription() == null | itemDto.getAvailable() == null) {
            throw new ItemValidationException("Name and Description can't be empty");
        }
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

}
