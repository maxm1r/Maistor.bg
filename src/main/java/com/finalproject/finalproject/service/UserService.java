package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.UserRegisterResponseDTO;
import com.finalproject.finalproject.model.dto.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

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
        User user = new User(registerDTO);
        user = userRepository.save(user);
        return new UserRegisterResponseDTO(user);
    }

    public UserWithoutPasswordDTO addCategory(int id, CategoryDTO dto) {

        User user = userRepository.findById(id).orElse(null);
        Category category = categoryRepository.getByCategoryName(dto.getCategoryName());
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
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO removeCategory(int id, CategoryDTO dto){
        User user = userRepository.findById(id).orElse(null);
        Category category = categoryRepository.getByCategoryName(dto.getCategoryName());
        // TODO some validiations
        user.getCategories().remove(category);
        category.getUsers().remove(user);
        user = userRepository.save(user);
        category = categoryRepository.save(category);
        return new UserWithoutPasswordDTO(user);
    }
}
