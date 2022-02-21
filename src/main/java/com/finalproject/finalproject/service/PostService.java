package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.*;
import com.finalproject.finalproject.model.repositories.*;
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    OfferRepository offerRepository;

    public PostResponseDTO createPost(int id, PostDTO postDTO) {
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        Category category=categoryRepository.findByCategoryName(postDTO.getCategoryName())  .orElseThrow(()-> new BadRequestException("Category not found"));
        City city = cityRepository.findByCityName(postDTO.getCityName()).orElseThrow(()-> new BadRequestException("City not found"));
        if ( postDTO.getDescription().isEmpty() ||  postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad description");
        }
        if (!UserUtility.userHasCategory(user,postDTO.getCategoryName())) {
            throw new BadRequestException("This user isn't qualified for that kind of work");
        }
        Post post = new Post();
        post.setCategory(category);
        post.setOwner(user);
        post.setDescription(postDTO.getDescription());
        post.setPostedDate(LocalDateTime.now());
        post.setCity(city);
        post = postRepository.save(post);
        return  modelMapper.map(post, PostResponseDTO.class);
    }
    public PostResponseDTO deletePost(int id, int userId){
        Post post = postRepository.findById(id).orElseThrow(()->new BadRequestException("Post not found"));
        if (post.getOwner().getId() != userId){
            throw new BadRequestException("User can't delete this post");
        }
        postRepository.deleteById(id);
        return modelMapper.map(post,PostResponseDTO.class);
    }

    public Set<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream().map(post -> modelMapper.map(post,PostResponseDTO.class)).collect(Collectors.toSet());
    }

    public PostResponseDTO editPost(PostDTO postDTO, int id) {
        Post post = postRepository.findById(id).orElseThrow(()->new BadRequestException("Post not found"));
        if (postDTO.getDescription().isEmpty() || postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad Description");
        }
        post.setDescription(postDTO.getDescription());
        post.setCategory(categoryRepository.findByCategoryName(postDTO.getCategoryName()).orElseThrow(()-> new BadRequestException("Category not found")));
        post.setCity(cityRepository.findByCityName(postDTO.getCityName()).orElseThrow(()-> new BadRequestException("City not found")));
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }
    public PostResponseDTO acceptOffer(int postId, int offerId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new BadRequestException("Post not found"));
        Offer offer = offerRepository.findById(offerId).orElseThrow(()-> new BadRequestException("Offer not found"));
        if (offer.getPost() != post){
            throw new BadRequestException("This offer doesn't belong to this post");
        }
        post.setAcceptedOffer(offer);
        post.setAssignedDate(LocalDateTime.now());
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }
}
