package ru.practicum.shareit.booking.exceptions;

public class NotSuchBookingException extends RuntimeException {
    public NotSuchBookingException(String s) {
        super(s);
    }
}
