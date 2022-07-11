package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * // TODO .
 */
@Data
public class User {
    Long id;
    String name;
    @Email
    String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
