package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserChangeItemException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.SpringCommentRepository;
import ru.practicum.shareit.item.repository.SpringItemRepository;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {
    private final SpringItemRepository itemRepository;
    private final SpringUserRepository userRepository;

    private final SpringCommentRepository commentRepository;

    public ItemDto getItemById(Long id) {
        if (itemRepository.existsById(id)) {
            return ItemMapper.toItemDto(itemRepository.findById(id).get());
        } else throw new ItemNotFoundException("Item not found");
    }


    public List<ItemDto> getAllByUserId(Long userId) {
        return itemRepository.findAllByOwner(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItemDto(ItemDto itemDto, Long userId) {
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    public ItemDto updateItemDto(ItemDto itemDto, Long userId) {
        if (!userRepository.existsById(userId) || !getAllByUserId(userId).stream().map(ItemDto::getId).collect(Collectors.toList()).contains(itemDto.getId())) {
            throw new WrongUserChangeItemException("Wrong user can't change other users items");
        }
        if (itemDto.getName() == null && itemDto.getDescription() == null) {
            ItemDto i = getItemById(itemDto.getId());
            i.setAvailable(itemDto.getAvailable());
            itemDto = i;
        } else if (itemDto.getAvailable() == null && itemDto.getDescription() == null) {
            ItemDto i = getItemById(itemDto.getId());
            i.setName(itemDto.getName());
            itemDto = i;
        } else if (itemDto.getAvailable() == null && itemDto.getName() == null) {
            ItemDto i = getItemById(itemDto.getId());
            i.setDescription(itemDto.getDescription());
            itemDto = i;
        }
        itemDto.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    public List<ItemDto> searchItemByText(String text) {
        return itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }
}
