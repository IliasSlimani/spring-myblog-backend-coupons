package com.example.coupons.response;

import com.example.coupons.model.Coupon;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CouponResponse {


    private Long id;

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
    
    public CouponResponse(Coupon coupon) {
        this.setId(coupon.getCouponid());
        this.setName(coupon.getName());
        this.setRating(coupon.getRating());
        this.setTips(coupon.getTips());
        this.setDescr(coupon.getDescr());
        this.setImage(coupon.getImage());
        this.setNleft(coupon.getNleft());
        this.setNRating(coupon.getNRating());
        this.setNuses(coupon.getNuses());
        this.setType(coupon.getType());
        this.setTag(coupon.getTag());
        if(coupon.getCategory() != null)
            this.setCategory(coupon.getCategory().getId());
        if(coupon.getDeals() != null) {
            ArrayList<Long> deals = new ArrayList<>();
            coupon.getDeals().forEach(deal -> {
                deals.add(deal.getId());

            });
            this.setDeals(deals);
        }


    }

}
