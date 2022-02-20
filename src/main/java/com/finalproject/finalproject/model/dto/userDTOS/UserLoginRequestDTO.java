package com.finalproject.finalproject.model.dto.userDTOS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequestDTO {

    private int id;
    private String email;
    private String password;
}
