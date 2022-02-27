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
    public ResponseEntity<UserWithoutPasswordDTO> verifyEmail(@RequestParam("code") String code){
        return ResponseEntity.ok(userService.verifyEmail(code));
    }
    @PostMapping("/verify")
    public ResponseEntity<UserWithoutPasswordDTO> verifyPhone(@RequestBody UserVerifyDTO dto){
        return ResponseEntity.ok(userService.verifyPhone(dto.getVerificationCode()));
    }
    @PostMapping("/login")
    public ResponseEntity<UserWithCommentDTO> login(@RequestBody UserLoginRequestDTO user, HttpSession session, HttpServletRequest request){
        User u = userService.login(user.getEmail(),user.getPassword());
        sessionManager.loginUser(session,u.getId(),request);
        return ResponseEntity.ok( modelMapper.map(u, UserWithCommentDTO.class));
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
        return  ResponseEntity.ok(userService.getAllWorkmenForCategory(categoryName.getCategoryName()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> getUserByID(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserByID(id));
    }

    @GetMapping("user/all")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/all/workmen")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getAllWorkmen(){
        return ResponseEntity.ok(userService.getAllWorkmen());
    }

    @GetMapping("/user/rate/{id}")
    public ResponseEntity<UserWithRating> getUserRateById(@PathVariable int id){
        return ResponseEntity.ok(userService.getUserRateById(id));
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
