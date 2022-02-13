package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.UserRegisterResponseDTO;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserRegisterResponseDTO register(UserRegisterRequestDTO registerDTO) {

        if (!UserUtility.passMatch(registerDTO)){
            throw new BadRequestException("Passwords dont match");
        }
        if (!UserUtility.isEmailValid(registerDTO)){
            throw new BadRequestException("Invalid email");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()) != null){
            throw  new BadRequestException("Email already exist");
        }
        User user = new User(registerDTO);
        user = userRepository.save(user);
        return new UserRegisterResponseDTO(user);
    }
}
