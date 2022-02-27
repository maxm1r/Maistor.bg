package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.CategoryDTO;
import com.finalproject.finalproject.model.dto.CategoryNameDTO;
import com.finalproject.finalproject.service.CategoryService;
import com.finalproject.finalproject.utility.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CategoryController extends CustomExceptionHandler {

    @Autowired
    CategoryService categoryService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> addCategoryToDB(@RequestParam("categoryName") CategoryNameDTO categoryName, HttpServletRequest request){
        sessionManager.verifyAdmin(request);
        return ResponseEntity.ok(categoryService.addToDB(categoryName));
    }
    @DeleteMapping("/categories")
    public ResponseEntity<CategoryDTO> deleteCategoryFromDB(@RequestParam("categoryName") CategoryNameDTO categoryName, HttpServletRequest request) {
        sessionManager.verifyAdmin(request);
        return ResponseEntity.ok(categoryService.deleteFromDB(categoryName));
    }
    @GetMapping("/categories/all")
    public ResponseEntity<List<CategoryDTO>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }


}

