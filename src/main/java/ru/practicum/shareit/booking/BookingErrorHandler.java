package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.StateValidationException;
import ru.practicum.shareit.booking.exceptions.ValidationBookingOwnerException;
import ru.practicum.shareit.user.ErrorResponse;
import ru.practicum.shareit.user.exceptions.ValidationException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class BookingErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bookingValidation(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse bookingNotFoundValidation(final NoSuchElementException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse bookingUserValidation(final ValidationBookingOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bookingStateValidation(final StateValidationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
