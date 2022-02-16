package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.model.pojo.User;
import com.finalproject.finalproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController extends AbstractController {

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

