package com.example.coupons.request;

import com.example.coupons.model.Coupon;
import lombok.*;

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
    
    public CouponRequest(Coupon coupon) {
        this.setName(coupon.getName());
        this.setDescr(coupon.getDescr());
        this.setImage(coupon.getImage());
        this.setNuses(coupon.getNuses());
        this.setNleft(coupon.getNleft());
        this.setNRating(coupon.getNRating());

    }
}
