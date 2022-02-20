package com.finalproject.finalproject.model.dto.OfferDTOS;

import com.finalproject.finalproject.model.dto.postDTOS.PostForOfferResponseDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {

    private int id;
    private PostForOfferResponseDTO post;
    private UserWithoutPasswordDTO user;
    private double pricePerHour;
    private LocalDateTime offer_date;
    private int hoursNeeded;
    private int daysNeeded;

}
