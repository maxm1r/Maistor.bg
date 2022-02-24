package com.finalproject.finalproject.service;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.dto.CategoryNameDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;

    public CategoryDTO addToDB(CategoryNameDTO categoryName) {
        if (categoryName.getCategoryName().trim().equals("")) {
         throw new BadRequestException("Invalid name for category");
        }
        if (categoryRepository.existsByCategoryName(categoryName.getCategoryName())){
            throw new BadRequestException("This category already exists");
        }
        Category category = new Category();
        category.setCategoryName(categoryName.getCategoryName());
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO deleteFromDB(CategoryNameDTO categoryName) {
        if (!categoryRepository.existsByCategoryName(categoryName.getCategoryName())){
            throw new BadRequestException("No category with that name");
        }
        Category category = categoryRepository.findByCategoryName(categoryName.getCategoryName()).orElseThrow(()-> new BadRequestException("Category not found"));
        categoryRepository.deleteCategoryByCategoryName(categoryName.getCategoryName());
        return modelMapper.map(category,CategoryDTO.class);
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
    }
}
