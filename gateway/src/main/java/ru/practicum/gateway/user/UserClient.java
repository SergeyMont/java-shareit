package ru.practicum.gateway.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).build());
    }

    public Object create(UserDto userDto) {
        return post("", -1, userDto);
    }

    public Object update(long userId, UserDto userDto) {
        return patch("/" + userId, -1, userDto);
    }

    public void delete(long userId) {
        delete("/" + userId, -1);
    }

    public Object get(long userId) {
        return get("/" + userId, -1);
    }

    public Object getAll() {
        return get("", -1);
    }
}
