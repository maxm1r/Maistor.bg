package com.finalproject.finalproject.service;

import com.finalproject.finalproject.controller.UserController;
import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.dto.userDTOS.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserRegisterResponseDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithRating;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import com.finalproject.finalproject.model.repositories.RateRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.UserUtility;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserRegisterResponseDTO register(UserRegisterRequestDTO registerDTO) {

        if (!UserUtility.passMatch(registerDTO)){
            throw new BadRequestException("Passwords don't match");
        }
        if (!UserUtility.isEmailValid(registerDTO)){
            throw new BadRequestException("Invalid email");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()) != null){
            throw  new BadRequestException("Email already exist");
        }
        if(registerDTO.getFirstName().isEmpty()){
            throw new BadRequestException("Enter your first name!");
        }
        if(registerDTO.getLastName().isEmpty()){
            throw new BadRequestException("Enter your last name!");
        }
        if(registerDTO.getPhoneNumber().isEmpty()){
            throw new BadRequestException("Enter your phone number!");
        }
        if (userRepository.findUserByPhoneNumber(registerDTO.getPhoneNumber()) != null){
            throw new BadRequestException("The phone is already registered!");
        }
        // TODO real email verification , real password verification, phone verification
        User user = modelMapper.map(registerDTO,User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user = userRepository.save(user);
        return modelMapper.map(user,UserRegisterResponseDTO.class);
    }

    public User login(String email, String password){
        if(email == null || email.isBlank()){
            throw new BadRequestException("Email is mandatory");
        }
        if(password == null || password.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        User u = userRepository.findByEmail(email);
        if(u == null){
            throw new UnauthorizedException("Wrong credentials");
        }
        if(!passwordEncoder.matches(password, u.getPassword())){
            throw new UnauthorizedException("Wrong credentials");
        }
        return u;
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
        return  modelMapper.map(user, UserWithoutPasswordDTO.class);
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
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
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
            throw new NotFoundException("There is no user with id: "+id);
        }
        return modelMapper.map(user,UserWithoutPasswordDTO.class);
    }

    public Set<UserWithoutPasswordDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class)).collect(Collectors.toSet());
    }

    public Set<UserWithoutPasswordDTO> getAllWorkmen() {
        return userRepository.findAllWorkmans()
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public UserWithRating getUserRatebyId(int id) {
        if (userRepository.findById(id).isEmpty()){
            throw new BadRequestException("Bad user id");
        }  
        User user = userRepository.findById(id).get();
        UserWithRating userWithRating = modelMapper.map(user,UserWithRating.class);
        double rate = rateRepository.getAverageRatingForUser(id);
        BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
        userWithRating.setRating(bd.doubleValue());
        return userWithRating;
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file , HttpServletRequest request){
        UserUtility.validateLogin(request.getSession(),request);
        int loggedUserId = (int) request.getSession().getAttribute(UserController.USER_ID);
        String name = String.valueOf(System.nanoTime());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        File holder = new File("uploads" + File.separator + name + "." + ext);
        Files.copy(file.getInputStream(), Path.of(holder.toURI()));
        User u = userRepository.findById(loggedUserId).orElseThrow(()-> new NotFoundException("User not found!"));;
        u.setProfilePicture(name);
        userRepository.save(u);
        return holder.getName();
    }
}