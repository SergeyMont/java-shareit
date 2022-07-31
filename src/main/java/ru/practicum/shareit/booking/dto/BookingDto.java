package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;


    @Data
    static class Item {
        private Long id;
        private String name;

        public Item(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    static class User {
        private Long id;

        public User(Long id) {
            this.id = id;
        }
    }
}
