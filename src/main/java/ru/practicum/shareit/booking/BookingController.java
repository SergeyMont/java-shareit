package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("{id}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return BookingMapper.toBookingDto(bookingService.getBooking(id, userId));
    }

    @PostMapping
    public BookingDto createBooking(@Valid @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ExternalBookingDto bookingDto) {
        return BookingMapper.toBookingDto(bookingService.createBooking(bookingDto, userId));
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateStatus(@RequestParam boolean approved,
                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("bookingId") Long bookingId) {
        return BookingMapper.toBookingDto(bookingService.saveBooking(bookingId, approved, userId));
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size);
        return bookingService.getAllByUser(userId, state, pageable)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(defaultValue = "ALL") String state,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size);
        return bookingService.getAllByOwner(userId, state, pageable)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
