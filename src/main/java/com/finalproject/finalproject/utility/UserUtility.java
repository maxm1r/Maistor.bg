package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;

public class UserUtility {

    public static boolean passMatch(UserRegisterRequestDTO dto){
        if (dto.getPassword().equals(dto.getConfirmPassword())){
            return true;
        }
        else return false;
    }

    public static boolean isEmailValid(UserRegisterRequestDTO registerDTO) {
        String pattern = "^(.+)@(.+)$";
        if (registerDTO.getEmail().matches(pattern)){
            return true;
        }
        else return false;
    }
}
