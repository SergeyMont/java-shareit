package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.ValidationEmailNullException;
import ru.practicum.shareit.user.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


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
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        validationUserEmail(userDto);
        if (userDto.getEmail() == null) {
            throw new ValidationEmailNullException("Email should be not null");
        }
        return userService.createUserDto(userDto);
    }

    private void validationUserEmail(UserDto userDto) {
        List<String> list = userService.getAllUserDto().stream().map(UserDto::getEmail).collect(Collectors.toList());
        if (userDto.getEmail() != null) {
            for (int i = 0; i < list.size(); i++) {
                if (userDto.getEmail().equals(list.get(i))) {
                    throw new ValidationException("Email should be unique");
                }
            }
        }
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        validationUserEmail(userDto);
        userDto.setId(userId);
        return userService.updateUserDto(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserDto(userId);
    }
}
