package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
