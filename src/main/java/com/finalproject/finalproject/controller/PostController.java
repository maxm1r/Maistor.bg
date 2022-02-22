package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.service.PostService;
import com.finalproject.finalproject.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;


@RestController
public class PostController extends CustomExceptionHandler {

    @Autowired
    PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponseDTO> createPost(HttpServletRequest request, @RequestBody PostDTO postDTO){
        UserUtility.validateLogin(request.getSession(),request);
        return ResponseEntity.ok(postService.createPost((Integer) request.getSession().getAttribute(UserController.USER_ID),postDTO));
    }
    @DeleteMapping("/post")
    public ResponseEntity<PostResponseDTO> deletePost(@RequestParam(name = "id") int id, HttpServletRequest request){
        UserUtility.validateLogin(request.getSession(),request);
        return ResponseEntity.ok(postService.deletePost(id, (Integer) request.getSession().getAttribute(UserController.USER_ID)));
    }
    @GetMapping("/post/all")
    public ResponseEntity<Set<PostResponseDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> editPost(@RequestBody PostDTO postDTO,@PathVariable int id ){
        return ResponseEntity.ok(postService.editPost(postDTO,id));
    }
    @PostMapping("/post/offer")
    public ResponseEntity<PostResponseDTO> acceptOffer(@RequestParam(name = "postId") int postId, @RequestParam(name = "offerId") int offerId){
        return ResponseEntity.ok(postService.acceptOffer(postId,offerId));
    }
}
