package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    User user = new User(1L, "Simple User", "user@mail.ru");
    User user2 = new User(2L, "Another User", "test@mail.ru");
    Item item = new Item(1L, "Unit", "Super unit", true, user.getId(), null);
    Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2L), item, user2, Status.WAITING);

    @Test
    void getById() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(booking);
        mvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking( any(), anyLong())).thenReturn(booking);
        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BookingMapper.toBookingDto(booking)))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())));
    }

    @Test
    void updateStatus() throws Exception {
        booking.setStatus(Status.APPROVED);
        Booking booking1 = booking;
        when(bookingService.saveBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(booking1);
        mvc.perform(patch("/bookings/{id}", booking.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
    }

    @Test
    void getAllByUser() throws Exception {
        when(bookingService.getAllByUser(anyLong(), anyString(), any())).thenReturn(List.of(booking));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())));
    }

    @Test
    void getAllByOwner() throws Exception {
        when(bookingService.getAllByOwner(anyLong(), anyString(), any())).thenReturn(List.of(booking));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())));
    }
}