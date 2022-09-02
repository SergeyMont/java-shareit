package ru.practicum.gateway.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).build());
    }

    public Object createItem(long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public Object updateItem(long itemId, long userId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public Object getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public Object getAllItems(long userId, int from, int size) {
        return get("", userId, Map.of("from", from, "size", size));
    }

    public Object search(String text, int from, int size) {
        return get("/search",  -1L, Map.of("text", text, "from", from, "size", size));
    }

    public Object createComment(long itemId, long userId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
