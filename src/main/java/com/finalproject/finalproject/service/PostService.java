package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostFilterDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.model.pojo.*;
import com.finalproject.finalproject.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
    @Autowired
    JdbcTemplate jdbcTemplate;

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
        //TODO check if im the owner
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
        //TODO check if im the owner
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

    public List<PostResponseDTO> getPostsByFilter(PostFilterDTO postFilterDTO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM post ");
        boolean firstTime = true;
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm");
        if (postFilterDTO.getPostedDateAfter() != null && !postFilterDTO.getPostedDateAfter().isBefore(LocalDateTime.now().minusYears(1))) {
            if (firstTime){
                sql.append("WHERE(posted_date >="+ java.sql.Date.valueOf(dt.format(postFilterDTO.getPostedDateAfter()))+ ") ");
                firstTime=false;
            }
            else {
                sql.append("AND (posted_date >="+java.sql.Date.valueOf(dt.format(postFilterDTO.getPostedDateAfter()))+ ") ");
            }
        }

        if (postFilterDTO.getPostedDateBefore() != null) {
            if (firstTime){
                sql.append("WHERE(posted_date >="+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()) + ") ");
                firstTime=false;
            }
            else {
                sql.append("AND (posted_date >="+java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter().toLocalDate()+ ") " ));
            }
        }
        List<Post> posts = jdbcTemplate.query(
                String.valueOf(sql),
                new BeanPropertyRowMapper(Post.class));
        System.out.println(String.valueOf(sql));
        return posts.stream().map(post -> modelMapper.map(post,PostResponseDTO.class)).collect(Collectors.toList());
    }

}

