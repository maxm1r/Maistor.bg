package com.finalproject.finalproject.model.dto.postDTOS;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.City;
import com.finalproject.finalproject.model.pojo.Offer;
import com.finalproject.finalproject.model.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PostDTO {

    private String categoryName;
    private String description;
    private String cityName;

}
