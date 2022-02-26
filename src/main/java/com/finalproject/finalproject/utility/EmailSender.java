package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender extends Thread{
    JavaMailSender mailSender;
    User user;
    String url;
    @Autowired
    UserRepository userRepository;
    public EmailSender(User user, String url, JavaMailSender mailSender){
        this.user=user;
        this.url=url;
        this.mailSender=mailSender;
    }
    @Override
    public void run() {
        String verifyURL = url + "/verify?code=" + user.getVerificationCode();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setSubject("Verify your account");
        msg.setText("Follow this link "+ verifyURL);
        mailSender.send(msg);
    }
}
