package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingValidationManager validationManager;

    @GetMapping("{id}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        validationManager.validateBookingOwnerBooker(id, userId);
        return BookingMapper.toBookingDto(bookingService.getBooking(id));
    }

    @PostMapping
    public BookingDto createBooking(@Valid @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ExternalBookingDto bookingDto) {
        validationManager.validateBookingCreation(bookingDto, userId);
        return BookingMapper.toBookingDto(bookingService.createBooking(bookingDto, userId));
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateStatus(@RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("bookingId") Long bookingId) {
        validationManager.validateBookingOwner(bookingId, userId);
        return BookingMapper.toBookingDto(bookingService.saveBooking(bookingId, approved));
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        validationManager.validateState(state);
        validationManager.validateUser(userId);
        return bookingService.getAllByUser(userId, state)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(defaultValue = "ALL") String state) {
        validationManager.validateState(state);
        validationManager.validateUser(userId);
        return bookingService.getAllByOwner(userId, state)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
