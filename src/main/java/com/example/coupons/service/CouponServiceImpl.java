package com.example.coupons.service;

import com.example.coupons.exceptions.EmptyRequestParam;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Category;
import com.example.coupons.model.Coupon;
import com.example.coupons.model.Deal;
import com.example.coupons.repository.CouponsRepository;
import com.example.coupons.repository.DealRepository;
import com.example.coupons.request.CouponRequest;
import com.example.coupons.response.CategoryResponse;
import com.example.coupons.response.CouponResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponsRepository couponsRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DealRepository dealRepository;


    public CouponResponse addCoupon(CouponRequest couponRequest) {

        if(couponRequest.getCategory() == null)
            throw new EmptyRequestParam("At Least one category is required");


        Coupon coupon = new Coupon();
        coupon.setName(couponRequest.getName());
        coupon.setDescr(couponRequest.getDescr());
        coupon.setTips(couponRequest.getTips());
        coupon.setNleft(couponRequest.getNleft());
        coupon.setRating(couponRequest.getRating());
        coupon.setImage(couponRequest.getImage());
        coupon.setType(couponRequest.getType());
        coupon.setNuses(couponRequest.getNuses());
        coupon.setTag(couponRequest.getTag());
        coupon.setNRating(couponRequest.getNRating());
        Category category = categoryService.getCategory(couponRequest.getCategory());
        coupon.setCategory(category);

        Coupon savedCoupon = couponsRepository.save(coupon);

        CouponResponse couponResponse = new CouponResponse(savedCoupon);

        return couponResponse;


    }

    public Coupon getCoupon(Long couponId) {

        Coupon cp = couponsRepository.findById(couponId).orElse(null);




        if(cp != null) {

            return cp;
        } else
            throw new ResourceNotFound("Coupon " + couponId + " Not Found");


    }


    public Boolean removeCoupon(Long couponid) {
        Coupon cp = couponsRepository.findById(couponid).orElse(null);
        if(cp != null) {
            couponsRepository.delete(cp);
            return true;
        } else
            throw new ResourceNotFound("Coupon " + cp.getName() + " Not Found");

    }



    @Transactional
    @Override
    public CouponResponse updateCoupon(CouponRequest couponRequest, Long couponId) {


        Coupon cp = couponsRepository.findById(couponId).orElse(null);
        if(cp != null) {
            Coupon coupon = new Coupon();
            coupon.setCouponid(couponId);
            if(couponRequest.getName() != null)
                coupon.setName(couponRequest.getName());
            if(couponRequest.getDescr() != null)
                coupon.setDescr(couponRequest.getDescr());
            if(couponRequest.getTips() != null)
                coupon.setTips(couponRequest.getTips());
            if(couponRequest.getNleft() != null)
                coupon.setNleft(couponRequest.getNleft());
            if(couponRequest.getRating() != null)
                coupon.setRating(couponRequest.getRating());
            if(couponRequest.getImage() != null)
                coupon.setImage(couponRequest.getImage());
            if(couponRequest.getType() != null)
                coupon.setType(couponRequest.getType());
            if(couponRequest.getNuses() != null)
                coupon.setNuses(couponRequest.getNuses());
            if(couponRequest.getTag() != null)
                coupon.setTag(couponRequest.getTag());
            if(couponRequest.getCategory() != null) {
                Category category = categoryService.getCategory(couponRequest.getCategory());
                coupon.setCategory(category);
            }



            // Add Coupon Response
            CouponResponse couponResponse = new CouponResponse(coupon);

            return couponResponse;
        } else {
            throw new ResourceNotFound("Coupon " + couponRequest.getName() + " Not Found");
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

    @Override
    @Transactional
    public CouponResponse addDealtoCoupon(Long couponid, Long dealid) {
        Coupon coupon = couponsRepository.findById(couponid).orElse(null);
        Deal deal = dealRepository.findById(dealid).orElse(null);

        if(coupon == null)
            throw new ResourceNotFound("No Coupon with id " + couponid + " found");

        if(deal == null)
            throw new ResourceNotFound("No Deal with id " + dealid + " found");

        List<Deal> deals = coupon.getDeals();
        deals.add(deal);
        coupon.setDeals(deals);

        deal.setCoupon(coupon);

        CouponResponse couponResponse = new CouponResponse(coupon);
        return couponResponse;
    }

    @Override
    @Transactional
    public CouponResponse removeDealFromCoupon(Long couponid, Long dealid) {
        Coupon coupon = couponsRepository.findById(couponid).orElse(null);
        Deal deal = dealRepository.findById(dealid).orElse(null);

        if(coupon == null)
            throw new ResourceNotFound("No Coupon with id " + couponid + " found");

        if(deal == null)
            throw new ResourceNotFound("No Deal with id " + dealid + " found");

        List<Deal> deals = coupon.getDeals();
        deals.remove(deal);
        coupon.setDeals(deals);

        deal.setCoupon(null);

        CouponResponse couponResponse = new CouponResponse(coupon);
        return couponResponse;
    }


}
