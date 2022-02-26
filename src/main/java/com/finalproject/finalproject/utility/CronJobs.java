package com.finalproject.finalproject.utility;

import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.model.repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class CronJobs {

    @Autowired
    UserUtility util;
    @Autowired
    UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendEmailNotification(){
        List<User> users = userRepository.findAllByEmailEnabled(false);
        for (User user : users) {
            long daysLeft = ChronoUnit.DAYS.between(user.getCreationDate().plusDays(7),LocalDate.now());
            String token = RandomString.make(64);
            user.setEmailVerificationCode(token);
            user=userRepository.save(user);
            util.sendEmail(user.getEmail(),
                    "Verify your account",
                    "You have "+daysLeft+" to verify your account or we are going to delete it\n" +
                            "Please follow the link:"+UserUtility.VERIFY_URL.concat(token));
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void sendSMSNotification(){
        List<User> users = userRepository.findAllByPhoneEnabled(false);
        for (User user : users) {
            long daysLeft = ChronoUnit.DAYS.between(user.getCreationDate().plusDays(7),LocalDate.now());
            String token = RandomString.make(6);
            user.setPhoneVerificationCode(token);
            user=userRepository.save(user);
            util.sendSMS(user.getPhoneNumber(),
                    "You have "+daysLeft+"to verify your account. Your confirmation code for Maistor.bg is:"+token);
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteUsers(){
        List<User> users = userRepository.findAllForDelete(LocalDate.now().minusDays(7),false,false);
        for (User user : users) {
            userRepository.deleteById(user.getId());
            util.sendSMS(user.getPhoneNumber(),
                    "Unfortunately we had to delete your account in Maistor.bg " +
                            "\nFeel free to make a new one!");
            util.sendEmail(user.getEmail(),
                    "Your account has been deleted",
                    "Unfortunately we had to delete your account in Maistor.bg " +
                            "\nFeel free to make a new one!");
        }


    }
}
