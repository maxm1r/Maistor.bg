package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController extends AbstractController {

    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/user")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO registerDTO){
        UserRegisterResponseDTO responseDTO = userService.register(registerDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> addCategory(@PathVariable int id , @RequestBody CategoryDTO dto){
        User user = userService.addCategory(id,dto.getCategoryName());
        UserWithoutPasswordDTO userWithoutPasswordDTO = modelMapper.map(user,UserWithoutPasswordDTO.class);
        return ResponseEntity.ok(userWithoutPasswordDTO);
    }

    @DeleteMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> removeCategory(@PathVariable int id, @RequestBody CategoryDTO dto){
        User user  = userService.removeCategory(id,dto.getCategoryName());
        UserWithoutPasswordDTO userWithoutPasswordDTO = modelMapper.map(user,UserWithoutPasswordDTO.class);
        return ResponseEntity.ok(userWithoutPasswordDTO);
    }

    @GetMapping("/user/category/{categoryName}")
    public ResponseEntity<Set<UserWithoutPasswordDTO>> getUsersForCategory(@PathVariable String categoryName){
        Set<UserWithoutPasswordDTO> usersForReturn = userService.getAllUsersForCategory(categoryName)
                .stream().map(element -> modelMapper.map(element, UserWithoutPasswordDTO.class))
                .collect(Collectors.toSet());
        return  ResponseEntity.ok(usersForReturn);
    }

    @PutMapping("/user")
    public ResponseEntity<EditUserDTO> editUser(@RequestBody EditUserDTO dto){
        User user = userService.edinUser(dto);
        EditUserDTO dtoForReturn = modelMapper.map(user,EditUserDTO.class);
        return ResponseEntity.ok(dtoForReturn);
    }

}

