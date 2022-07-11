package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> map = new HashMap<>();
    private Long counter = Long.valueOf(0);

    @Override
    public User getUserById(Long id) {
        return map.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        list.addAll(map.values());
        return list;
    }

    @Override
    public User createUser(User user) {
        if (user.getId() == null) {
            counter++;
            user.setId(counter);
        }
        map.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getEmail() == null & user.getName() != null) {
            User u = map.get(user.getId());
            u.setName(user.getName());
            user = u;
        }

        if (user.getEmail() != null & user.getName() == null) {
            User u = map.get(user.getId());
            u.setEmail(user.getEmail());
            user = u;
        }

        map.replace(user.getId(), user);
        User result = map.get(user.getId());
        return result;
    }

    @Override
    public void deleteUserById(Long id) {
        map.remove(id);
    }

    @Override
    public boolean isUserCreated(Long id) {
        return map.containsKey(id);
    }
}
