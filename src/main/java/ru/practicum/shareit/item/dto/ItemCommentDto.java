package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
}
