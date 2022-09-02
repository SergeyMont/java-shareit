package ru.practicum.shareit.user.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
