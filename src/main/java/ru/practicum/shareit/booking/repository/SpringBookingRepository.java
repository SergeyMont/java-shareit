package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringBookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime before);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime after);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                             LocalDateTime before,
                                                                             LocalDateTime after);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime before);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Long userId, LocalDateTime after);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                              LocalDateTime before,
                                                                              LocalDateTime after);

    List<Booking> findAllByItemOwnerOrderByStartDesc(Long userId);

    Booking getFirstByItemIdOrderByEndDesc(Long itemId);

    Booking getFirstByItemIdOrderByStartAsc(Long itemId);

    Integer countByItemIdAndBookerIdAndStatusAndStartBefore(Long itemId,
                                                            Long userId,
                                                            Status status,
                                                            LocalDateTime now);
}
