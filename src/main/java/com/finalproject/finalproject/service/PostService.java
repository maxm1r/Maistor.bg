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
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public PostResponseDTO createPost(int id, PostDTO postDTO) {

        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            throw new BadRequestException("No user with that id");
        }
        if ( postDTO.getDescription() == null ||  postDTO.getDescription().isBlank()){
            throw new BadRequestException("No description");
        }
        Post post = new Post();
        if (!UserUtility.userHasCategory(user,postDTO.getCategoryName())) {
            throw new BadRequestException("This user isn't qualified for that kind of work");
        }
        post.setCategory(categoryRepository.findByCategoryName(postDTO.getCategoryName()));
        post.setOwner(user);
        post.setDescription(postDTO.getDescription());
        if (cityRepository.findByCityName(postDTO.getCityName()) == null){
            throw new BadRequestException("No city with that name");
        }
        post.setPostedDate(LocalDateTime.now());
        post.setCity(cityRepository.findByCityName(postDTO.getCityName()));
        post = postRepository.save(post);
        return  modelMapper.map(post, PostResponseDTO.class);
    }

    public PostResponseDTO deletePost(int id){
        if (!postRepository.findById(id).isPresent()){
            throw new BadRequestException("No post with that id");
        }
        Post post = postRepository.deleteById(id);
        PostResponseDTO dto = modelMapper.map(post,PostResponseDTO.class);
        return dto;
    }

    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> dtos = posts.stream().map(post -> modelMapper.map(post,PostResponseDTO.class)).collect(Collectors.toList());
        return dtos;
    }
}
