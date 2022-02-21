package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Category;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EditUserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
