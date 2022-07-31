package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.exceptions.NotSuchBookingException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemCommentDto findItemById(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemCommentDto result = ItemMapper.itemCommentDto(itemService.getItemById(itemId));
        result.setComments(itemService.getComments(itemId).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        if (Objects.equals(itemService.getItemById(itemId).getOwner(), userId)) {
            result.setLastBooking(bookingService.getLastByItemId(itemId));
            result.setNextBooking(bookingService.getNextByItemId(itemId));
        }
        return result;
    }

    @GetMapping
    public List<ItemCommentDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemService.getAllByUserId(userId).stream()
                .map(itemDto -> {
                    ItemCommentDto itemCommentDto = ItemMapper.itemCommentDto(itemDto);
                    itemCommentDto.setLastBooking(bookingService.getLastByItemId(itemDto.getId()));
                    itemCommentDto.setNextBooking(bookingService.getNextByItemId(itemDto.getId()));
                    itemCommentDto.setComments(itemService.getComments(itemDto.getId()).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
                    return itemCommentDto;
                }).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        if (!userService.isUserCreated(userId)) {
            throw new UserNotFoundException("User in not created");
        }
        if (itemDto.getName().isEmpty() || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
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
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.searchItemByText(text);
    }

    @PostMapping("{id}/comment")
    public CommentDto addComment(@PathVariable Long id,
                                 @Valid @RequestBody ExternalCommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (!bookingService.isHasBookingsByItemIdAndUserId(id, userId)) {
            throw new NotSuchBookingException("Haven't bookings");
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(ItemMapper.toItem(itemService.getItemById(id)));
        comment.setAuthor(UserMapper.toUser(userService.getUserDtoById(userId)));
        return CommentMapper.toCommentDto(itemService.addComment(comment));
    }

}
