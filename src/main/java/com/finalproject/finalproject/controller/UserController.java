package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.dto.userDTOS.*;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.service.UserService;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@RestController
public class UserController extends CustomExceptionHandler {

    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;
    public static final String LOGGED = "logged";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_FROM = "logged_from";

    @PostMapping("/user")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO registerDTO){
        return ResponseEntity.ok(userService.register(registerDTO));
    }
    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO user, HttpSession session, HttpServletRequest request){
        String email = user.getEmail();
        String password = user.getPassword();
        User u = userService.login(email, password);
        session.setAttribute(LOGGED, true);
        session.setAttribute("logged_from",request.getRemoteAddr());
        session.setAttribute(USER_ID, u.getId());
        UserLoginResponseDTO dto = modelMapper.map(u, UserLoginResponseDTO.class);
        return dto;
    }

    @PostMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> addCategory(@PathVariable int id , @RequestBody CategoryNameDto categoryName){
        return ResponseEntity.ok(userService.addCategory(id,categoryName.getCategoryName()));
    }

    @DeleteMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> removeCategory(@PathVariable int id, @RequestBody CategoryNameDto categoryName){
        return ResponseEntity.ok(userService.removeCategory(id,categoryName.getCategoryName()));
    }

    @GetMapping("/user/category/{categoryName}")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getUsersForCategory(@PathVariable String categoryName){
        return  ResponseEntity.ok(userService.getAllUsersForCategory(categoryName));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> getUserByID(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserByID(id));
    }

    @GetMapping("user/all")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/allWorkMans")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllWorkmans(){
        return ResponseEntity.ok(userService.getAllWorkmans());
    }

    @GetMapping("/user/rate/{id}")
    public ResponseEntity<UserWithRating> getUserRateById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserRatebyId(id));
    }

    @PutMapping("/user")
    public ResponseEntity<EditUserDTO> editUser(@RequestBody EditUserDTO dto){
        return ResponseEntity.ok(userService.edinUser(dto));
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session){
        session.invalidate();
    }

    @SneakyThrows
    @PostMapping("/users/image")
    public String uploadProfileImage(@RequestParam(name = "file")MultipartFile file){
        String name = String.valueOf(System.nanoTime());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        File holder = new File("uploads" + File.separator + name + "." + ext);
        Files.copy(file.getInputStream(), Path.of(holder.toURI()));
        return holder.getName();
    }


}
