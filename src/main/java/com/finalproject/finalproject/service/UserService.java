package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.UserUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;


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
        // TODO real email verification , real password verification, phone verification
        User user = modelMapper.map(registerDTO,User.class);
        user = userRepository.save(user);
        return modelMapper.map(user,UserRegisterResponseDTO.class);
    }
    @Transactional
    public User addCategory(int id, String categoryName) {
        User user = userRepository.findById(id).orElse(null);
        Category category = categoryRepository.getByCategoryName(categoryName);
        if (user == null){
            throw new BadRequestException("There is no user with that id");
        }
        if (!user.isWorkman()){
            throw new BadRequestException("Only workmans can have categories!");
        }
        if (category == null){
            throw  new BadRequestException("There is no category with that name");
        }
        if (user.getCategories().contains(category)){
            throw new BadRequestException("User already have this category");
        }
        // TODO some validations
        user.getCategories().add(category);
        category.getUsers().add(user);
        user = userRepository.save(user);
        category = categoryRepository.save(category);
        return  user;
    }

    @Transactional
    public User removeCategory(int id, String categoryName){
        User user = userRepository.findById(id).orElse(null);
        Category category = categoryRepository.getByCategoryName(categoryName);
        // TODO some validiations
        user.getCategories().remove(category);
        category.getUsers().remove(user);
        user = userRepository.save(user);
        category = categoryRepository.save(category);
        return user;
    }

    public Set<User> getAllUsersForCategory(String categoryName) {
        Category category = categoryRepository.getByCategoryName(categoryName);
        Set<User> users = userRepository.getAllByCategoriesContaining(category);

        return users;
    }

    public User edinUser(EditUserDTO dto) {
        User user = userRepository.getById(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePicture(dto.getProfilePicture());
        user = userRepository.save(user);
        return user;
    }
}