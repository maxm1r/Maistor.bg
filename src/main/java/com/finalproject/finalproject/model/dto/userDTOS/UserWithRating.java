package com.finalproject.finalproject.model.dto.userDTOS;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserWithRating {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isWorkman;
    private String profilePicture;
    private String phoneNumber;
    private Set<CategoryDTO> categories = new HashSet<>();
    private double rating;
}
