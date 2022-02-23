package com.finalproject.finalproject.service;

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
import org.springframework.stereotype.Service;

@Service
public class RateService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RateRepository rateRepository;
    @Autowired
    ModelMapper modelMapper;
    public RateResponseDTO createRate(int raterId, int ratedId, RateDTO rating) {
        if (rating.getRate() > 5 || rating.getRate() < 0){
            throw new BadRequestException("Bad value for rate");
        }
        if (rateRepository.findByUserIdAndRatedWorkmanId(raterId,ratedId).isPresent()){
            throw new BadRequestException("This user already rated this workman");
        }
        User rater = userRepository.findById(raterId).orElseThrow(()->new BadRequestException("Rater not found"));
        User rated = userRepository.findById(ratedId).orElseThrow(()->new BadRequestException("Rated user not found"));
        if (!rated.isWorkman()){
            throw new BadRequestException("Only workmen can be rated");
        }
        Rates rate = new Rates();
        rate.setRating(rating.getRate());
        rate.setRatedWorkman(rated);
        rate.setUser(rater);
        rate = rateRepository.save(rate);
        return modelMapper.map(rate,RateResponseDTO.class);
    }

    public RateResponseDTO editRate(int raterId, int ratedId, RateDTO rating) {
        if (rating.getRate() > 5 || rating.getRate() < 0){
            throw new BadRequestException("Bad value for rate");
        }
        Rates rates = rateRepository.findByUserIdAndRatedWorkmanId(raterId,ratedId).orElseThrow(()-> new BadRequestException("This user haven't rated this workman"));
        rates.setRating(rating.getRate());
        rates = rateRepository.save(rates);
        return modelMapper.map(rates,RateResponseDTO.class);
    }

    public RateResponseDTO unrate(int raterId, int ratedId){
        Rates rates = rateRepository.findByUserIdAndRatedWorkmanId(raterId,ratedId).orElseThrow(()->new BadRequestException("This user haven't rated this workman"));
        rateRepository.deleteById(rates.getId());
        return modelMapper.map(rates,RateResponseDTO.class);
    }
}
