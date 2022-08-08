package ru.practicum.shareit.booking.exceptions;

public class StateValidationException extends RuntimeException {
    public StateValidationException(String s) {
        super(s);
    }
}
