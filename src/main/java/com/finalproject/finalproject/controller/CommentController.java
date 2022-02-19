package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.AddCommentDTO;
import com.finalproject.finalproject.model.dto.CommentWithoutOwnerDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{id}/comments")
    public Comment add(@RequestBody AddCommentDTO comment, HttpSession session, @PathVariable int id){
        return commentService.addComment(comment, (Integer)session.getAttribute(UserController.USER_ID), id);
    }
}
