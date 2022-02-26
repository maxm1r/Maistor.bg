package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.pojo.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;

@AllArgsConstructor
public class VerificationSender extends Thread{


    String randomCode;
    User user;
    String url;
    JavaMailSender mailSender;
    @Override
    @SneakyThrows
    public void run() {

        Twilio.init(UserUtility.USER_CODE,UserUtility.PASS_CODE);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(user.getPhoneNumber()),
                        new com.twilio.type.PhoneNumber(UserUtility.SENT_FROM),
                        randomCode)
                .create();
        if (message.getStatus() != Message.Status.DELIVERED){
            Thread sendEmail = new EmailSender(user,url,mailSender);
            sendEmail.start();
            }
        else {
            Thread.sleep(3 * 60 * 10000);
            if (!user.isEnabled()){
                Thread sendEmail = new EmailSender(user,url,mailSender);
                sendEmail.start();
            }
        }
    }
}
