package com.example.coupons.request;

import com.example.coupons.model.Coupon;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CouponRequest {


    private String name;


    private String descr;


    private String tips;

    private Long nleft;

    private String rating;

    private String image;


    private List<String> type;

    private Long nuses;

    private List<String> tag;

    private Long nRating;

    private List<Long> deals;

    private Long category;
    
    public CouponRequest(Coupon coupon) {
        this.setName(coupon.getName());
        this.setDescr(coupon.getDescr());
        this.setImage(coupon.getImage());
        this.setNuses(coupon.getNuses());
        this.setNleft(coupon.getNleft());
        this.setNRating(coupon.getNRating());
        this.setTag(coupon.getTag());
        this.setType(coupon.getType());
        this.setCategory(coupon.getCategory().getId());
        ArrayList<Long> deals = new ArrayList<>();
        coupon.getDeals().forEach(deal -> {
            deals.add(deal.getId());
        });
        this.deals = deals;
    }
}
