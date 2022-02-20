package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

import static com.finalproject.finalproject.controller.UserController.LOGGED;
import static com.finalproject.finalproject.controller.UserController.LOGGED_FROM;

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

    public static void validateLogin(HttpSession session, HttpServletRequest request) {
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if(newSession || !logged || !sameIP){
            throw new UnauthorizedException("You have to login!");
        }
    }
}
