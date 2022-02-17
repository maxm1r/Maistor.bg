package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.PostDTO;
import com.finalproject.finalproject.model.dto.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
public class PostController extends CustomExceptionHandler {

    @Autowired
    PostService postService;

    @PostMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable int id, @RequestBody PostDTO postDTO){
        return ResponseEntity.ok(postService.createPost(id,postDTO));
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable int id){
        return ResponseEntity.ok(postService.deletePost(id));
    }
    @GetMapping("/post/all")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }
}
