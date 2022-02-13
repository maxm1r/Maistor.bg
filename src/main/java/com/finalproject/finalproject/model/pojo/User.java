package com.finalproject.finalproject.model.pojo;

import com.finalproject.finalproject.model.dto.UserRegisterRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isWorkman;
    private String profilePicture;
    private String phoneNumber;
    @ManyToMany(mappedBy = "users")
    private Set<Category> categories;

    public User(UserRegisterRequestDTO dto){
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.phoneNumber = dto.getPhoneNumber();
        this.isWorkman = dto.isWorkman();
        if (this.isWorkman && dto.getCategories() != null) {
            this.categories.addAll(dto.getCategories());
        }
    }


}
