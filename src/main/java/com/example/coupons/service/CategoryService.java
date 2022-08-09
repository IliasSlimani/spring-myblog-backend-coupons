package com.example.coupons.service;

import com.example.coupons.model.Category;
import com.example.coupons.model.Coupon;
import com.example.coupons.response.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Category addCategory(Category category);
    Boolean removeCategory(Long categoryId);
    Boolean updateCategory(Category category, Long id);
    Category getCategory(Long id);

    List<CategoryResponse> getAllCategories();


}
