package com.example.coupons.controllers;

import com.example.coupons.model.Coupon;
import com.example.coupons.request.CouponRequest;
import com.example.coupons.response.CouponResponse;
import com.example.coupons.service.CouponService;
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
    private CouponService couponService;

    @Autowired
    private ResponseHandler responseHandler;

    @GetMapping("/coupon/{couponId}")
    public ResponseEntity<Object> getCoupon(@PathVariable("couponId") Integer couponId) {
        log.info("Coupon id: " + couponId);
        CouponResponse couponResponse = couponService.getCoupon(couponId);
        String msg = "Coupon " + couponResponse.getName() + " retrieved successfully";

        return responseHandler.generateResponse(msg,HttpStatus.OK, couponResponse);

    }

    @PostMapping("/addcoupon")
    public ResponseEntity<Object> addCoupon(@RequestBody Coupon coupon) {


        log.info("Coupon: " + coupon.toString());

        CouponResponse couponResponse = couponService.addCoupon(coupon);
        log.info("Saved Coupon: " + couponResponse);

        String msg = "Coupon " + couponResponse.getName() + " saved successfully";

        return responseHandler.generateResponse(msg, HttpStatus.OK,  couponResponse);
    }

    @PutMapping("/updatecoupon/{couponId}")
    public ResponseEntity<Object> updateCoupon(@RequestBody CouponRequest couponRequest, @PathVariable("couponId") Integer couponId) {



        CouponResponse cpresponse = couponService.updateCoupon(couponRequest, couponId);


        return responseHandler.generateResponse("Coupon " + cpresponse.getName() + " updated successfully", HttpStatus.OK, cpresponse);
    }

    @DeleteMapping("/deletecoupon/{couponid}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable("couponid") Integer couponId) {
        String msg = couponService.removeCoupon(couponId);
        return responseHandler.generateResponse(msg, HttpStatus.OK, "" );
    }

    @GetMapping("/coupons")
    public ResponseEntity<Object> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();


        return responseHandler.generateResponse("All coupons retrieved successfully", HttpStatus.OK, coupons);
    }

}
