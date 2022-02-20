package com.finalproject.finalproject.model.dto.commentDTOS;

import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentWithoutUserPasswordDTO {


    private int id;
    private String text;
    private LocalDateTime postedDate;
    private UserWithoutPasswordDTO ownerID;
    private UserWithoutPasswordDTO commentedUserID;

    public CommentWithoutUserPasswordDTO(Comment comment){
        ModelMapper mapper = new ModelMapper();
        this.id = comment.getId();
        this.text = comment.getText();
        this.postedDate = comment.getPostedDate();
        this.ownerID = mapper.map(comment.getOwnerId(), UserWithoutPasswordDTO.class);
        this.commentedUserID = mapper.map(comment.getOwnerId(), UserWithoutPasswordDTO.class);
}

}
