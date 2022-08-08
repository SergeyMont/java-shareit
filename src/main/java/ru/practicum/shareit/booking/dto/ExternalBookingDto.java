package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ExternalBookingDto {
    @FutureOrPresent
    private LocalDateTime start;
    @FutureOrPresent
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
