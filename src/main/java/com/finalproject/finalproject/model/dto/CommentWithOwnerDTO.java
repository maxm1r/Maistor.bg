package com.finalproject.finalproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithOwnerDTO {

    private int id;
    private String text;
    private LocalDateTime postedDate;
    private UserWithoutCommentDTO ownerID;
}
