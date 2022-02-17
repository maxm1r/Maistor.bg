package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.PostDTO;
import com.finalproject.finalproject.model.dto.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.City;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import com.finalproject.finalproject.model.repositories.CityRepository;
import com.finalproject.finalproject.model.repositories.PostRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository ;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CityRepository cityRepository;

    public PostResponseDTO createPost(int id, PostDTO postDTO) {

        User user = userRepository.findById(id).orElse(null);
        Post post = new Post();
        post.setOwner(user);
        post.setDescription(postDTO.getDescription());
        if (categoryRepository.existsByCategoryName(postDTO.getCategoryName())) {
            post.setCategory(categoryRepository.getByCategoryName(postDTO.getCategoryName()));
        }
        City city = cityRepository.findByCityName(postDTO.getCityName());
        post.setPostedDate(LocalDateTime.now());
        post.setCity(city);
        post = postRepository.save(post);
        user.getPosts().add(post);
        userRepository.save(user);
        return  modelMapper.map(post, PostResponseDTO.class);
    }

    public PostDTO deletePost(int id){
        Post post = postRepository.deleteById(id);
        PostDTO dto = modelMapper.map(post,PostDTO.class);
        return dto;
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> dtos = posts.stream().map(post -> modelMapper.map(post,PostDTO.class)).collect(Collectors.toList());
        return dtos;
    }
}
