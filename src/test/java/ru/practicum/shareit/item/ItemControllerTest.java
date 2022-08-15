package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
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

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ItemService itemService;

    User user = new User(1L, "Simple User", "user@mail.ru");
    User user2 = new User(2L, "Another User", "test@mail.ru");
    Item item = new Item(1L, "Unit", "Super unit", true, user.getId(), null);
    Comment comment = new Comment(1L, "simple text", item, user2, LocalDateTime.now());
    ItemCommentDto itemCommentDto = ItemMapper.itemCommentDto(ItemMapper.toItemDto(item));
    ItemDto itemDto = ItemMapper.toItemDto(item);

    @Test
    void findItemById() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemCommentDto);
        mvc.perform(get("/items/{id}", item.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void findAllItemsByUserId() throws Exception {
        when(itemService.getAllByUserId(anyLong(), any())).thenReturn(List.of(itemCommentDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].name", is(item.getName())));
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItemDto(any(), anyLong())).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void updateItemById() throws Exception {
        when(itemService.getItemById(anyLong(),anyLong())).thenReturn(itemCommentDto);
        when(itemService.updateItemDto(any(), anyLong())).thenReturn(itemDto);
        mvc.perform(patch("/items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    void searchByText() throws Exception {
        when(itemService.searchItemByText(anyString(), any())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .param("text", "Super unit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(notNullValue())))
                .andExpect(jsonPath("$[0].name", is(item.getName())));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), any(), anyLong())).thenReturn(comment);
        mvc.perform(post("/items/{id}/comment", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(user2.getName())));
    }
}