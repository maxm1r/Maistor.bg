package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController extends AbstractController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/category/{categoryName}")
    public Category addCategoryToDB(@PathVariable("categoryName") CategoryDTO dto){
        return categoryService.addToDB(dto);
    }
    @DeleteMapping("/category/{categoryName}")
    public ResponseEntity<Long> deleteCategoryFromDB(@PathVariable("categoryName") CategoryDTO dto) {
          return  categoryService.deleteFromDB(dto);

        }
}
