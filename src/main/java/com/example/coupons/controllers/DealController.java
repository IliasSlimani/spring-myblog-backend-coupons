package com.example.coupons.controllers;

import com.example.coupons.model.Deal;
import com.example.coupons.request.DealRequest;
import com.example.coupons.response.DealResponse;
import com.example.coupons.service.DealService;
import com.example.coupons.utils.ResponseHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class DealController {
    @Autowired
    DealService dealService;

    @Autowired
    private ResponseHandler responseHandler;

    @PostMapping("/adddeal")
    ResponseEntity<Object> addDeal(@RequestBody DealRequest dealRequest) {
        Deal deal_ = dealService.addDeal(dealRequest);
        DealResponse dealResponse = new DealResponse(deal_);
        return responseHandler.generateResponse("Deal has been saved successfully", HttpStatus.OK, dealResponse);
    }

    @DeleteMapping("/removedeal/{dealid}")
    ResponseEntity<Object> removeDeal(@PathVariable Long dealid) {
        dealService.removeDeal(dealid);

        return responseHandler.generateResponse("Deal " + dealid + " has been deleted successfully", HttpStatus.OK, "");
    }

    @PostMapping("/updatedeal/{dealid}")
    ResponseEntity<Object> updateDeal(@RequestBody Deal deal, @PathVariable("dealid") Long id) {

        Deal deal_ = dealService.updateDeal(deal, id);
        DealResponse dealResponse = new DealResponse(deal_);
        return responseHandler.generateResponse("Deal " + id + " has been updated successfully", HttpStatus.OK, dealResponse);

    }

    @GetMapping("/getdeal/{dealid}")
    ResponseEntity<Object> getDeal(@PathVariable ("dealid") Long dealid) {
        Deal deal = dealService.getDeal(dealid);
        DealResponse dealResponse = new DealResponse(deal);

        return responseHandler.generateResponse("Deal " + dealid + " has been retrieved successfully", HttpStatus.OK, dealResponse);
    }

    @GetMapping("/getalldeals")
    ResponseEntity<Object> getAllDeals() {
        List<Deal> deals = dealService.getAllDeals();
        List<DealResponse> dealsResponse = new ArrayList<>();
        deals.forEach(deal -> {
            DealResponse dealResponse = new DealResponse(deal);
            dealsResponse.add(dealResponse);
        });
        return responseHandler.generateResponse("All Deals have been retrieved successfully.", HttpStatus.OK, dealsResponse);
    }

    @PostMapping("/adddealtocoupon/{dealid}/{couponid}")
    ResponseEntity<Object> addDealtoCoupon(@PathVariable("dealid") Long dealid, @PathVariable("couponid") Long couponid) {
        Map<String, String> response = dealService.addDealtoCoupon(dealid,couponid);
        return responseHandler.generateResponse("Deal " + response.get("deal") + " has been added successfully to coupon " + response.get("couponname"), HttpStatus.OK, "");

    }

    @PostMapping("/removedealfromcoupon/{dealid}/{couponid}")
    ResponseEntity<Object> removeDealFromCoupon(@PathVariable("dealid") Long dealid,@PathVariable("couponid") Long couponid) {
        Map<String, String> response = dealService.removeDealFromCoupon(dealid,couponid);
        return responseHandler.generateResponse("Deal " + response.get("deal") + " has been removed successfully from coupon " + response.get("couponname"), HttpStatus.OK, "");

    }
}
