package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.ReplyDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CommentRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public CommentWithoutUserPasswordDTO addComment(CommentDTO commentDTO, Integer userId, Integer workmanID){
        if (userId ==null ){
            throw new UnauthorizedException("Please login");
        }
        if (commentDTO.getText().isEmpty() || commentDTO.getText().isBlank()){
            throw new BadRequestException("There is no text in the comment body!");
        }
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setPostedDate(LocalDateTime.now());
        comment.setOwnerId(getUserById(userId));
        comment.setWorkmanId(getUserById(workmanID));
        comment.setParentComment(commentRepository.findById(commentDTO.getParentCommentId()).orElseThrow(()-> new BadRequestException("Invalid parent comment") ));
        commentRepository.save(comment);
        CommentWithoutUserPasswordDTO dto = new CommentWithoutUserPasswordDTO(comment);
        return dto;
    }

    public Comment getComment(int id){
        return getCommentById(id);
    }

    public boolean deleteCommentById(Integer id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Comment edit(Comment comment){
        Optional<Comment> opt = commentRepository.findById(comment.getId());
        if(opt.isPresent()){
            commentRepository.save(comment);
            return comment;
        }
        else{
            throw new NotFoundException("Comment not found");
        }
    }


    public List<Comment> getAllByParentId(int id){
        Comment comment = getCommentById(id);
        return commentRepository.findAllByParentComment(comment);
    }

    private Comment getCommentById(int id){
        return commentRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment not found!"));
    }
    private User getUserById(int id){
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
    }
}
