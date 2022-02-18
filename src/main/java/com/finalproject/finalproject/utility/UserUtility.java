package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
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
        String pattern = "^(.+)@(.+)$";
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
}
