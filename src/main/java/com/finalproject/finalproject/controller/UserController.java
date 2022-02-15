package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.UserRegisterResponseDTO;
import com.finalproject.finalproject.model.dto.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends AbstractController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO registerDTO){
        return userService.register(registerDTO);
    }

    @PostMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> addCategory(@PathVariable int id , @RequestBody CategoryDTO dto){
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.addCategory(id,dto);
        return ResponseEntity.ok(userWithoutPasswordDTO);
    }

    @DeleteMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> removeCategory(@PathVariable int id, @RequestBody CategoryDTO dto){
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.removeCategory(id ,dto);
        return ResponseEntity.ok(userWithoutPasswordDTO);
    }
}

