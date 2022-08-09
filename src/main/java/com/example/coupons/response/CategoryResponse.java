package com.example.coupons.response;

import com.example.coupons.model.Category;
import com.example.coupons.model.Coupon;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CategoryResponse {


    private Long id;

    private String name;


    private String description;


    private List<Long> coupons;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        if(category.getCoupons() != null) {
            List<Long> coupons = new ArrayList<>();
            category.getCoupons().forEach(coupon -> {
                coupons.add(coupon.getCouponid());
            });
            this.coupons = coupons;
        }
    }
}
