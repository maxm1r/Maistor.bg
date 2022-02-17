package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController extends CustomExceptionHandler {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/category/{categoryName}")
    public ResponseEntity<Category> addCategoryToDB(@PathVariable("categoryName") String categoryName){
        return ResponseEntity.ok(categoryService.addToDB(categoryName));
    }

    @DeleteMapping("/category/{categoryName}")
    public ResponseEntity<Category> deleteCategoryFromDB(@PathVariable("categoryName") String categoryName) {
          return ResponseEntity.ok(categoryService.deleteFromDB(categoryName));
    }
}

