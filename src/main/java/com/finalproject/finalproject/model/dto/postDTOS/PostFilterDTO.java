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

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PostFilterDTO {

    private LocalDateTime postedDateAfter;
    private LocalDateTime postedDateBefore;
    private LocalDateTime assignedDateAfter;
    private LocalDateTime assignedDateBefore;
    private List<String> categoryList;
    private List<City> cityList;

}

