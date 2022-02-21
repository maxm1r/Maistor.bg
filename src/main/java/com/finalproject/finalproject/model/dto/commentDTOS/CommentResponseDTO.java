package com.finalproject.finalproject.model.dto.commentDTOS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDTO {

    private int id;
    private String text;
    private int parentCommentId;
}
