package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.OfferCreateDTO;
import com.finalproject.finalproject.model.dto.OfferDTO;
import com.finalproject.finalproject.model.pojo.Offer;
import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.OfferRepository;
import com.finalproject.finalproject.model.repositories.PostRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class OfferService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OfferRepository offerRepository;

    public OfferDTO createOffer(OfferCreateDTO createOffer,int id){
        if (createOffer.getDaysNeeded() > 100){
            throw new BadRequestException("Invalid days needed");
        }
        if (createOffer.getDaysNeeded()<1){
            throw new BadRequestException("Invalid days needed");
        }
        if (createOffer.getHoursNeeded()<0){
            throw new BadRequestException("Invalid hours needed");
        }
        if (createOffer.getHoursNeeded()<0){
            throw new BadRequestException("Invalid hours needed");
        }
        if (createOffer.getPricePerHour()<0){
            throw new BadRequestException("Invalid price per hour");
        }
        if (userRepository.findById(id).isEmpty()){
            throw new BadRequestException("User not found");
        }
        if (postRepository.findById(createOffer.getPostId()) == null){
            throw new BadRequestException("Post not found");
        }
        Post post = postRepository.findById(createOffer.getPostId());
        User user = userRepository.findById(id).get();
        if (!user.isWorkman()){
            throw new BadRequestException("Only workmans can make offers");
        }
        if (post.getOffers() == user){
            throw new BadRequestException("Cant make offer for your post");
        }
        Offer offer = new Offer();
        offer.setOffer_date(LocalDateTime.now());
        offer.setDaysNeeded(createOffer.getDaysNeeded());
        offer.setHoursNeeded(createOffer.getHoursNeeded());
        offer.setPricePerHour(createOffer.getPricePerHour());
        offer.setPost(post);
        offer.setUser(user);
        offer = offerRepository.save(offer);
        post.getOffers().add(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }
}
