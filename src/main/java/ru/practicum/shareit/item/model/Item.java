package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    Long request;
}
