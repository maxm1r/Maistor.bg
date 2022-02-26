package com.finalproject.finalproject.service;

import com.finalproject.finalproject.controller.SessionManager;
import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.dto.userDTOS.*;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import com.finalproject.finalproject.model.repositories.RateRepository;
import com.finalproject.finalproject.model.repositories.RolesRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.finalproject.finalproject.utility.VerificationSender;
import com.finalproject.finalproject.utility.UserUtility;
import com.twilio.rest.api.v2010.account.Message;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RolesRepository roleRepository;


    @SneakyThrows
    public UserRegisterResponseDTO register(UserRegisterRequestDTO registerDTO, HttpServletRequest request) {

        if(registerDTO.getFirstName().isEmpty() || registerDTO.getFirstName().isBlank()){
            throw new BadRequestException("Bad first name");
        }
        if(registerDTO.getLastName().isEmpty() || registerDTO.getLastName().isBlank()){
            throw new BadRequestException("Bad last name");
        }
        if(registerDTO.getFirstName().length() > 20 ){
            throw new BadRequestException("Too long first name!");
        }
        if(registerDTO.getLastName().length() > 20 ){
            throw new BadRequestException("Too long last name!");
        }
        if(registerDTO.getPhoneNumber().isEmpty() || registerDTO.getPhoneNumber().length() > 14){
            throw new BadRequestException("Bad phone number");
        }
        if (!UserUtility.passMatch(registerDTO)){
            throw new BadRequestException("Passwords don't match");
        }
        if (!UserUtility.isEmailValid(registerDTO)){
            throw new BadRequestException("Invalid email");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()) != null){
            throw  new BadRequestException("User already exist");
        }
        if (userRepository.findUserByPhoneNumber(registerDTO.getPhoneNumber()) != null){
            throw new BadRequestException("The phone is already registered!");
        }
        if (!UserUtility.validPass(registerDTO)){
            throw new BadRequestException("Password doesn't match the requirements");
        }
        User user = modelMapper.map(registerDTO,User.class);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(roleRepository.getById(SessionManager.USER_ROLE_ID));
        user.setEnabled(false);
        String randomCode = RandomString.make(8);
        user.setVerificationCode(randomCode);
        user = userRepository.save(user);
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(),"");
        Thread verificationCode = new VerificationSender(randomCode,
                user,
                siteURL,
                mailSender);
        verificationCode.start();

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
        if(!u.isEnabled()){
            throw new UnauthorizedException("User not verified");
        }
        if(!passwordEncoder.matches(password, u.getPassword())){
            throw new UnauthorizedException("Wrong credentials");
        }
        return u;
    }

    public UserWithoutPasswordDTO addCategory(int id, String categoryName) {
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new BadRequestException("Category not found"));
        if (!user.isWorkman()){
            throw new BadRequestException("User isn't a workman");
        }
        if (user.getCategories().contains(category)){
            throw new BadRequestException("User already have this category");
        }
        user.getCategories().add(category);
        user = userRepository.save(user);
        category.getUsers().add(user);
        category = categoryRepository.save(category);
        return  modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    public UserWithoutPasswordDTO removeCategory(int id, String categoryName){
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new BadRequestException("Category not found"));
        if (!user.getCategories().contains(category)){
            throw new BadRequestException("User doesn't have this category");
        }
        user.getCategories().remove(category);
        user = userRepository.save(user);
        category.getUsers().remove(user);
        category = categoryRepository.save(category);
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }

    public Set<UserWithoutPasswordDTO> getAllUsersForCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new BadRequestException("Category not found"));
        return userRepository.getAllByCategoriesContaining(category)
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public EditUserDTO edinUser(EditUserDTO dto, int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        String password = user.getPassword();
        user = modelMapper.map(dto,User.class);
        user.setPassword(password);
        user = userRepository.save(user);
        return modelMapper.map(user,EditUserDTO.class);
    }

    public UserWithoutPasswordDTO getUserByID(int id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
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
            throw new NotFoundException("User not found");
        }  
        User user = userRepository.findById(id).get();
        UserWithRating userWithRating = modelMapper.map(user,UserWithRating.class);
        double rate = rateRepository.getAverageRatingForUser(id).orElse(0.00);
        BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
        userWithRating.setRating(bd.doubleValue());
        return userWithRating;
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file , HttpServletRequest request){
        int loggedUserId = (int) request.getSession().getAttribute(SessionManager.USER_ID);
        String name = String.valueOf(System.nanoTime());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        File holder = new File("uploads" + File.separator + name + "." + ext);
        Files.copy(file.getInputStream(), Path.of(holder.toURI()));
        User u = userRepository.findById(loggedUserId).orElseThrow(()-> new NotFoundException("User not found!"));;
        u.setProfilePicture(name);
        userRepository.save(u);
        return holder.getName();
    }

    public UserWithoutPasswordDTO verify(String code) {
        User user = userRepository.findByVerificationCode(code).orElseThrow(()-> new BadRequestException("User not found"));
        user.setVerificationCode(null);
        user.setEnabled(true);
        user =userRepository.save(user);
        return modelMapper.map(user,UserWithoutPasswordDTO.class);
    }
}
