package ru.practicum.gateway.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    @NotBlank
    @Size(max = 4000)
    private String text;
    private LocalDateTime created = LocalDateTime.now();
}
