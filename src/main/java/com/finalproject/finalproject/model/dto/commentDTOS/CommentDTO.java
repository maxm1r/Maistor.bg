package com.finalproject.finalproject.model.dto.commentDTOS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {

    private int id;
    private String text;
    private Optional<Integer> parentCommentId;
}
