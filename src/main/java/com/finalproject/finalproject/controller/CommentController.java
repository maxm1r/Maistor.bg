package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.ForbiddenException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithOwnerDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.ReplyDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.service.CommentService;
import com.finalproject.finalproject.utility.SessionManager;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/{id}/comments")
    public CommentWithoutUserPasswordDTO add(@RequestBody CommentDTO comment, HttpServletRequest request, @PathVariable int id){
        sessionManager.verifyUser(request);
        return commentService.addComment(comment, (Integer)request.getSession().getAttribute(SessionManager.USER_ID), id);
    }

    @GetMapping("/comments/{id}")
        public CommentWithOwnerDTO getById(@PathVariable int id,HttpServletRequest request){
            sessionManager.verifyUser(request);
            Comment comment = commentService.getCommentById(id);
            CommentWithOwnerDTO dto = new CommentWithOwnerDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setPostedDate(comment.getPostedDate());
            dto.setOwnerID(mapper.map(comment.getOwnerId(), UserWithoutCommentDTO.class));
            return dto;
        }

    @DeleteMapping("/comments/{id}")
        public CommentWithoutUserPasswordDTO delete(@PathVariable int id,HttpServletRequest request){
        sessionManager.verifyUser(request);
        int commentOwnerId= commentService.getCommentById(id).getOwnerId().getId();
        Comment comment = new Comment();
        comment.setId(id);
        if((Integer) request.getSession().getAttribute(SessionManager.USER_ID) != commentOwnerId){
            throw new ForbiddenException("You are not the owner of this comment!");
        }
        CommentWithoutUserPasswordDTO dto = new CommentWithoutUserPasswordDTO(commentService.getCommentById(id));
        commentService.deleteCommentById(id);
        return dto;
    }

    @PutMapping("/comments")
    public ResponseEntity<CommentDTO> edit(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        sessionManager.verifyUser(request);
        Comment comment = commentService.getCommentById(commentDTO.getId());
        if((Integer) request.getSession().getAttribute(SessionManager.USER_ID) != comment.getOwnerId().getId()){
            throw new ForbiddenException("You are not the owner of this comment!");
        }
        if (commentDTO.getText() == null || commentDTO.getText().isEmpty() || commentDTO.getText().isBlank()){
            throw new BadRequestException("There is no text in the comment body!");
        }
        comment.setText(commentDTO.getText());
        commentService.edit(comment);
        CommentDTO dto = mapper.map(comment, CommentDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<ReplyDTO>> getReplies(@PathVariable int id){
        List<Comment> comments = commentService.getAllByParentId(id);
        List<ReplyDTO> replies = mapper.map(comments, new TypeToken<List<ReplyDTO>>() {}.getType());
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ReplyDTO>> getCommentsForUser(@PathVariable int id){
        List<Comment> comments = commentService.getAllByOwner(id);
        List<ReplyDTO> dtos = mapper.map(comments, new TypeToken<List<ReplyDTO>>(){}.getType());
        return ResponseEntity.ok(dtos);
    }
}
