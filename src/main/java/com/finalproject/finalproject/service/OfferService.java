package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
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
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
        if (createOffer.getDaysNeeded()<0 || createOffer.getDaysNeeded() >100){
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
        if(!UserUtility.userHasCategory(user,post.getCategory().getCategoryName())){
            throw new BadRequestException("User isn't qualified for this job");
        }
        Offer offer = new Offer();
        offer.setDaysNeeded(createOffer.getDaysNeeded());
        offer.setPricePerHour(createOffer.getPricePerHour());
        offer.setHoursNeeded(createOffer.getHoursNeeded());
        offer.setOffer_date(LocalDateTime.now());
        offer.setPost(post);
        offer.setUser(user);
        offer = offerRepository.save(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }
    public OfferDTO editOffer(OfferEditDTO offerEditDTO, int id){
        Offer offer = offerRepository.findById(offerEditDTO.getId()).orElseThrow(()-> new NotFoundException("Offer not found"));
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        if (offer.getUser() != user){
            throw new UnauthorizedException("User isn't offer owner");
        }
        if (offerEditDTO.getDaysNeeded()<0 || offerEditDTO.getDaysNeeded() >100){
            throw new BadRequestException("Invalid days needed");
        }
        if (offerEditDTO.getHoursNeeded()<0 || offerEditDTO.getHoursNeeded()>100){
            throw new BadRequestException("Invalid hours needed");
        }
        if (offerEditDTO.getPricePerHour()<0){
            throw new BadRequestException("Invalid price per hour");
        }
        if (offer.getPost().getAcceptedOffer() != null && offer.getPost().getAcceptedOffer() == offer){
            throw new BadRequestException("Accepted offers can't be edited");
        }
        offer.setHoursNeeded(offerEditDTO.getHoursNeeded());
        offer.setPricePerHour(offerEditDTO.getPricePerHour());
        offer.setDaysNeeded(offerEditDTO.getDaysNeeded());
        offer = offerRepository.save(offer);
        return modelMapper.map(offer,OfferDTO.class);
    }

    public OfferDTO deleteById(int offerId,int userId){
        Offer offer = offerRepository.findById(offerId).orElseThrow(()->new NotFoundException("Offer not found"));
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("User not found"));
        if (offer.getUser() != user){
            throw new UnauthorizedException("User is not offer owner");
        }
        if (offer.getPost().getAcceptedOffer() != null && offer.getPost().getAcceptedOffer() == offer){
            throw new BadRequestException("Accepted offers can't be deleted");
        }
        offerRepository.deleteById(offerId);
        return modelMapper.map(offer,OfferDTO.class);
    }

    public List<OfferDTO> getAllOffersForUser(int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        return offerRepository.findAllByUser(user).stream().map(offer -> modelMapper.map(offer,OfferDTO.class)).collect(Collectors.toList());
    }

    public List<OfferDTO> findAllByPost(int postId,int userId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new NotFoundException("Post not found"));
        if (post.getOwner().getId() != userId){
            throw new UnauthorizedException("User is not post owner");
        }
        return offerRepository.findAllByPost(post).stream().map(offer -> modelMapper.map(offer,OfferDTO.class)).collect(Collectors.toList());
    }
}
