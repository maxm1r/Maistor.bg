package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CommentRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
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
        User u = userRepository.findById(workmanID).orElseThrow(()-> new NotFoundException("User not found!"));
        if (!u.isWorkman()){
            throw new BadRequestException("You can comment only workmen!");
        }
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setPostedDate(LocalDateTime.now());
        comment.setOwnerId(getUserById(userId));
        comment.setWorkmanId(getUserById(workmanID));
        if (commentDTO.getParentCommentId() != null){
            comment.setParentComment(commentRepository.findById(commentDTO.getParentCommentId().get()).orElseThrow(() -> new BadRequestException("Invalid parent comment")));
        }
        commentRepository.save(comment);
        CommentWithoutUserPasswordDTO dto = new CommentWithoutUserPasswordDTO(comment);
        return dto;
    }

    public Comment getCommentById(int id){
        return commentRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment not found!"));
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

    public List<Comment> getAllByOwner(int id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment not found!"));
        return commentRepository.findAllByOwnerId(user);
    }

    public List<Comment> getAllByParentId(int id){
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new NotFoundException("Comment not found!"));
        return commentRepository.findAllByParentComment(comment);
    }

    private User getUserById(int id){
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
    }
}
