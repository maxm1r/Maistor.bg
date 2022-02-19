package com.finalproject.finalproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OfferForPostResponseDTO {

    private int id;
    private double pricePerHour;
    private LocalDateTime offer_date;
    private int hoursNeeded;
    private int daysNeeded;
}
