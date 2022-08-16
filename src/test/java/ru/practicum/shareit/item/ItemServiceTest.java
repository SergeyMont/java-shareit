package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.item.dto.ExternalCommentDto;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.SpringCommentRepository;
import ru.practicum.shareit.item.repository.SpringItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemServiceTest {
    private final SpringCommentRepository commentRepository;

    private final SpringItemRepository itemRepository;

    private final BookingService bookingService;

    private final ItemService itemService;
    private final SpringUserRepository userRepository;
    User user = new User(1L, "Simple User", "user@mail.ru");
    User user2 = new User(2L, "Another User", "test@mail.ru");
    Item item = new Item(1L, "Unit", "Super unit", true, user.getId(), null);

    private final Item item1;
    private final ItemCommentDto itemCommentDto;

    @Autowired
    public ItemServiceTest(SpringCommentRepository commentRepository,
                           SpringUserRepository userRepository,
                           SpringItemRepository itemRepository,
                           BookingService bookingService,
                           ItemService itemService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        userRepository.save(user);
        userRepository.save(user2);
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        item1 = itemRepository.save(item);
        this.bookingService = bookingService;
        itemCommentDto = ItemMapper.itemCommentDto(ItemMapper.toItemDto(item1));
        itemCommentDto.setComments(new ArrayList<>());
        itemCommentDto.setLastBooking(bookingService.getLastByItemId(item1.getId()));
        itemCommentDto.setNextBooking(bookingService.getNextByItemId(item1.getId()));

    }

    @Test
    void getItemById() {
        assertEquals(itemCommentDto, itemService.getItemById(item1.getId(), user.getId()));
    }

    @Test
    void getAllByUserId() {
        assertEquals(List.of(itemCommentDto), itemService.getAllByUserId(item1.getOwner()));
    }

    @Test
    void testGetAllByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        assertEquals(List.of(itemCommentDto), itemService.getAllByUserId(item.getOwner(), pageable));

    }

    @Test
    void createItemDto() {
        Item item2 = ItemMapper.toItem(itemService.createItemDto(ItemMapper.toItemDto(item1), item1.getOwner()));
        assertEquals(itemRepository.findById(item1.getId()).orElse(null), item2);
    }

    @Test
    void searchItemByText() {
        assertEquals(List.of(ItemMapper.toItemDto(item1)), itemService.searchItemByText("nit", null));
    }

    @Test
    void addComment() throws InterruptedException {
        ExternalBookingDto externalBookingDto = ExternalBookingDto.builder()
                .start(LocalDateTime.now().plusSeconds(2))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(item1.getId())
                .build();
        Booking booking = bookingService.createBooking(
                externalBookingDto, user2.getId());
        bookingService.saveBooking(booking.getId(), true, user.getId());
        ExternalCommentDto externalCommentDto = new ExternalCommentDto();
        externalCommentDto.setText("text");
        Thread.sleep(2000);
        Comment comment1 = itemService.addComment(
                item1.getId(),
                externalCommentDto,
                user2.getId());
        assertEquals(commentRepository.findById(comment1.getId()).orElse(null), comment1);
    }
}