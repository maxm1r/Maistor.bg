package com.finalproject.finalproject.model.dto.userDTOS;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finalproject.finalproject.model.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserRegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    @JsonProperty
    private boolean isWorkman;
    private String phoneNumber;

}
