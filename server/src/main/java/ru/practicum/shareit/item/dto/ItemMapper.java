package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
@AllArgsConstructor
public class ItemMapper {

    public static ItemDto toItemDto(@NotNull Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner() : null,
                item.getRequestId()
        );
    }

    public static Item toItem(@NotNull ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable() != null ? itemDto.getAvailable() : null,
                itemDto.getOwner() != null ? itemDto.getOwner() : null,
                itemDto.getRequestId()
        );
    }

    public static ItemCommentDto itemCommentDto(ItemDto itemDto) {
        return new ItemCommentDto(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId(), null, null, null
        );
    }
}
