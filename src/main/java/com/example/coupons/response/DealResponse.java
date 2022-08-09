package com.example.coupons.response;

import com.example.coupons.model.Coupon;
import com.example.coupons.model.Deal;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealResponse {

    private Long id;

    private String name;


    private String description;

    private Long totaluses;

    private Long dailyuses;

    private String code;


    private List<String> tags;

    private String image;


    private Long coupon;

    public DealResponse(Deal deal) {
        this.name = deal.getName();
        this.description = deal.getDescription();
        this.totaluses = deal.getTotaluses();
        this.dailyuses = deal.getDailyuses();
        this.code = deal.getCode();
        this.tags = deal.getTags();
        this.image = deal.getImage();
        this.coupon = deal.getCoupon().getCouponid();
    }
}
