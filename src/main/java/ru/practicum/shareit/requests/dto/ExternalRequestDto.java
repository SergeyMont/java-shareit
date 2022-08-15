package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ExternalRequestDto {
    @NotNull
    private String description;
}
