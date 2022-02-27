package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.userDTOS.UserRegisterRequestDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithRating;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.RateRepository;
import com.finalproject.finalproject.model.repositories.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.concurrent.Future;
@EnableAsync
@Service
@Data
@Configurable

public class UserUtility {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RateRepository rateRepository;
    public static final String USER_CODE = "ACcb31887879ffbf75cf705f58b2e67ca4";
    public static final String PASS_CODE = "5e0b739c8bf7711a06b2f39c4c508fdb";
    public static final String SENT_FROM = "+19124204643";
    public static final String nameRegex = "^[A-Za-z]\\w{2,29}$";
    public static final String VERIFY_URL = "http://localhost:8888/verify?code=";


    public static boolean passMatch(UserRegisterRequestDTO dto){
        if (dto.getPassword().equals(dto.getConfirmPassword())){
            return true;
        }
        else return false;
    }

    public static boolean isEmailValid(String registerDTO) {
        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        // RFC 5322
        return registerDTO.matches(pattern);
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

    public static String validateAndConfigureNumber(String phoneNumber) {
        if(phoneNumber.chars().anyMatch(Character::isLetter) ||
                (phoneNumber.length()!=10   &&  phoneNumber.length() != 13) ||
                (phoneNumber.length() == 10 && !phoneNumber.startsWith("0")) ||
                (phoneNumber.length() == 13 && !phoneNumber.startsWith("+359"))) {
            throw new BadRequestException("Invalid phone number");
        }
        if (phoneNumber.length() == 10){
            phoneNumber = phoneNumber.replaceFirst("0","+359");
        }
        return phoneNumber;
    }
    @Async
    public void sendConfirmationEmail(String email , String text){
        sendEmail(email,"Verify your account", "Please follow this link to activate your account: "+text);
    }
    @Async
    public void sendConfirmationSmS(String phoneNumber, String code){
        sendSMS(phoneNumber,"Your activation code for Maistor.bg is: "+code);
    }
    @Async
    public void sendSMS(String phoneNumber, String body){
        Twilio.init(UserUtility.USER_CODE,UserUtility.PASS_CODE);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),
                        new com.twilio.type.PhoneNumber(UserUtility.SENT_FROM),
                        body)
                .create();
    }
    @Async
    public void sendEmail(String email, String subject, String text){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    public UserWithRating setRate(int id, UserWithRating userWithRating) {
        double rate = rateRepository.getAverageRatingForUser(id).orElse(0.00);
        BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
        userWithRating.setRating(bd.doubleValue());
        return  userWithRating;
    }
}
