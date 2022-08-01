package com.example.coupons.service;

import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Coupon;
import com.example.coupons.repository.CouponsRepository;
import com.example.coupons.request.CouponRequest;
import com.example.coupons.response.CouponResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CouponService {

    @Autowired
    private CouponsRepository couponsRepository;



    public CouponService(CouponsRepository couponsRepository) {
        this.couponsRepository = couponsRepository;
    }

    public CouponResponse addCoupon(Coupon coupon) {

        Coupon cp = couponsRepository.findById(coupon.getCouponid()).orElse(null);
        Coupon savedCoupon;

        if(cp == null) {
            try {
                savedCoupon = couponsRepository.save(coupon);

            } catch (Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }

            CouponResponse couponResponse = new CouponResponse(savedCoupon);
            return couponResponse;
        } else
            throw new ResourceNotFound("Coupon " + coupon.getName() + " Already Existed");


    }

    public CouponResponse getCoupon(Integer couponId) {

        Coupon cp = couponsRepository.findById(couponId).orElse(null);


        CouponResponse couponResponse = new CouponResponse(cp);

        if(cp != null) {

            return couponResponse;
        } else
            throw new ResourceNotFound("Coupon " + couponId + " Not Found");


    }

    public String removeCoupon(Integer couponid) {
        Coupon cp = couponsRepository.findById(couponid).orElse(null);
        if(cp != null) {
            couponsRepository.delete(cp);
            return "Coupon " + cp.getName() + " has been removed successfully";
        } else
            throw new ResourceNotFound("Coupon " + cp.getName() + " Not Found");

    }


    @Transactional
    public CouponResponse updateCoupon(CouponRequest couponrequest, Integer couponId) {
        Coupon cp = couponsRepository.findById(couponId).orElse(null);
        if(cp != null) {

            Coupon coupon = new Coupon(couponrequest);
            coupon.setCouponid(couponId);
            couponsRepository.save(coupon);
            log.info(coupon.toString());

            // Add Coupon Response
            CouponResponse couponResponse = new CouponResponse(coupon);

            return couponResponse;
        } else {
            throw new ResourceNotFound("Coupon " + couponrequest.getName() + " Not Found");
        }
    }
    public List<CouponResponse> getAllCoupons() {

        List<CouponResponse> couponResponses = new ArrayList<>();
        List<Coupon> coupons = couponsRepository.findAll();


        if(coupons == null)
            throw new ResourceNotFound("No Coupons Found");
        else {
            coupons.forEach(cp -> {
                CouponResponse couponResponse = new CouponResponse(cp);


                couponResponses.add(couponResponse);
            });
            return couponResponses;

        }
    }
}
