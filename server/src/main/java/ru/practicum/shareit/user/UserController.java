package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.ValidationEmailNullException;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUserDto();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserDtoById(userId);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationEmailNullException("Email should be not null");
        }
        return userService.createUserDto(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        return userService.updateUserDto(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserDto(userId);
    }
}
