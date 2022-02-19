package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CommentDTO;
import com.finalproject.finalproject.model.dto.CommentWithOwnerDTO;
import com.finalproject.finalproject.model.dto.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.dto.UserWithoutCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.service.CommentService;
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping("/{id}/comments")
    public CommentWithoutUserPasswordDTO add(@RequestBody CommentDTO comment, HttpSession session, HttpServletRequest request, @PathVariable int id){
        UserUtility.validateLogin(session, request);
        return commentService.addComment(comment, (Integer)session.getAttribute(UserController.USER_ID), id);
    }

    @GetMapping("/comments/{id}")
        public CommentWithOwnerDTO getById(@PathVariable int id,HttpSession session,HttpServletRequest request){
            UserUtility.validateLogin(session,request);
            Comment comment = commentService.getComment(id);
            CommentWithOwnerDTO dto = new CommentWithOwnerDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setPostedDate(comment.getPostedDate());
            dto.setOwnerID(mapper.map(comment.getOwnerId(), UserWithoutCommentDTO.class));
            return dto;
        }


}
