package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.exceptions.NotSuchBookingException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserChangeItemException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.SpringCommentRepository;
import ru.practicum.shareit.item.repository.SpringItemRepository;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {
    private final SpringItemRepository itemRepository;
    private final SpringUserRepository userRepository;

    private final SpringCommentRepository commentRepository;
    private final BookingService bookingService;

    public ItemCommentDto getItemById(Long itemId, Long userId) {
        ItemDto itemDto;
        if (itemRepository.existsById(itemId)) {
            itemDto = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        } else throw new ItemNotFoundException("Item not found");
        ItemCommentDto result = ItemMapper.itemCommentDto(itemDto);
        result.setComments(commentRepository.findAllByItemId(itemId).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        if (Objects.equals(itemRepository.findById(itemId).get().getOwner(), userId)) {
            result.setLastBooking(bookingService.getLastByItemId(itemId));
            result.setNextBooking(bookingService.getNextByItemId(itemId));
        }
        return result;
    }


    public List<ItemCommentDto> getAllByUserId(Long userId) {
        return itemRepository.findAllByOwner(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .map(itemDto -> {
                    ItemCommentDto itemCommentDto = ItemMapper.itemCommentDto(itemDto);
                    itemCommentDto.setLastBooking(bookingService.getLastByItemId(itemDto.getId()));
                    itemCommentDto.setNextBooking(bookingService.getNextByItemId(itemDto.getId()));
                    itemCommentDto.setComments(commentRepository.findAllByItemId(itemDto.getId()).stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
                    return itemCommentDto;
                })
                .collect(Collectors.toList());
    }

    public ItemDto createItemDto(ItemDto itemDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User in not created");
        }
        if (itemDto.getName().isEmpty() || itemDto.getDescription() == null || itemDto.getAvailable() == null) {
            throw new ItemValidationException("Name and Description can't be empty");
        }
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    public ItemDto updateItemDto(ItemDto itemDto, Long userId) {
        if (!userRepository.existsById(userId) || !getAllByUserId(userId).stream().map(ItemCommentDto::getId).collect(Collectors.toList()).contains(itemDto.getId())) {
            throw new WrongUserChangeItemException("Wrong user can't change other users items");
        }
        if (itemDto.getName() == null && itemDto.getDescription() == null) {
            ItemDto innerDto = ItemMapper.toItemDto(itemRepository.findById(itemDto.getId()).get());
            innerDto.setAvailable(itemDto.getAvailable());
            itemDto = innerDto;
        } else if (itemDto.getAvailable() == null && itemDto.getDescription() == null) {
            ItemDto innerDto = ItemMapper.toItemDto(itemRepository.findById(itemDto.getId()).get());
            innerDto.setName(itemDto.getName());
            itemDto = innerDto;
        } else if (itemDto.getAvailable() == null && itemDto.getName() == null) {
            ItemDto innerDto = ItemMapper.toItemDto(itemRepository.findById(itemDto.getId()).get());
            innerDto.setDescription(itemDto.getDescription());
            itemDto = innerDto;
        }
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    public List<ItemDto> searchItemByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Comment addComment(Long id, ExternalCommentDto commentDto, Long userId) {
        if (!bookingService.isHasBookingsByItemIdAndUserId(id, userId)) {
            throw new NotSuchBookingException("Haven't bookings");
        }
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemRepository.findById(id).get());
        comment.setAuthor(userRepository.findById(userId).get());
        return commentRepository.save(comment);
    }
}
