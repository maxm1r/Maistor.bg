package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.AddCommentDTO;
import com.finalproject.finalproject.model.dto.CommentWithOwnerDTO;
import com.finalproject.finalproject.model.dto.UserWithoutCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping("/{id}/comments")
    public Comment add(@RequestBody AddCommentDTO comment, HttpSession session, @PathVariable int id){
        return commentService.addComment(comment, (Integer)session.getAttribute(UserController.USER_ID), id);
    }

    @GetMapping("/comments/{id}")
        public CommentWithOwnerDTO getById(@PathVariable int id){
            Comment comment = commentService.getComment(id);
            CommentWithOwnerDTO dto = new CommentWithOwnerDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setPostedDate(comment.getPostedDate());
            dto.setOwnerID(mapper.map(comment.getOwnerId(), UserWithoutCommentDTO.class));
            return dto;
        }

}
