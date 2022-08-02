package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.repository.SpringBookingRepository;
import ru.practicum.shareit.item.exceptions.ItemValidationException;
import ru.practicum.shareit.item.repository.SpringItemRepository;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {
    private final SpringBookingRepository bookingRepository;
    private final SpringItemRepository itemRepository;
    private final SpringUserRepository userRepository;

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).get();
    }

    public Booking createBooking(ExternalBookingDto bookingDto, Long userId) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(userRepository.findById(userId).get());
        booking.setItem(itemRepository.findById(bookingDto.getItemId()).get());
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    public Booking saveBooking(Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getStatus().equals(Status.APPROVED))
            throw new ItemValidationException("Item is approved status of booking");
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllByUser(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "WAITING":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case "PAST":
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case "FUTURE":
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case "CURRENT":
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            default:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        }
    }

    public List<Booking> getAllByOwner(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "WAITING":
                return bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(userId, Status.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case "PAST":
                return bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(userId, now);
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(userId, now);
            case "CURRENT":
                return bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            default:
                return bookingRepository.findAllByItemOwnerOrderByStartDesc(userId);
        }
    }

    public boolean isHasBookingsByItemIdAndUserId(Long itemId, Long userId) {
        return bookingRepository.countByItemIdAndBookerIdAndStatusAndStartBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now()) > 0;
    }

    public ShortBookingDto getLastByItemId(Long itemId) {
        Booking booking = bookingRepository.getFirstByItemIdOrderByStartAsc(itemId);
        if (booking == null) return null;
        return BookingMapper.toShortBookingDto(booking);
    }

    public ShortBookingDto getNextByItemId(Long itemId) {
        Booking booking = bookingRepository.getFirstByItemIdOrderByEndDesc(itemId);
        if (booking == null) return null;
        return BookingMapper.toShortBookingDto(booking);
    }
}
