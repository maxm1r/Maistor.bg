package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.ForbiddenException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentResponseDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithOwnerDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.ReplyDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.service.CommentService;
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private ModelMapper mapper;

    @PostMapping("/{id}/comments")
    public CommentWithoutUserPasswordDTO add(@RequestBody CommentResponseDTO comment, HttpSession session, HttpServletRequest request, @PathVariable int id){
        UserUtility.validateLogin(session, request);
        return commentService.addComment(comment, (Integer)session.getAttribute(UserController.USER_ID), id);
    }

    @GetMapping("/comments/{id}")
        public CommentWithOwnerDTO getById(@PathVariable int id,HttpSession session,HttpServletRequest request){
            UserUtility.validateLogin(session,request);
            Comment comment = commentService.getCommentById(id);
            CommentWithOwnerDTO dto = new CommentWithOwnerDTO();
            dto.setId(comment.getId());
            dto.setText(comment.getText());
            dto.setPostedDate(comment.getPostedDate());
            dto.setOwnerID(mapper.map(comment.getOwnerId(), UserWithoutCommentDTO.class));
            return dto;
        }

    @DeleteMapping("/comments/{id}")
        public CommentWithoutUserPasswordDTO delete(@PathVariable int id,HttpSession session,HttpServletRequest request){
        UserUtility.validateLogin(session,request);
        int commentId= commentService.getCommentById(id).getOwnerId().getId();
        Comment comment = new Comment();

        comment.setId(id);
        if((Integer) session.getAttribute(UserController.USER_ID) != commentId){
            throw new ForbiddenException("You are not the owner of this comment!");
        }

        CommentWithoutUserPasswordDTO dto = new CommentWithoutUserPasswordDTO(commentService.getCommentById(id));
        commentService.deleteCommentById(id);
        return dto;
    }

    @PutMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDTO> edit(@RequestBody CommentResponseDTO comment, @PathVariable int id, HttpSession session, HttpServletRequest request){
        UserUtility.validateLogin(session,request);
        Comment c = commentService.getCommentById(comment.getId());
        if((Integer) session.getAttribute(UserController.USER_ID) != c.getOwnerId().getId()){
            throw new ForbiddenException("You are not the owner of this comment!");
        }

        Integer userId = (Integer) session.getAttribute(UserController.USER_ID);
        Optional<User> u = userRepository.findById(userId);
        c.setId(comment.getId());
        c.setOwnerId(u.orElseThrow(()-> new NotFoundException("User not found!")));
        c.setWorkmanId(userRepository.getById(id));
        c.setPostedDate(LocalDateTime.now());
        c.setText(comment.getText());
        commentService.edit(c);

        CommentResponseDTO dto = mapper.map(c, CommentResponseDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<ReplyDTO>> getReplies(@PathVariable int id){
        List<Comment> comments = commentService.getAllByParentId(id);
        List<ReplyDTO> replies = mapper.map(comments, new TypeToken<List<ReplyDTO>>() {}.getType());
        return ResponseEntity.ok(replies);
    }
}
