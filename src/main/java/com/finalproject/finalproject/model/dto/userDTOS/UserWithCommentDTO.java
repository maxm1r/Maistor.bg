package com.finalproject.finalproject.model.dto.userDTOS;

import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutOwnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserWithCommentDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String profilePicture;
    List<CommentWithoutOwnerDTO> comments;
}
