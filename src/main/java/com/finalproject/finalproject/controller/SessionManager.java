package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithCommentDTO;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.finalproject.finalproject.controller.UserController.LOGGED;
import static com.finalproject.finalproject.controller.UserController.LOGGED_FROM;

@Component
public class SessionManager {
    private static final String USER_ID = "USER_ID";
    private static final int ADMIN_ID = 2;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;


    public User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if(newSession || !logged || !sameIP){
            throw new UnauthorizedException("You have to login!");
        }
        int userId = (int) session.getAttribute(USER_ID);
        return  userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));


    }
    public void loginUser(HttpSession session, int id, HttpServletRequest request){
        session.setAttribute(LOGGED, true);
        session.setAttribute("logged_from",request.getRemoteAddr());
        session.setAttribute(USER_ID, id);;
        session.setMaxInactiveInterval(60*30);
    }
    public void verifyUser(HttpServletRequest request){
        getLoggedUser(request);

    }
    public void verifyAdmin(HttpServletRequest request){
        if (getLoggedUser(request).getRole().getId() != ADMIN_ID){
            throw new UnauthorizedException("Admin rights required!");
        }
    }
    public void logoutUser(HttpSession session){
        session.invalidate();
    }



}
