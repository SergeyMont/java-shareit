package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ShortBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortBookingDto that = (ShortBookingDto) o;
        return Objects.equals(id, that.id) && Objects.equals(itemId, that.itemId) && Objects.equals(bookerId, that.bookerId) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, bookerId, status);
    }
}
