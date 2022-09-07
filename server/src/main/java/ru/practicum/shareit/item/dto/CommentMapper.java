package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
@AllArgsConstructor
public class CommentMapper {
    public static Comment toComment(ExternalCommentDto commentDto) {
        return new Comment(
                null, commentDto.getText(), null, null, commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }
}
