package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.User;
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
public class PostResponseDTO {

    private int id;
    private String categoryName;
    private String description;
    private String cityName;
    private int ownerId;

}
