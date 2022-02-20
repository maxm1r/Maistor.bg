package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
