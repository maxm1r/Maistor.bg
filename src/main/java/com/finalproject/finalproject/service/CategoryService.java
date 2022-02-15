package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    public Category addToDB(String categoryName) {
        if (categoryName.trim().equals("")) {
         throw new BadRequestException("Invalid name for category");
        }
        if (categoryRepository.existsByCategoryName(categoryName)){
            throw new BadRequestException("This category already exists");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        category = categoryRepository.save(category);
        return category;
    }
    @Transactional
    public Category deleteFromDB(String categoryName) {
        if (!categoryRepository.existsByCategoryName(categoryName)){
            throw new BadRequestException("No category with that name");
        }
        Category category = categoryRepository.getByCategoryName(categoryName);
        categoryRepository.deleteAllByCategoryName(categoryName);
        return category;
    }
}
