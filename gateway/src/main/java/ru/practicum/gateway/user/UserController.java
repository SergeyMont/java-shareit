package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public Object getAll() {
        return userClient.getAll();
    }

    @GetMapping("{id}")
    public Object get(@PathVariable @Positive long id) {
        return userClient.get(id);
    }

    @PostMapping
    public Object create(@Valid @RequestBody UserDto userDto) {
        log.info("create user {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("{id}")
    public Object update(@PathVariable("id") @Positive long userId,
                         @Valid @RequestBody UserDto userDto) {
        log.info("update user {}", userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable @Positive long id) {
        log.info("delete user with id={}", id);
        userClient.delete(id);
    }
}
