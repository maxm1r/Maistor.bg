package com.finalproject.finalproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OfferEditDTO {

    private int id;
    private double pricePerHour;
    private int hoursNeeded;
    private int daysNeeded;
}
