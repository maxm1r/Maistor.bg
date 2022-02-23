package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.ForbiddenException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentRequestDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithOwnerDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.CommentWithoutUserPasswordDTO;
import com.finalproject.finalproject.model.dto.commentDTOS.ReplyDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutCommentDTO;
import com.finalproject.finalproject.model.pojo.Comment;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.service.CommentService;
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
    public CommentWithoutUserPasswordDTO add(@RequestBody CommentRequestDTO comment, HttpServletRequest request, @PathVariable int id){
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
        int commentId= commentService.getCommentById(id).getOwnerId().getId();
        Comment comment = new Comment();

        comment.setId(id);
        if((Integer) request.getSession().getAttribute(SessionManager.USER_ID) != commentId){
            throw new ForbiddenException("You are not the owner of this comment!");
        }

        CommentWithoutUserPasswordDTO dto = new CommentWithoutUserPasswordDTO(commentService.getCommentById(id));
        commentService.deleteCommentById(id);
        return dto;
    }

    @PutMapping("/{id}/comments")
    public ResponseEntity<CommentRequestDTO> edit(@RequestBody CommentRequestDTO comment, @PathVariable int id, HttpServletRequest request){
        sessionManager.verifyUser(request);
        Comment c = commentService.getCommentById(comment.getId());
        if((Integer) request.getSession().getAttribute(SessionManager.USER_ID) != c.getOwnerId().getId()){
            throw new ForbiddenException("You are not the owner of this comment!");
        }

        Integer userId = (Integer) request.getSession().getAttribute(SessionManager.USER_ID);
        Optional<User> u = userRepository.findById(userId);
        c.setId(comment.getId());
        c.setOwnerId(u.orElseThrow(()-> new NotFoundException("User not found!")));
        c.setWorkmanId(userRepository.getById(id));
        c.setPostedDate(LocalDateTime.now());
        c.setText(comment.getText());
        commentService.edit(c);

        CommentRequestDTO dto = mapper.map(c, CommentRequestDTO.class);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<ReplyDTO>> getReplies(@PathVariable int id){
        List<Comment> comments = commentService.getAllByParentId(id);
        List<ReplyDTO> replies = mapper.map(comments, new TypeToken<List<ReplyDTO>>() {}.getType());
        return ResponseEntity.ok(replies);
    }
}
