package com.example.coupons.controllers;


import com.example.coupons.model.Category;
import com.example.coupons.response.CategoryResponse;
import com.example.coupons.service.CategoryService;
import com.example.coupons.utils.ResponseHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResponseHandler responseHandler;

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Object> getCategory(@PathVariable("categoryId") Long categoryId) {
        log.info("Category id: " + categoryId);
        Category category = categoryService.getCategory(categoryId);
        CategoryResponse categoryResponse = new CategoryResponse(category);
        String msg = "Category " + categoryResponse.getName() + " retrieved successfully";

        return responseHandler.generateResponse(msg, HttpStatus.OK, categoryResponse);

    }

    @PostMapping("/addcategory")
    public ResponseEntity<Object> addCategory(@RequestBody Category category) {

        Category category1 = categoryService.addCategory(category);


        String msg = "Category " + category1.getName() + " saved successfully";

        return responseHandler.generateResponse(msg, HttpStatus.OK,  category1);
    }

    @PostMapping("/updatecategory/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestBody Category category, @PathVariable("categoryId") Long categoryId) {



        categoryService.updateCategory(category, categoryId);
        category.setId(categoryId);

        return responseHandler.generateResponse("Category " + category.getName() + " updated successfully", HttpStatus.OK, category);
    }

    @DeleteMapping("/deletecategory/{categoryid}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("categoryid") Long categoryId) {
        categoryService.removeCategory(categoryId);
        return responseHandler.generateResponse("Category " + categoryId + " has been deleted successfully", HttpStatus.OK, "" );
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();


        return responseHandler.generateResponse("All categories retrieved successfully", HttpStatus.OK, categoryResponses);
    }



}
