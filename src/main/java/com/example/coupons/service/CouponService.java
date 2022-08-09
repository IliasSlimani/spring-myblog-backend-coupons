package com.example.coupons.service;

import com.example.coupons.model.Coupon;
import com.example.coupons.request.CouponRequest;
import com.example.coupons.response.CouponResponse;

import java.util.List;
import java.util.Map;

public interface CouponService {

    CouponResponse addCoupon(CouponRequest coupon);

    Boolean removeCoupon(Long couponid);

    CouponResponse updateCoupon(CouponRequest coupon, Long id);

    Coupon getCoupon(Long couponid);

    List<CouponResponse> getAllCoupons();

    CouponResponse addDealtoCoupon(Long couponid, Long dealid);

    CouponResponse removeDealFromCoupon(Long couponid, Long dealid);

}
