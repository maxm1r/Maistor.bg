package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
@Component
@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isWorkman;
    private String profilePicture;
    private String phoneNumber;
    private Set<CategoryDTO> categories;


    public UserWithoutPasswordDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.profilePicture = user.getProfilePicture();
        this.phoneNumber = user.getPhoneNumber();
        this.isWorkman = user.isWorkman();
        categories = new HashSet<>();
        Set<Category>  hashSet = user.getCategories();
        for (Category category : hashSet) {
            categories.add(new CategoryDTO(category));
        }
    }


}
