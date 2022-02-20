package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RateResponseDTO {
    private int id;
    private UserWithoutPasswordDTO user;
    private UserWithoutPasswordDTO ratedWorkman;
    private int rating;
}
