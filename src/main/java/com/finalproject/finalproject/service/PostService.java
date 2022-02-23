package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostFilterDTO;
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
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        Category category=categoryRepository.findByCategoryName(postDTO.getCategoryName())  .orElseThrow(()-> new NotFoundException("Category not found"));
        City city = cityRepository.findByCityName(postDTO.getCityName()).orElseThrow(()-> new NotFoundException("City not found"));
        if ( postDTO.getDescription().isEmpty() ||  postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad description");
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
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("Post not found"));
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
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("Post not found"));
        if (postDTO.getDescription().isEmpty() || postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad Description");
        }
        post.setDescription(postDTO.getDescription());
        post.setCategory(categoryRepository.findByCategoryName(postDTO.getCategoryName()).orElseThrow(()-> new NotFoundException("Category not found")));
        post.setCity(cityRepository.findByCityName(postDTO.getCityName()).orElseThrow(()-> new NotFoundException("City not found")));
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }
    public PostResponseDTO acceptOffer(int postId, int offerId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new NotFoundException("Post not found"));
        Offer offer = offerRepository.findById(offerId).orElseThrow(()-> new NotFoundException("Offer not found"));
        if (offer.getPost() != post){
            throw new BadRequestException("This offer doesn't belong to this post");
        }
        post.setAcceptedOffer(offer);
        post.setAssignedDate(LocalDateTime.now());
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }

    public List<Post> getAllPostForUser(int  id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found!"));
        return postRepository.findAllByOwner(user);
    }

//    public Object getPostsByFilter(PostFilterDTO postFilterDTO) {
//        StringBuilder sql = new StringBuilder("SELECT * FROM post WHERE");
//        boolean firstTime = true;
//        if (postFilterDTO.getAssignedDateAfter() != null && postFilterDTO.getPostedDateAfter().isBefore(LocalDateTime.now().minusYears(1))) {
//            if (firstTime==true){
//                sql.append("WHERE(posted_date >"+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()) + ") ");
//                firstTime=false;
//            }
//            else {
//                sql.append("AND (posted_date >"+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()+ ") " ));
//            }
//        }
//
//        if (postFilterDTO.getAssignedDateAfter() != null && postFilterDTO.getPostedDateAfter().isBefore(LocalDateTime.now().minusYears(1))) {
//            if (firstTime==true){
//                sql.append("WHERE(posted_date >"+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()) + ") ");
//                firstTime=false;
//            }
//            else {
//                sql.append("AND (posted_date >"+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()+ ") " ));
//            }
//        }
//    }

}

