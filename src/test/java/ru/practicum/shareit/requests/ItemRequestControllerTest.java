package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.dto.RequestMapper;
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

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemRequestService itemRequestService;
    User user = new User(1L, "Simple User", "user@mail.ru");
    ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now());

    @Test
    void createNewRequest() throws Exception {
        when(itemRequestService.addNewRequest(anyLong(), any())).thenReturn(RequestMapper.toItemRequestDto(itemRequest));
        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestMapper.toItemRequestDto(itemRequest)))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));

    }

    @Test
    void getAllRequestsByUser() throws Exception {
        when(itemRequestService.getAllByUserId(anyLong())).thenReturn(List.of(RequestMapper.toItemRequestDto(itemRequest)));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequestOrderByCreated(anyLong(), any())).thenReturn(List.of(RequestMapper.toItemRequestDto(itemRequest)));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(RequestMapper.toItemRequestDto(itemRequest));
        mvc.perform(get("/requests/{id}", itemRequest.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())));
    }
}