package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringBookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime after, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                             LocalDateTime before,
                                                                             LocalDateTime after,
                                                                             Pageable pageable);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(Long userId, LocalDateTime after, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                              LocalDateTime before,
                                                                              LocalDateTime after,
                                                                              Pageable pageable);

    List<Booking> findAllByItemOwnerOrderByStartDesc(Long userId, Pageable pageable);

    Booking getFirstByItemIdOrderByEndDesc(Long itemId);

    Booking getFirstByItemIdOrderByStartAsc(Long itemId);

    Integer countByItemIdAndBookerIdAndStatusAndStartBefore(Long itemId,
                                                            Long userId,
                                                            Status status,
                                                            LocalDateTime now);
}
