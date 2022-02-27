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
import com.finalproject.finalproject.utility.UserUtility;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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
    @Autowired
    private UserUtility util;


    @SneakyThrows
    public UserRegisterResponseDTO register(UserRegisterRequestDTO registerDTO, HttpServletRequest request) {

        if(!registerDTO.getFirstName().matches(UserUtility.nameRegex)){
            throw new BadRequestException("Bad first name");
        }
        if(!registerDTO.getLastName().matches(UserUtility.nameRegex)){
            throw new BadRequestException("Bad last name");
        }
        if (!UserUtility.passMatch(registerDTO)){
            throw new BadRequestException("Passwords don't match");
        }
        if (!UserUtility.isEmailValid(registerDTO.getEmail())){
            throw new BadRequestException("Invalid email");
        }
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()){
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
        user.setPhoneNumber(UserUtility.validateAndConfigureNumber(user.getPhoneNumber()));
        user.setRole(roleRepository.getById(SessionManager.USER_ROLE_ID));
        user.setEmailEnabled(false);
        user.setPhoneEnabled(false);
        String emailCode = RandomString.make(64);
        String SMSCode = RandomString.make(6);
        user.setEmailVerificationCode(emailCode);
        user.setPhoneVerificationCode(SMSCode);
        user.setCreationDate(LocalDate.now());
        user = userRepository.save(user);
        String emailBodyLink = UserUtility.VERIFY_URL.concat(user.getEmailVerificationCode());
        util.sendConfirmationEmail(user.getEmail(),emailBodyLink);
        util.sendConfirmationSmS(user.getPhoneNumber(),SMSCode);
        login(user.getEmail(),user.getPassword());
        return modelMapper.map(user,UserRegisterResponseDTO.class);
    }

    public User login(String email, String password){
        if(email == null || email.isBlank()){
            throw new BadRequestException("Email is mandatory");
        }
        if(password == null || password.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        User u = userRepository.findByEmail(email).orElseThrow(()-> new UnauthorizedException("Wrong credentials"));
        if(!passwordEncoder.matches(password, u.getPassword())){
            throw new UnauthorizedException("Wrong credentials");
        }
        return u;
    }

    @Transactional
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

    @Transactional
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

    public Set<UserWithoutPasswordDTO> getAllWorkmenForCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new NotFoundException("Category not found"));
        return userRepository.getAllByCategoriesContaining(category)
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public EditUserDTO editUser(EditUserDTO dto, int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
        if (!dto.getFirstName().matches(UserUtility.nameRegex)){
            throw new BadRequestException("Bad first name");
        }
        if (!dto.getLastName().matches(UserUtility.nameRegex)){
            throw new BadRequestException("Bad last name");
        }
        dto.setPhoneNumber(UserUtility.validateAndConfigureNumber(dto.getPhoneNumber()));
        if (!UserUtility.isEmailValid(dto.getEmail())){
            throw new BadRequestException("Invalid email");
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPhoneEnabled(false);
        user.setEmail(dto.getEmail());
        user.setEmailEnabled(false);
        String SMScode = RandomString.make(6);
        String emailCode = RandomString.make(64);
        user.setEmailVerificationCode(emailCode);
        user.setPhoneVerificationCode(SMScode);
        util.sendConfirmationEmail(user.getEmail(),emailCode);
        util.sendConfirmationSmS(user.getPhoneNumber(),emailCode);
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
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public Set<UserWithoutPasswordDTO> getAllWorkmen() {
        return userRepository.findAllWorkmans()
                .stream()
                .map(user -> modelMapper.map(user,UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
    }

    public UserWithRating getUserRateById(int id) {
        User user = userRepository.findById(id).orElseThrow(()-> new BadRequestException("User not found"));
        UserWithRating userWithRating = modelMapper.map(user,UserWithRating.class);
        return util.setRate(id,userWithRating);
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file , HttpServletRequest request){
        int loggedUserId = (int) request.getSession().getAttribute(SessionManager.USER_ID);
        String name = String.valueOf(System.nanoTime());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        File holder = new File("uploads" + File.separator + name + "." + ext);
        Files.copy(file.getInputStream(), Path.of(holder.toURI()));
        Magic magic = new Magic();
        MagicMatch match = Magic.getMagicMatch(holder,false);
        if ( !match.getMimeType().equalsIgnoreCase("image/png") && !match.getMimeType().equalsIgnoreCase("image/jpeg")){
                throw new BadRequestException("You can only upload jpg and png files!");
        }
        User u = userRepository.findById(loggedUserId).orElseThrow(()-> new NotFoundException("User not found!"));;
        u.setProfilePicture(name+"." + ext);
        userRepository.save(u);
        return holder.getName();
    }

    public UserWithoutPasswordDTO verifyEmail(String code) {
        User user = userRepository.findByEmailVerificationCode(code).orElseThrow(()-> new BadRequestException("User not found"));
        user.setEmailVerificationCode(null);
        user.setEmailEnabled(true);
        user =userRepository.save(user);
        return modelMapper.map(user,UserWithoutPasswordDTO.class);
    }

    public UserWithoutPasswordDTO verifyPhone(String verificationCode) {
        User user = userRepository.findByPhoneVerificationCode(verificationCode).orElseThrow(()-> new BadRequestException("User not found"));
        user.setPhoneVerificationCode(null);
        user.setPhoneEnabled(true);
        user = userRepository.save(user);
        return modelMapper.map(user,UserWithoutPasswordDTO.class);
    }
}
