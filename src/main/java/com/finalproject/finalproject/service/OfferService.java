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
        if (createOffer.getDayNeeded() > 100){
            throw new BadRequestException("Invalid days needed");
        }
        if (createOffer.getDayNeeded()<1){
            throw new BadRequestException("Invalid days needed");
        }
        if (createOffer.getHoursNeeded()<0){
            throw new BadRequestException("Invalid hours needed");
        }
        if (createOffer.getHoursNeeded()<0){
            throw new BadRequestException("Invalid hours needed");
        }
        ArrayList asdf = new ArrayList();
        if (createOffer.getPricePerHour()<0){
            throw new BadRequestException("Invalid price per hour");
        }
        if (userRepository.findById(id).isEmpty()){
            throw new BadRequestException("User not found");
        }
        if (postRepository.findById(createOffer.getPostId()).isEmpty()){
            throw new BadRequestException("Post not found");
        }
        Post post = postRepository.findById(createOffer.getPostId()).get();
        User user = userRepository.findById(id).get();
        Offer offer = new Offer();
        offer.setOffer_date(LocalDateTime.now());
        offer.setDaysNeeded(createOffer.getDayNeeded());
        offer.setHoursNeeded(createOffer.getHoursNeeded());
        offer.setPricePerHour(createOffer.getPricePerHour());
        offer.setPost(post);
        offer.setUser(user);
        offer = offerRepository.save(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }
}
