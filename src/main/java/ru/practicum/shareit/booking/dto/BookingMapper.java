package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

import javax.validation.constraints.NotNull;

public class BookingMapper {

    public static Booking toBooking(@NotNull ExternalBookingDto bookingDto) {
        return new Booking(
                null,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null, null, null
        );
    }

    public static BookingDto toBookingDto(@NotNull Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.User(booking.getBooker().getId()),
                booking.getStatus()
        );
    }

    public static ShortBookingDto toShortBookingDto(@NotNull Booking booking) {
        return new ShortBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()
        );
    }
}
