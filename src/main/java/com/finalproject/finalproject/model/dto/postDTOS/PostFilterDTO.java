package com.finalproject.finalproject.model.dto.postDTOS;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.finalproject.model.dto.CategoryNameDTO;
import com.finalproject.finalproject.model.dto.CityNameDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PostFilterDTO {
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate postedDateAfter;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate postedDateBefore;

    private List<CategoryNameDTO> categoryList=new ArrayList<CategoryNameDTO>();
    private List<CityNameDTO> cityList = new ArrayList<CityNameDTO>();

}

