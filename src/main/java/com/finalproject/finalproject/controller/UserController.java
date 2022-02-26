package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.dto.userDTOS.*;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@RestController
public class UserController extends CustomExceptionHandler {

    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SessionManager sessionManager;

    @PostMapping("/user")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO registerDTO,HttpServletRequest request){
        return ResponseEntity.ok(userService.register(registerDTO,request));
    }
    @GetMapping("/verify")
    public ResponseEntity<UserWithoutPasswordDTO> verify(@RequestParam("code") String code){
        return ResponseEntity.ok(userService.verify(code));
    }
    @PostMapping("/verify")
    public ResponseEntity<UserWithoutPasswordDTO> verifyBySMSCode(@RequestBody UserVerifyDTO dto){
        return ResponseEntity.ok(userService.verify(dto.getVerificationCode()));
    }
    @PostMapping("/login")
    public UserWithCommentDTO login(@RequestBody UserLoginRequestDTO user, HttpSession session, HttpServletRequest request){
        String email = user.getEmail();
        String password = user.getPassword();
        User u = userService.login(email, password);
        sessionManager.loginUser(session,u.getId(),request);
        UserWithCommentDTO dto = modelMapper.map(u, UserWithCommentDTO.class);
        return dto;
    }

    @PostMapping("/user/category")
    public ResponseEntity<UserWithoutPasswordDTO> addCategory(@RequestBody CategoryNameDTO categoryName, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(userService.addCategory((Integer) request.getSession().getAttribute(SessionManager.USER_ID),  categoryName.getCategoryName()));
    }

    @DeleteMapping("/user/category")
    public ResponseEntity<UserWithoutPasswordDTO> removeCategory(@RequestBody CategoryNameDTO categoryName, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(userService.removeCategory((Integer) request.getSession().getAttribute(SessionManager.USER_ID),categoryName.getCategoryName()));
    }

    @GetMapping("/user/category/{categoryName}")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getUsersForCategory(@PathVariable CategoryNameDTO categoryName){
        return  ResponseEntity.ok(userService.getAllUsersForCategory(categoryName.getCategoryName()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> getUserByID(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserByID(id));
    }

    @GetMapping("user/all")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/all/workmans")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllWorkmen(){
        return ResponseEntity.ok(userService.getAllWorkmen());
    }

    @GetMapping("/user/rate/{id}")
    public ResponseEntity<UserWithRating> getUserRateById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserRatebyId(id));
    }

    @PutMapping("/user")
    public ResponseEntity<EditUserDTO> editUser(@RequestBody EditUserDTO dto,HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(userService.editUser(dto, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session){
        sessionManager.logoutUser(session);
    }

    @PostMapping("/users/image")
    public String uploadProfileImage(@RequestParam(name = "file")MultipartFile file, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return  userService.uploadFile(file,request);
    }


}
