package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.*;
import com.finalproject.finalproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController extends CustomExceptionHandler {

    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/user")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO registerDTO){
        return ResponseEntity.ok(userService.register(registerDTO));
    }

    @PostMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> addCategory(@PathVariable int id , @RequestBody String categoryName){
        return ResponseEntity.ok(userService.addCategory(id,categoryName));
    }

    @DeleteMapping("/user/category/{id}")
    public ResponseEntity<UserWithoutPasswordDTO> removeCategory(@PathVariable int id, @RequestBody String categoryName){
        return ResponseEntity.ok(userService.removeCategory(id,categoryName));
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

    @PutMapping("/user")
    public ResponseEntity<EditUserDTO> editUser(@RequestBody EditUserDTO dto){
        return ResponseEntity.ok(userService.edinUser(dto));
    }

}

