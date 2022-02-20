package com.finalproject.finalproject.model.dto.commentDTOS;

import com.finalproject.finalproject.model.pojo.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {

    private int id;
    private String text;
    private String ownerEmail;

    public ReplyDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.ownerEmail = comment.getOwnerId().getEmail();
    }
}
