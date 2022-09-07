package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.SpringUserRepository;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    SpringUserRepository userRepository;

    @Autowired
    private UserService userService;
    static User user = new User(1L, "Simple User", "user@mail.ru");
    static User user2 = new User(2L, "Another User", "test@mail.ru");


    @Autowired
    public UserServiceTest(SpringUserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        userRepository.save(user);
        userRepository.save(user2);
        this.userService = userService;
    }

    @Test
    void getUserDtoById() {
        assertEquals(UserMapper.toUserDto(user), userService.getUserDtoById(1L));
    }

    @Test
    void getAllUserDto() {
        assertEquals(List.of(UserMapper.toUserDto(user), UserMapper.toUserDto(user2)), userService.getAllUserDto());
    }

    @Test
    void createUserDto() {
        UserDto user1 = userService.createUserDto(UserMapper.toUserDto(user));
        assertEquals(userRepository.findById(user1.getId()).orElse(null), user);
    }

    @Test
    void updateUserDto() {
        UserDto userDto = userService.getUserDtoById(1L);
        userDto.setName("Super");
        userService.updateUserDto(userDto);
        assertEquals(userRepository.findById(1L).orElse(null), UserMapper.toUser(userDto));
    }

    @Test
    void deleteUserDto() {
        userService.deleteUserDto(user.getId());
        assertNull(userRepository.findById(user.getId()).orElse(null));
    }

    @Test
    void isUserCreated() {
        assertTrue(userService.isUserCreated(1L));
        assertFalse(userService.isUserCreated(100L));
    }
}