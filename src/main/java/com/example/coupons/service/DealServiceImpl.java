package com.example.coupons.service;

import com.example.coupons.exceptions.DuplicateResource;
import com.example.coupons.exceptions.EmptyRequestParam;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Deal;
import com.example.coupons.model.Deal;
import com.example.coupons.model.Coupon;
import com.example.coupons.repository.DealRepository;
import com.example.coupons.request.DealRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Deal addDeal(DealRequest dealRequest) {

        if(dealRequest.getCouponId() == null)
            throw new EmptyRequestParam("You should specify at least one coupon id");

        Deal deal = new Deal();
        deal.setCode(dealRequest.getCode());
        deal.setDailyuses(dealRequest.getDailyuses());
        deal.setDescription(dealRequest.getDescription());
        deal.setImage(dealRequest.getImage());
        deal.setTags(dealRequest.getTags());
        deal.setName(dealRequest.getName());
        deal.setTotaluses(dealRequest.getTotaluses());
        Coupon coupon = couponService.getCoupon(dealRequest.getCouponId());


        if(get_deal == null) {
            try {
                Deal deal_ = dealRepository.save(deal);
                return deal_;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new DuplicateResource("Deal " + deal.getName() + " Already exists");
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
    public Deal updateDeal(Deal deal, Long id) {

        Deal deal_f = dealRepository.findById(id).orElse(null);
        Deal deal_d = dealRepository.findByName(deal.getName());

        if(deal_f == null) {
            throw new ResourceNotFound("No Deal " + id + " found");

        } else if (deal_d != null) {
            throw new DuplicateResource("Deal " + deal.getName() + " Already exists!");
        }
        else {
            deal.setId(id);
            Deal deal_n = dealRepository.save(deal);
            return deal_n;
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
    @Transactional
    public Map<String,String> addDealtoCoupon(Long dealid, Long couponid) {
        Deal deal = dealRepository.findById(dealid).orElse(null);
        Coupon coupon = couponService.getCoupon(couponid);

        Map<String,String> response = new HashMap<>();
        if(deal == null)
            throw new ResourceNotFound("Deal " + dealid + " not found");
        if(coupon == null)
            throw new ResourceNotFound("Coupon " + couponid + " not found");

        if(deal != null && coupon != null) {
            List<Deal> deals = coupon.getDeals();
            List<Coupon> coupons = deal.getCoupons();

            if(deals.contains(deal))
                throw new DuplicateResource("Coupon " + coupon.getCouponname() + " has already this deal " + deal.getName());

            deals.add(deal);
            coupons.add(coupon);

            coupon.setDeals(deals);
            deal.setCoupons(coupons);

            response.put("deal", deal.getName());
            response.put("couponname", coupon.getCouponname());
            return response;
        }


        return null;
    }

    @Override
    @Transactional
    public Map<String, String> removeDealFromCoupon(Long dealid, Long couponid) {
        Deal deal = dealRepository.findById(dealid).orElse(null);
        Coupon coupon = couponService.getCoupon(couponid);

        Map<String,String> response = new HashMap<>();
        if(deal == null)
            throw new ResourceNotFound("Deal " + dealid + " not found");
        if(coupon == null)
            throw new ResourceNotFound("Coupon " + couponid + " not found");

        if(deal != null && coupon != null) {
            List<Deal> deals = coupon.getDeals();
            List<Coupon> coupons = deal.getCoupons();

            if(deals.contains(deal)) {
                deals.remove(deal);
                coupons.remove(coupon);
                coupon.setDeals(deals);
                deal.setCoupons(coupons);
            } else
                throw new ResourceNotFound("Coupon " + coupon.getCouponname() + " don't have deal " + deal.getName());



            response.put("deal", deal.getName());
            response.put("couponname", coupon.getCouponname());
            return response;
        }
        return null;
    }

    @Override
    public List<Deal> getAllDeals() {
        List<Deal> deals = dealRepository.findAll();
        return deals;
    }
}
