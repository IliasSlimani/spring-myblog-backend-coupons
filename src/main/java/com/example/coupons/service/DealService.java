package com.example.coupons.service;

import com.example.coupons.model.Coupon;
import com.example.coupons.model.Deal;
import com.example.coupons.model.Deal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DealService {
    Deal addDeal(Deal deal);

    Boolean removeDeal(Long dealid);

    Deal updateDeal(Deal deal, Long id);

    Deal getDeal(Long dealid);

    Map<String,String> addDealtoCoupon(Long dealid, Long couponId);

    Map<String,String> removeDealFromCoupon(Long dealid, Long couponId);

    List<Deal> getAllDeals();


}
