package ru.practicum.gateway.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 4000)
    private String description;

    @NotNull
    private Boolean available;

    private Integer requestId;
}
