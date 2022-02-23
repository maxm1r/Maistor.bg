package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferCreateDTO;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferDTO;
import com.finalproject.finalproject.model.dto.OfferEditDTO;
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
import java.util.List;
import java.util.stream.Collectors;

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
        if (createOffer.getDaysNeeded()<1 || createOffer.getDaysNeeded() >100){
            throw new BadRequestException("Invalid days needed");
        }
        if (createOffer.getHoursNeeded()<0 || createOffer.getHoursNeeded()>100){
            throw new BadRequestException("Invalid hours needed");
        }
        if (createOffer.getPricePerHour()<0){
            throw new BadRequestException("Invalid price per hour");
        }
        Post post = postRepository.findById(createOffer.getPostId()).orElseThrow(()-> new BadRequestException("Post not found"));
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        if (!user.isWorkman()){
            throw new BadRequestException("User is not workman");
        }
        if (post.getOwner() == user){
            throw new BadRequestException("User is post owner");
        }
        Offer offer = modelMapper.map(createOffer,Offer.class);
        offer.setOffer_date(LocalDateTime.now());
        offer.setPost(post);
        offer.setUser(user);
        offer = offerRepository.save(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }
    public OfferDTO editOffer(OfferEditDTO offerEditDTO, int id){
        if (offerEditDTO.getDaysNeeded()<1 || offerEditDTO.getDaysNeeded() >100){
            throw new BadRequestException("Invalid days needed");
        }
        if (offerEditDTO.getHoursNeeded()<0 || offerEditDTO.getHoursNeeded()>100){
            throw new BadRequestException("Invalid hours needed");
        }
        if (offerEditDTO.getPricePerHour()<0){
            throw new BadRequestException("Invalid price per hour");
        }
        Offer offer = offerRepository.findById(offerEditDTO.getId()).orElseThrow(()-> new BadRequestException("Offer not found"));
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        if (offer.getUser() != user){
            throw new UnauthorizedException("User isn't offer owner");
        }
        offer = offerRepository.save(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }

    public OfferDTO deleteById(int offerId,int userId){
        Offer offer = offerRepository.findById(offerId).orElseThrow(()->new BadRequestException("Offer not found"));
        User user = userRepository.findById(userId).orElseThrow(()-> new BadRequestException("User not found"));
        if (offer.getUser() != user){
            throw new UnauthorizedException("User is not post owner");
        }
        offerRepository.deleteById(offerId);
        return modelMapper.map(offer,OfferDTO.class);
    }

    public List<OfferDTO> getAllOffersForUser(int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        return offerRepository.findAllByUser(user).stream().map(offer -> modelMapper.map(offer,OfferDTO.class)).collect(Collectors.toList());
    }

    public List<OfferDTO> findAllByPost(int id){
        Post post = postRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        return offerRepository.findAllByPost(post).stream().map(offer -> modelMapper.map(offer,OfferDTO.class)).collect(Collectors.toList());
    }
}
