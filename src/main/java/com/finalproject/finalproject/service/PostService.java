package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.CategoryNameDTO;
import com.finalproject.finalproject.model.dto.CityNameDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostFilterDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostForOfferResponseDTO;
import com.finalproject.finalproject.model.dto.postDTOS.PostResponseDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.*;
import com.finalproject.finalproject.model.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
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
        post.setPostedDate(LocalDate.now());
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

    public PageImpl<PostForOfferResponseDTO> getAllPosts(Optional<Integer> page, Optional<String> sortBy) {
        List<PostForOfferResponseDTO> dtos = postRepository.findAll(PageRequest.of(page.orElse(0),5, Sort.Direction.DESC, sortBy.orElse("postedDate")))
                .stream().map(post -> modelMapper.map(post,PostForOfferResponseDTO.class)).collect(Collectors.toList());
        return  new PageImpl<>(dtos);
    }

    public PageImpl<PostForOfferResponseDTO> getPostsForCategory(String categoryName,Optional<Integer> page, Optional<String> sortBy){
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new NotFoundException("Category not found!"));
        List<PostForOfferResponseDTO> dtos = postRepository.findAllByCategory(category,PageRequest
                        .of(page.orElse(0),5, Sort.Direction.DESC, sortBy.orElse("postedDate")))
                .stream()
                .map(post -> modelMapper.map(post,PostForOfferResponseDTO.class)).collect(Collectors.toList());
        return new PageImpl<>(dtos);
    }

    public PostResponseDTO editPost(PostDTO postDTO, int id, int userId) {
        Post post = postRepository.findById(id).orElseThrow(()->new NotFoundException("Post not found"));
        if (postDTO.getDescription().isEmpty() || postDTO.getDescription().isBlank()){
            throw new BadRequestException("Bad Description");
        }
        if (userId != post.getOwner().getId()){
            throw new UnauthorizedException("User is not post owner");
        }
        post.setDescription(postDTO.getDescription());
        post.setCategory(categoryRepository.findByCategoryName(postDTO.getCategoryName()).orElseThrow(()-> new NotFoundException("Category not found")));
        post.setCity(cityRepository.findByCityName(postDTO.getCityName()).orElseThrow(()-> new NotFoundException("City not found")));
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }
    public PostResponseDTO acceptOffer(int postId, int offerId, int userId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new NotFoundException("Post not found"));
        Offer offer = offerRepository.findById(offerId).orElseThrow(()-> new NotFoundException("Offer not found"));
        if (offer.getPost() != post){
            throw new BadRequestException("This offer doesn't belong to this post");
        }
        if (offer.getUser().getId() != userId){
            throw new UnauthorizedException("User is not offer owner");
        }
        if (post.getAcceptedOffer() != null) {
            throw new BadRequestException("This post already has accepted offer");
        }
        post.setAcceptedOffer(offer);
        post.setAssignedDate(LocalDate.now());
        post = postRepository.save(post);
        return modelMapper.map(post,PostResponseDTO.class);
    }

    public List<Post> getAllPostForUser(int  id){
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found!"));
        return postRepository.findAllByOwner(user);
    }

    public List<PostResponseDTO> getPostsByFilter(PostFilterDTO postFilterDTO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM post \n");
        boolean firstTime = true;
        boolean isCategoryPresent = postFilterDTO.getCategoryList() != null && !postFilterDTO.getCategoryList().isEmpty();
        boolean isCityPresent = postFilterDTO.getCityList() != null && !postFilterDTO.getCityList().isEmpty();
        if (isCategoryPresent){
            sql.append("JOIN category AS c ON category_id = c.id \n");
        }
        if (isCityPresent){
            sql.append("JOIN city AS ct ON city_id = ct.id \n");
        }
        if (postFilterDTO.getPostedDateAfter() != null && !postFilterDTO.getPostedDateAfter().isBefore(LocalDate.now().minusYears(1))) {
            if (firstTime){
                sql.append("WHERE(  (posted_date >='"+ java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter())+ "') ");
                firstTime=false;
            }
            else {
                sql.append("AND (posted_date >='"+ java.sql.Date.valueOf(postFilterDTO.getPostedDateAfter())+ "') ");
            }
        }

        if (postFilterDTO.getPostedDateBefore() != null) {
            if (firstTime){
                sql.append("WHERE( (posted_date <='"+ java.sql.Date.valueOf(postFilterDTO.getPostedDateBefore()) + "') ");
                firstTime=false;
            }
            else {
                sql.append("AND (posted_date <='"+ java.sql.Date.valueOf(postFilterDTO.getPostedDateBefore()) + "') ");
            }
        }
        if (postFilterDTO.getCategoryList() != null && postFilterDTO.getCategoryList().size() > 0){
            isCategoryPresent=true;
            if (firstTime){
                sql.append("WHERE(c.category_name IN(");
                firstTime=false;
            }
            else{
                sql.append("AND (c.category_name IN(");
            }

            int lastCategoryCounter = 0;
            for (CategoryNameDTO dto: postFilterDTO.getCategoryList()) {
                    sql.append("\'"+dto.getCategoryName()+"\' ");
                    if (++lastCategoryCounter != postFilterDTO.getCategoryList().size()){     // not last element
                        sql.append(",");
                    }
                    else {
                        sql.append(")");
                    }
            }
            sql.append(") ");
        }
        if (postFilterDTO.getCityList() != null && postFilterDTO.getCityList().size() > 0){
            isCityPresent=true;
            if (firstTime){
                sql.append("WHERE(ct.city_name IN(");
                firstTime=false;
            }
            else{
                sql.append("AND (ct.city_name IN(");
            }

            int lastCityCounter =0;
            for (CityNameDTO dto : postFilterDTO.getCityList()) {
                sql.append("\'"+dto.getCityName()+"\' ");
                if (++lastCityCounter != postFilterDTO.getCityList().size()){     // not last element
                    sql.append(",");
                }
                else {
                    sql.append(")");
                }
            }
            sql.append(") ");
        }
        if (!firstTime){
            sql.append(") ");
        }
        System.out.println(String.valueOf(sql));
        List<PostResponseDTO> posts = jdbcTemplate.query(
                String.valueOf(sql),
                (rs, rowNum) -> {
                    PostResponseDTO post = new PostResponseDTO();
                    post.setId(rs.getInt("id"));
                    post.setCategoryName(rs.getString("category_name"));
                    post.setCityName(rs.getString("city_name"));
                    if (userRepository.findById(rs.getInt("owner_id")).isEmpty()) {
                        throw new BadRequestException("Owner of post with id " + rs.getInt("id") + "not found");
                    }
                    User user = userRepository.findById(rs.getInt("owner_id")).get();
                    post.setOwner(modelMapper.map(user, UserWithoutPasswordDTO.class));
                    post.setDescription(rs.getString("description"));
                    post.setPostedDate(rs.getDate("posted_date").toLocalDate());
                    return post;
                });

        return posts;
    }

}

