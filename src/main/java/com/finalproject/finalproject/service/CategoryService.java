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

    public Category addToDB(CategoryDTO dto) {
        if (dto.getCategoryName() == null) {
         throw new BadRequestException("Invalid name for category");
        }
        Category category = modelMapper.map(dto,Category.class);
        category = categoryRepository.save(category);
        return category;
    }
    @Transactional
    public Category deleteFromDB(CategoryDTO category) {
        if (!categoryRepository.existsByCategoryName(category.getCategoryName())){
            throw new BadRequestException("No category with that name");
        }
        return categoryRepository.removeCategoriesByCategoryName(category.getCategoryName());
    }
}
