package com.finalproject.finalproject.model.dto;

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
public class PostForOfferResponseDTO {
    private int id;
    private String categoryName;
    private String description;
    private String cityName;
    private UserWithoutPasswordDTO owner;
    private LocalDateTime postedDate;
}
