package com.example.coupons.service;

import com.example.coupons.exceptions.DuplicateResource;
import com.example.coupons.exceptions.EmptyRequestParam;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Deal;
import com.example.coupons.model.Coupon;
import com.example.coupons.repository.CouponsRepository;
import com.example.coupons.repository.DealRepository;
import com.example.coupons.request.DealRequest;
import com.example.coupons.response.CouponResponse;
import com.example.coupons.response.DealResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DealServiceImpl implements DealService{
    @Autowired
    private DealRepository dealRepository;


    @Autowired
    private CouponService couponService;

    @Override
    public DealResponse addDeal(DealRequest dealRequest) {

        if(dealRequest.getCoupon() == null)
            throw new EmptyRequestParam("You should specify at least one coupon id");

        Deal deal = new Deal();
        deal.setCode(dealRequest.getCode());
        deal.setDailyuses(dealRequest.getDailyuses());
        deal.setDescription(dealRequest.getDescription());
        deal.setImage(dealRequest.getImage());
        deal.setTags(dealRequest.getTags());
        deal.setName(dealRequest.getName());
        deal.setTotaluses(dealRequest.getTotaluses());
        Coupon coupon = couponService.getCoupon(dealRequest.getCoupon());
        if(coupon != null)
            deal.setCoupon(coupon);
        else
            throw new ResourceNotFound("No Coupon with id " + dealRequest.getCoupon() + " found");
           try {
                Deal deal_ = dealRepository.save(deal);
                DealResponse dealResponse = new DealResponse(deal_);
                return dealResponse;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }



    }

    @Override
    public Boolean removeDeal(Long dealid) {

        Deal get_deal = dealRepository.findById(dealid).orElse(null);
        if(get_deal != null) {
            try {
                dealRepository.delete(get_deal);
                return true;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new ResourceNotFound("No deal " + dealid + " has been found");
        }


    }

    @Override
    @Transactional
    public DealResponse updateDeal(DealRequest dealRequest, Long id) {

        Deal deal_f = dealRepository.findById(id).orElse(null);


        if(deal_f == null) {
            throw new ResourceNotFound("No Deal " + id + " found");

        }
        else {
            Deal deal = new Deal();
            deal.setId(id);
            deal.setCode(dealRequest.getCode());
            deal.setDailyuses(dealRequest.getDailyuses());
            deal.setDescription(dealRequest.getDescription());
            deal.setImage(dealRequest.getImage());
            deal.setTags(dealRequest.getTags());
            deal.setName(dealRequest.getName());
            deal.setTotaluses(dealRequest.getTotaluses());
            if(dealRequest.getCoupon() != null) {
                Coupon coupon = couponService.getCoupon(dealRequest.getCoupon());
                if(coupon != null)
                    deal.setCoupon(coupon);
            }

            DealResponse dealResponse = new DealResponse(deal);

            return dealResponse;
        }


    }

    @Override
    public Deal getDeal(Long id) {
        try {
            Deal deal = dealRepository.findById(id).orElse(null);
            if(deal == null)
                throw new ResourceNotFound("No deal " + id + " found");
            else
                return deal;

        } catch (Exception ex) {
            throw new InternalError("Internal Problem. Check with your admin");
        }

    }


    @Override
    public List<DealResponse> getAllDeals() {
        List<Deal> deals = dealRepository.findAll();
        List<DealResponse> dealsresponse = new ArrayList<>();
        deals.forEach(deal -> {
            DealResponse dealResponse = new DealResponse(deal);
            dealsresponse.add(dealResponse);
        });
        return dealsresponse;
    }
}
