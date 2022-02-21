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
        if (postRepository.findById(id) == null){
            throw new BadRequestException("No post with that id");
        }
        Post post = postRepository.findById(id);
        postRepository.deleteById(id);
        PostResponseDTO dto = modelMapper.map(post,PostResponseDTO.class);
        return dto;
    }

    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDTO> dtos = posts.stream().map(post -> modelMapper.map(post,PostResponseDTO.class)).collect(Collectors.toList());
        return dtos;
    }

    public PostResponseDTO editPost(PostDTO postDTO, int id) {
        Post post = postRepository.findById(id);
        if (postDTO.getDescription().isEmpty() || postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad Description");
        }
        if (cityRepository.findByCityName(postDTO.getCityName()) == null){
         throw  new BadRequestException("Bad city name");
        }
        if (categoryRepository.findByCategoryName(postDTO.getCategoryName()) == null){
            throw new BadRequestException("Bad category name");
        }
        Category category = categoryRepository.findByCategoryName(postDTO.getCategoryName());
        City city = cityRepository.findByCityName(postDTO.getCityName());
        post.setDescription(postDTO.getDescription());
        post.setCategory(category);
        post.setCity(city);
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }
    @Transactional
    public PostResponseDTO acceptOffer(int postId, int offerId) {
        if (!postRepository.existsById(postId)){
            throw new BadRequestException("Bad post id");
        }
        if (!offerRepository.existsById(offerId)){
            throw new BadRequestException("Bad offer id");
        }
        Post post = postRepository.findById(postId);
        if (offerRepository.findById(offerId).isEmpty()){
            throw new BadRequestException("no such a offer");
        }
        Offer offer = offerRepository.findById(offerId).get();
        if (offer.getPost() != post){
            throw new BadRequestException("This offer is not for this post");
        }
        post.setAcceptedOffer(offer);
        post.setAssignedDate(LocalDateTime.now());
        post = postRepository.save(post);
        offer.setAcceptedBy(post);
        offer = offerRepository.save(offer);
        // post.getOffers().remove(offer); ?
        return modelMapper.map(post,PostResponseDTO.class);
    }
}
