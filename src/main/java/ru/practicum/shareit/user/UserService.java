package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exceptions.UnknownUserException;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final SpringUserRepository userRepository;

    public UserDto getUserDtoById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            return UserMapper.toUserDto(userRepository.findById(id).get());
        } else throw new UnknownUserException("Unknown User");
    }

    public List<UserDto> getAllUserDto() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto createUserDto(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    public UserDto updateUserDto(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userDto.getEmail() == null && userDto.getName() != null) {
            User innerUser = userRepository.findById(userDto.getId()).get();
            innerUser.setName(userDto.getName());
            user = innerUser;
        }

        if (userDto.getEmail() != null && userDto.getName() == null) {
            User innerUser = userRepository.findById(userDto.getId()).get();
            innerUser.setEmail(userDto.getEmail());
            user = innerUser;
        }
        if (userDto.getName() != null && userDto.getEmail() != null && userDto.getId() == null) {
            user = UserMapper.toUser(userDto);
            Example<User> example = Example.of(user);
            User innerUser = userRepository.findOne(example).get();
            innerUser.setName(userDto.getName());
            innerUser.setEmail(userDto.getEmail());
            user = innerUser;
        }

        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    public void deleteUserDto(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isUserCreated(Long id) {
        return userRepository.existsById(id);
    }

}
