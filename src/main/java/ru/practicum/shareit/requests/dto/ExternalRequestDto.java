package ru.practicum.shareit.requests.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExternalRequestDto {
    @NotNull
    private String description;
}
