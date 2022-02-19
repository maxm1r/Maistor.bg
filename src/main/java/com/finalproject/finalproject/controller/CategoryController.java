package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.dto.CategoryNameDto;
import com.finalproject.finalproject.model.pojo.Category;
import com.finalproject.finalproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController extends CustomExceptionHandler {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/category/{categoryName}")
    public ResponseEntity<CategoryDTO> addCategoryToDB(@PathVariable("categoryName") CategoryNameDto categoryName){

        return ResponseEntity.ok(categoryService.addToDB(categoryName));
    }

    @DeleteMapping("/category/{categoryName}")
    public ResponseEntity<CategoryDTO> deleteCategoryFromDB(@PathVariable("categoryName") CategoryNameDto categoryName) {
          return ResponseEntity.ok(categoryService.deleteFromDB(categoryName));
    }
    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryDTO>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }
}

