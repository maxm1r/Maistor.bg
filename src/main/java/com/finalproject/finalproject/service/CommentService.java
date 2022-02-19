package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.AddCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.repositories.CommentRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionException;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public Comment addComment(AddCommentDTO commentDTO, Integer userId, Integer workmanID){
        if (userId ==null ){
            throw new UnauthorizedException("Please login");
        }
        if (commentDTO.getText().isEmpty() || commentDTO.getText().isBlank()){
            throw new BadRequestException("There is no text in the comment body!");
        }
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setPostedDate(LocalDateTime.now());
        comment.setOwnerId(userRepository.findById(userId).orElseThrow(()-> new NotFoundException("Owner not found")));
        comment.setWorkmanId(userRepository.findById(workmanID).orElseThrow(()-> new NotFoundException("User not found")));
        commentRepository.save(comment);
        return comment;
    }

    public Comment getComment(int id){
        return getCommentById(id);
    }

    private Comment getCommentById(int id){
        return commentRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment not found!"));
    }
}
