package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.dto.userDTOS.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;

import java.util.Set;

public class UserUtility {


    public static boolean passMatch(UserRegisterRequestDTO dto){
        if (dto.getPassword().equals(dto.getConfirmPassword())){
            return true;
        }
        else return false;
    }

    public static boolean isEmailValid(UserRegisterRequestDTO registerDTO) {
        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        // RFC 5322
        if (registerDTO.getEmail().matches(pattern)){
            return true;
        }
        else return false;
    }
    public static boolean userHasCategory(User user, String categoryName){
        Set<Category> users = user.getCategories();
        for (Category category : users) {
            if (category.getCategoryName().equals(categoryName)){
                return true;
            }
        }
        return false;
    }

    public static boolean validPass(UserRegisterRequestDTO registerDTO) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (registerDTO.getPassword().matches(pattern)){
            return true;
        }
        else return false;
    }
}
