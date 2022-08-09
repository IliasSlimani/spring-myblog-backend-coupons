package com.example.coupons.service;

import com.example.coupons.model.Coupon;
import com.example.coupons.model.Deal;
import com.example.coupons.model.Deal;
import com.example.coupons.request.DealRequest;
import com.example.coupons.response.DealResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DealService {
    DealResponse addDeal(DealRequest dealRequest);

    Boolean removeDeal(Long dealid);

    DealResponse updateDeal(DealRequest deal, Long id);

    Deal getDeal(Long dealid);


    List<DealResponse> getAllDeals();


}
