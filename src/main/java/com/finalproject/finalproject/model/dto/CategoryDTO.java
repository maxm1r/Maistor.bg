package com.finalproject.finalproject.model.dto;

import com.finalproject.finalproject.model.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private String categoryName;

    public CategoryDTO(Category category){
        this.categoryName = category.getCategoryName();
    }

}
