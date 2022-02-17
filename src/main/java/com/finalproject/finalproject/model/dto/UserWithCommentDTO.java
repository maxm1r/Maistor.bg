package com.finalproject.finalproject.model.dto;

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
    List<CommentWithoutOwnerDTO> comments;
}
