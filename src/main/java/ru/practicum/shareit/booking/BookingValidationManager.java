package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.exceptions.StateValidationException;
import ru.practicum.shareit.booking.exceptions.ValidationBookingOwnerException;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.SpringItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class BookingValidationManager {
    private final BookingService bookingService;
    private final UserService userService;
    private final SpringItemRepository itemRepository;

    public void validateBookingOwnerBooker(Long bookingId, Long userId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (!(Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner(), userId))) {
            throw new ValidationBookingOwnerException("You are not owner or booker");
        }
    }

    public void validateState(String state) {
        if (!ObjectUtils.containsConstant(State.values(), state)) {
            throw new StateValidationException("Unknown state: " + state);
        }
    }

    public void validateBookingOwner(Long bookingId, Long userId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (!Objects.equals(booking.getItem().getOwner(), userId))
            throw new ValidationBookingOwnerException("You are not owner");
    }

    public void validateBookingCreation(ExternalBookingDto bookingDto, Long userId) {
        Item item = itemRepository.findById(bookingDto.getItemId()).get();
        if (!item.getAvailable()) throw new ItemValidationException("Item isn't available");

        if (Objects.equals(item.getOwner(), userId)) {
            throw new NoSuchElementException();
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start can't be in past");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("End of booking before start");
        }
        if (!userService.isUserCreated(userId)) {
            throw new UserNotFoundException("User in not created");
        }
    }

    public void validateUser(Long userId) {
        if (!userService.isUserCreated(userId)) {
            throw new UserNotFoundException("User in not created");
        }
    }
}
