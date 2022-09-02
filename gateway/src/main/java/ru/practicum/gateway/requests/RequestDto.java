package ru.practicum.gateway.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class RequestDto {
    @NotNull
    private String description;
}
