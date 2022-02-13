package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Set;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterResponseDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isWorkman;
    private String phoneNumber;
    private Set<Category> categories;

    public UserRegisterResponseDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.isWorkman = user.isWorkman();
        this.phoneNumber = user.getPhoneNumber();
        this.categories = user.getCategories();
    }
}

