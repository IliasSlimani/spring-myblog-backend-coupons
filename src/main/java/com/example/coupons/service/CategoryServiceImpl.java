package com.example.coupons.service;

import com.example.coupons.exceptions.EmptyRequestParam;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Category;
import com.example.coupons.model.Coupon;
import com.example.coupons.repository.CategoryRepository;
import com.example.coupons.repository.CouponsRepository;
import com.example.coupons.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements  CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponsRepository couponsRepository;


    @Override
    public Category addCategory(Category category) {
        if(category!= null && category.getName().isEmpty())
            throw new EmptyRequestParam("At Least one category name should be provided");

            Category category1 = categoryRepository.save(category);
            return category1;




    }

    @Override
    public Boolean removeCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category != null) {
            categoryRepository.delete(category);
            return true;
        } else {
            throw new ResourceNotFound("No Category with id" + categoryId + " found");
        }



    }

    @Override
    @Transactional
    public Boolean updateCategory(Category category, Long id) {
        Category category1 = categoryRepository.findById(id).orElse(null);
        if(category1 != null) {
            category1.setDescription(category.getDescription());
            category1.setName(category.getName());
            category1.setId(id);
        } else {
            throw new ResourceNotFound("No Category with id " + id + " found");
        }
        return null;
    }

    @Override
    public Category getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null)
            return category;
        else
            throw new ResourceNotFound("No Category with id " + id + " found");

    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categories.forEach(category -> {
            CategoryResponse categoryResponse = new CategoryResponse(category);
            categoryResponses.add(categoryResponse);
        });


        return categoryResponses;
    }




}
