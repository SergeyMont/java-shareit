package ru.practicum.gateway.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).build());
    }

    public Object createRequest(long userId, RequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public Object getRequest(long itemRequestId, long userId) {
        return get("/" + itemRequestId, userId);
    }

    public Object getAllByOwnerId(long userId) {
        return get("", userId);
    }

    public Object getAllRequests(long userId, int from, int size) {
        return get("/all", userId, Map.of("from", from, "size", size));
    }
}
