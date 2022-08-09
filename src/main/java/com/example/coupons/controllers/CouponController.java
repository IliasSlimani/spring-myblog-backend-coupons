package com.example.coupons.controllers;

import com.example.coupons.model.Coupon;
import com.example.coupons.request.CouponRequest;
import com.example.coupons.response.CategoryResponse;
import com.example.coupons.response.CouponResponse;
import com.example.coupons.service.CouponServiceImpl;
import com.example.coupons.utils.ResponseHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class CouponController {

    @Autowired
    private CouponServiceImpl couponService;

    @Autowired
    private ResponseHandler responseHandler;

    @GetMapping("/coupon/{couponId}")
    public ResponseEntity<Object> getCoupon(@PathVariable("couponId") Long couponId) {
        log.info("Coupon id: " + couponId);
        Coupon coupon = couponService.getCoupon(couponId);
        CouponResponse couponResponse = new CouponResponse(coupon);
        String msg = "Coupon " + couponResponse.getName() + " retrieved successfully";

        return responseHandler.generateResponse(msg,HttpStatus.OK, couponResponse);

    }

    @PostMapping("/addcoupon")
    public ResponseEntity<Object> addCoupon(@RequestBody CouponRequest couponRequest) {

        CouponResponse couponResponse = couponService.addCoupon(couponRequest);


        String msg = "Coupon " + couponResponse.getName() + " saved successfully";

        return responseHandler.generateResponse(msg, HttpStatus.OK,  couponResponse);
    }

    @PutMapping("/updatecoupon/{couponId}")
    public ResponseEntity<Object> updateCoupon(@RequestBody CouponRequest couponRequest, @PathVariable("couponId") Long couponId) {



        CouponResponse cpresponse = couponService.updateCoupon(couponRequest, couponId);


        return responseHandler.generateResponse("Coupon " + cpresponse.getName() + " updated successfully", HttpStatus.OK, cpresponse);
    }

    @DeleteMapping("/deletecoupon/{couponid}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable("couponid") Long couponId) {
        couponService.removeCoupon(couponId);
        return responseHandler.generateResponse("Coupon " + couponId + " has been deleted successfully", HttpStatus.OK, "" );
    }

    @GetMapping("/coupons")
    public ResponseEntity<Object> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();


        return responseHandler.generateResponse("All coupons retrieved successfully", HttpStatus.OK, coupons);
    }



}
