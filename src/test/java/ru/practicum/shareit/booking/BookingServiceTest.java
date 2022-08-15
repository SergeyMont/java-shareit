package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.exceptions.ValidationBookingOwnerException;
import ru.practicum.shareit.booking.repository.SpringBookingRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceTest {
    private final SpringBookingRepository bookingRepository;

    private final BookingService bookingService;

    private final ItemService itemService;
    private final UserService userService;
    User user = new User(1L, "Simple User", "user@mail.ru");
    User user2 = new User(2L, "Another User", "test@mail.ru");
    Item item = new Item(1L, "Unit", "Super unit", true, user.getId(), null);
    Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), item, user2, Status.WAITING);

    @Autowired
    public BookingServiceTest(SpringBookingRepository bookingRepository,
                              BookingService bookingService,
                              ItemService itemService,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.userService = userService;
        userService.createUserDto(UserMapper.toUserDto(user));
        userService.createUserDto(UserMapper.toUserDto(user2));
        itemService.createItemDto(ItemMapper.toItemDto(item), item.getOwner());
        bookingRepository.save(booking);
    }

    @Test
    void getBooking() {
        assertEquals(booking, bookingService.getBooking(booking.getId(), booking.getBooker().getId()));
    }

    @Test
    void testGetWrongUser() {
        assertThrows(ValidationBookingOwnerException.class, () -> bookingService.getBooking(booking.getId(), 100L));
    }

    @Test
    void createBooking() {
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        Booking booking1 = bookingService.createBooking(externalBookingDto, 2L);
        assertEquals(bookingRepository.findById(booking1.getId()).orElse(null), booking1);
    }

    @Test
    void saveBooking() {
        bookingService.saveBooking(booking.getId(), true, user.getId());
        assertEquals(bookingRepository.findById(booking.getId()).orElseThrow().getStatus(), Status.APPROVED);
    }

    @Test
    void getAllByUser() {
        assertEquals(List.of(booking), bookingService.getAllByUser(user2.getId(), "ALL", null));
    }

    @Test
    void getAllByOwner() {
        assertEquals(List.of(booking), bookingService.getAllByOwner(user.getId(), "ALL", null));
    }

    @Test
    void isHasBookingsByItemIdAndUserId() {
        assertFalse(bookingService.isHasBookingsByItemIdAndUserId(item.getId(), user2.getId()));
    }

    @Test
    void getLastByItemId() {
        assertEquals(BookingMapper.toShortBookingDto(booking), bookingService.getLastByItemId(booking.getItem().getId()));
    }

    @Test
    void getNextByItemId() {
        booking.setStart(LocalDateTime.now().plusDays(2));
        Booking booking1 = bookingRepository.save(booking);
        assertEquals(BookingMapper.toShortBookingDto(booking1), bookingService.getNextByItemId(booking1.getItem().getId()));
    }
}