package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item, Long userId);

    Item updateItem(Item item, Long userId);

    Item getItem(Long id);

    List<Item> getAllItem(Long userId);

    List<Item> getAllSearch(String text);
}
