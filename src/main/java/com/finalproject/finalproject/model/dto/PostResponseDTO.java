package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Offer;
import com.finalproject.finalproject.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PostResponseDTO {
    private int id;
    private OfferForPostResponseDTO acceptedOffer;
    private String categoryName;
    private String description;
    private String cityName;
    private UserWithoutPasswordDTO owner;
    private LocalDateTime postedDate;


}
