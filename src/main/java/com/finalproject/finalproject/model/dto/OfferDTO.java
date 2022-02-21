package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Post;
import com.finalproject.finalproject.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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
