package com.finalproject.finalproject.service;

import com.finalproject.finalproject.controller.UserController;
import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.RateDTO;
import com.finalproject.finalproject.model.dto.RateResponseDTO;
import com.finalproject.finalproject.model.pojo.Rates;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.RateRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class RateService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    ModelMapper modelMapper;
    public RateResponseDTO createRate(int raterId, int ratedId, RateDTO rating, HttpSession session, HttpServletRequest request) {
        UserUtility.validateLogin(session,request);
        if ((Integer) session.getAttribute(UserController.USER_ID) != raterId){
            throw new BadRequestException("Bad rater Id");
        }
        if (rating.getRate() > 5 || rating.getRate() < 0){
            throw new BadRequestException("Bad value for rate");
        }
        if (userRepository.findById(raterId).isEmpty()){
            throw new BadRequestException("No user with raterId");
        }
        if (userRepository.findById(ratedId).isEmpty()){
            throw new BadRequestException("No user with ratedId");
        }
        if (rateRepository.findByUserIdAndRatedWorkmanId(raterId,ratedId) !=null){
            throw new BadRequestException("This user already rated this workman");
        }
        User rater = userRepository.findById(raterId).get();
        User rated = userRepository.findById(ratedId).get();
        if (!rated.isWorkman()){
            throw new BadRequestException("Only workmans can be rated");
        }
        Rates rate = new Rates();
        rate.setRating(rating.getRate());
        rate.setRatedWorkman(rated);
        rate.setUser(rater);
        rate = rateRepository.save(rate);
        return modelMapper.map(rate,RateResponseDTO.class);
    }
}
