package com.finalproject.finalproject.model.dto.postDTOS;

import com.finalproject.finalproject.model.dto.CategoryNameDTO;
import com.finalproject.finalproject.model.dto.CityNameDTO;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferForPostResponseDTO;
import com.finalproject.finalproject.model.dto.userDTOS.UserWithoutPasswordDTO;
import com.finalproject.finalproject.model.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PostResponseDTO {


    private int id;
    private String categoryName;
    private String description;
    private String cityName;
    private UserWithoutPasswordDTO owner;
    private LocalDate postedDate;

}
