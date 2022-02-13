package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.UserRegisterResponseDTO;
import com.finalproject.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends AbstractController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public UserRegisterResponseDTO register(@RequestBody UserRegisterRequestDTO registerDTO){
        return userService.register(registerDTO);
    }

}
