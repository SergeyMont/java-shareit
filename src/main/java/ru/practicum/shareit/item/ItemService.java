package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemDto getItemById(Long id) {
        return ItemMapper.toItemDto(itemRepository.getItem(id));
    }

    public List<ItemDto> getAllByUserId(Long userId) {
        return itemRepository.getAllItem(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItemDto(ItemDto itemDto, Long userId) {
        return ItemMapper.toItemDto(itemRepository.createItem(ItemMapper.toItem(itemDto), userId));
    }

    public ItemDto updateItemDto(ItemDto itemDto, Long userId) {
        return ItemMapper.toItemDto(itemRepository.updateItem(ItemMapper.toItem(itemDto), userId));
    }

    public List<ItemDto> searchItemByText(String text) {
        return itemRepository.getAllSearch(text.toLowerCase())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
