package com.example.coupons.service;

import com.example.coupons.model.Category;
import com.example.coupons.model.Coupon;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category addCategory(Category category);
    Boolean removeCategory(Category category);
    Boolean updateCategory(Long id);
    Category getCategory(Long id);
    Boolean addCouponToCategory(Coupon coupon,Category category);
    Boolean removeCouponFromCategory(Coupon coupon, Category category);
}
