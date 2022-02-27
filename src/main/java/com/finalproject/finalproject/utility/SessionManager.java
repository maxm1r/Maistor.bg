package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.ForbiddenException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionManager {
    public static final String LOGGED = "logged";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_FROM = "logged_from";
    public static final int ADMIN_ROLE_ID = 2;
    public static final int USER_ROLE_ID = 1;

    @Autowired
    private UserRepository userRepository;


    public User getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean)session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if(newSession || !logged || !sameIP){
            throw new UnauthorizedException("You have to login!");
        }
        int userId = (int) session.getAttribute(USER_ID);
        User user =userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found"));
        if (!user.isEmailEnabled() || !user.isPhoneEnabled()){
            throw new BadRequestException("User not verified");
        }
        return user;
    }
    public void loginUser(int id, HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED, true);
        session.setAttribute("logged_from",request.getRemoteAddr());
        session.setAttribute(USER_ID, id);
        session.setMaxInactiveInterval(60*30);
    }
    public void verifyUser(HttpServletRequest request){
        getLoggedUser(request);

    }
    public void verifyAdmin(HttpServletRequest request){
        if (getLoggedUser(request).getRole().getId() != ADMIN_ROLE_ID){
            throw new ForbiddenException("Admin rights required!");
        }
    }
    public void logoutUser(HttpSession session){
        session.invalidate();
    }



}
