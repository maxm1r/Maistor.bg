package com.finalproject.finalproject.model.dto.commentDTOS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithoutOwnerDTO {

    private int id;
    private String text;
    private LocalDateTime postedDate;
}
