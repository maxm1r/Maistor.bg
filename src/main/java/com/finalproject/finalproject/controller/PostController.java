package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PostController extends CustomExceptionHandler {

    @Autowired
    PostService postService;

    @PostMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable int id, @RequestBody PostDTO postDTO){

        return ResponseEntity.ok(postService.createPost(id,postDTO));
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> deletePost(@PathVariable int id){
        return ResponseEntity.ok(postService.deletePost(id));
    }
    @GetMapping("/post/all")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> editPost(@RequestBody PostDTO postDTO,@PathVariable int id ){
        return ResponseEntity.ok(postService.editPost(postDTO,id));
    }
    @PostMapping("/post/{postId}/offer/{offerId}")
    public ResponseEntity<PostResponseDTO> acceptOffer(@PathVariable int postId, @PathVariable int offerId){
        return ResponseEntity.ok(postService.acceptOffer(postId,offerId));
    }
}
