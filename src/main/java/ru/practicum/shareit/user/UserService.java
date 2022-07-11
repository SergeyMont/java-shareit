package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserDtoById(Long id) {
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    public List<UserDto> getAllUserDto() {
        return userRepository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto createUserDto(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(userDto)));
    }

    public UserDto updateUserDto(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.updateUser(UserMapper.toUser(userDto)));
    }

    public void deleteUserDto(Long id) {
        userRepository.deleteUserById(id);
    }

    public boolean isUserCreated(Long id) {
        return userRepository.isUserCreated(id);
    }

    ;
}
