package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostFilterDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostForOfferResponseDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.service.PostService;
import com.finalproject.finalproject.utility.SessionManager;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@RestController
public class PostController extends CustomExceptionHandler {

    @Autowired
    PostService postService;
    @Autowired
    SessionManager sessionManager;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDTO> createPost(HttpServletRequest request, @RequestBody PostDTO postDTO){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(postService.createPost((Integer) request.getSession().getAttribute(SessionManager.USER_ID),postDTO));
    }
    @DeleteMapping("/posts")
    public ResponseEntity<PostResponseDTO> deletePost(@RequestParam(name = "id") int id, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(postService.deletePost(id, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }
    @GetMapping("/posts")
    public PageImpl<PostForOfferResponseDTO> getAllPosts(@RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy,HttpServletRequest request){
        return postService.getAllPosts(page,sortBy);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDTO> editPost(@RequestBody PostDTO postDTO,@PathVariable int id,HttpServletRequest request ){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(postService.editPost(postDTO,id, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }
    @PostMapping("/posts/offer")
    public ResponseEntity<PostResponseDTO> acceptOffer(@RequestParam(name = "postId") int postId, @RequestParam(name = "offerId") int offerId, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(postService.acceptOffer(postId,offerId, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponseDTO>> getPostsForUser(@PathVariable int id, HttpServletRequest request){
        List<Post> posts = postService.getAllPostForUser(id);
        List<PostResponseDTO> dto = mapper.map(posts, new TypeToken<List<PostResponseDTO>>() {}.getType());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("posts/filter")
    public ResponseEntity<List<PostResponseDTO>> getPostsByFilter(@RequestBody PostFilterDTO postFilterDTO, HttpServletRequest request){
        return ResponseEntity.ok(postService.getPostsByFilter(postFilterDTO));
    }

    @GetMapping("/posts/{categoryName}")
    public PageImpl<PostForOfferResponseDTO> getPostsForCategory(@PathVariable String categoryName,@RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy, HttpServletRequest request){
        return postService.getPostsForCategory(categoryName,page,sortBy);
    }
}
