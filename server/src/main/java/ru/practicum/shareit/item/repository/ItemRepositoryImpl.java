package ru.practicum.shareit.item.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.exceptions.WrongUserChangeItemException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private Map<Long, List<Item>> map = new HashMap<>();


    private Long counter = Long.valueOf(0);

    @Override
    public Item createItem(Item item, Long userId) {
        List<Item> list = new ArrayList<>();
        counter++;
        item.setId(counter);
        if (map.containsKey(userId)) {
            list = map.get(userId);
        }
        list.add(item);
        map.put(userId, list);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long userId) {
        List<Item> list = map.get(userId);
        if (list == null || !list.stream().map(Item::getId).collect(Collectors.toList()).contains(item.getId())) {
            throw new WrongUserChangeItemException("Wrong user can't change other users items");
        }
        if (item.getName() == null && item.getDescription() == null) {
            Item i = getItem(item.getId());
            i.setAvailable(item.getAvailable());
            item = i;
        } else if (item.getAvailable() == null && item.getDescription() == null) {
            Item i = getItem(item.getId());
            i.setName(item.getName());
            item = i;
        } else if (item.getAvailable() == null && item.getName() == null) {
            Item i = getItem(item.getId());
            i.setDescription(item.getDescription());
            item = i;
        }
        Item finalItem = item;
        List<Item> newList = list.stream().map(i -> Objects.equals(i.getId(), finalItem.getId()) ? finalItem : i).collect(Collectors.toList());
        map.replace(userId, newList);
        return item;
    }

    @Override
    public Item getItem(Long id) {
        List<Item> result = getItemList();
        return result.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Item> getAllItem(Long userId) {
        return map.get(userId);
    }

    public List<Item> getAllSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> result = getItemList();
        List<Item> clean = result.stream().filter(i -> i.getAvailable() != null).collect(Collectors.toList());
        return clean.stream()
                .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Item> getItemList() {
        List<Item> result = new ArrayList<>();
        List<List<Item>> col = new ArrayList<>();
        col.addAll(map.values());
        for (int i = 0; i < col.size(); i++) {
            result.addAll(col.get(i));
        }
        return result;
    }
}
