package ru.practicum.gateway.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(max = 250)
    private String name;

    @Email
    private String email;
}
