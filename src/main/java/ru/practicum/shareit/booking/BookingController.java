package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.exceptions.StateValidationException;
import ru.practicum.shareit.booking.exceptions.ValidationBookingOwnerException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exceptions.ValidationException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @GetMapping("{id}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        Booking booking = bookingService.getBooking(id);
        if (!(Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner(), userId))) {
            throw new ValidationBookingOwnerException("You are not owner or booker");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @PostMapping
    public BookingDto createBooking(@Valid @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ExternalBookingDto bookingDto) {
        Item item = ItemMapper.toItem(itemService.getItemById(bookingDto.getItemId()));
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
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(UserMapper.toUser(userService.getUserDtoById(userId)));
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        return BookingMapper.toBookingDto(bookingService.createBooking(booking));
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateStatus(@RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("bookingId") Long bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        if (!Objects.equals(booking.getItem().getOwner(), userId)) throw new ValidationBookingOwnerException("You are not owner");
        if (booking.getStatus().equals(Status.APPROVED))
            throw new ItemValidationException("Item is approved status of booking");
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.toBookingDto(bookingService.saveBooking(booking));
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        if (!ObjectUtils.containsConstant(State.values(), state)) {
            throw new StateValidationException("Unknown state: " + state);
        }
        return bookingService.getAllByUser(userId, state)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        if (!ObjectUtils.containsConstant(State.values(), state)) {
            throw new StateValidationException("Unknown state: " + state);
        }
        return bookingService.getAllByOwner(userId, state)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
