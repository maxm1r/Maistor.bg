package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.PostDTO;
import com.finalproject.finalproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PostController extends CustomExceptionHandler {

    @Autowired
    PostService postService;

    @PostMapping("/post/{id}")
    public ResponseEntity<PostDTO> createPost(@PathVariable int id,@RequestBody PostDTO postDTO){
        return ResponseEntity.ok(postService.createPost(id,postDTO));
    }
}
