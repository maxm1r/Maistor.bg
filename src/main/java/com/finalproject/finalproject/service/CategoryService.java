package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category addToDB(CategoryDTO dto) {
        if (dto.getCategoryName() != null){
            Category category = new Category(dto);
            category = categoryRepository.save(category);
            return category;
        }
        else throw new BadRequestException("Invalid name for category");
    }
    @Transactional
    public ResponseEntity<Long> deleteFromDB(CategoryDTO dto) {
        if (!categoryRepository.existsByCategoryName(dto.getCategoryName())){
            throw new BadRequestException("No category with that name");
        }
        else{
            categoryRepository.removeCategoriesByCategoryName(dto.getCategoryName());
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }
}
