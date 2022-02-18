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

import java.util.*;
import java.util.stream.Collectors;

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
    public UserWithoutPasswordDTO addCategory(int id, String categoryName) {
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
        return  modelMapper.map(category, UserWithoutPasswordDTO.class);
    }

    @Transactional
    public UserWithoutPasswordDTO removeCategory(int id, String categoryName){
        User user = userRepository.findById(id).orElse(null);
        Category category = categoryRepository.getByCategoryName(categoryName);
        // TODO some validiations
        user.getCategories().remove(category);
        category.getUsers().remove(user);
        user = userRepository.save(user);
        category = categoryRepository.save(category);
        return modelMapper.map(category, UserWithoutPasswordDTO.class);
    }

    public Set<UserWithoutPasswordDTO> getAllUsersForCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        return userRepository.getAllByCategoriesContaining(category)
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public EditUserDTO edinUser(EditUserDTO dto) {
        User user = userRepository.getById(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setProfilePicture(dto.getProfilePicture());
        user = userRepository.save(user);
        return modelMapper.map(user,EditUserDTO.class);
    }

    public UserWithoutPasswordDTO getUserByID(int id) {
        User user = userRepository.getById(id);
        if (user == null){
            throw new BadRequestException("There is no user with id:"+id);
        }
        return modelMapper.map(user,UserWithoutPasswordDTO.class);
    }

    public Set<UserWithoutPasswordDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class)).collect(Collectors.toSet());
    }

    public Set<UserWithoutPasswordDTO> getAllWorkmans() {
        return userRepository.findAllWorkmans()
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }
}