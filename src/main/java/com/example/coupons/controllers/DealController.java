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

        DealResponse dealResponse = dealService.addDeal(dealRequest);

        return responseHandler.generateResponse("Deal has been saved successfully", HttpStatus.OK, dealResponse);
    }

    @DeleteMapping("/removedeal/{dealid}")
    ResponseEntity<Object> removeDeal(@PathVariable Long dealid) {
        dealService.removeDeal(dealid);

        return responseHandler.generateResponse("Deal " + dealid + " has been deleted successfully", HttpStatus.OK, "");
    }

    @PostMapping("/updatedeal/{dealid}")
    ResponseEntity<Object> updateDeal(@RequestBody DealRequest dealRequest, @PathVariable("dealid") Long id) {

        DealResponse dealResponse = dealService.updateDeal(dealRequest, id);

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
        List<DealResponse> dealsResponse = dealService.getAllDeals();

        return responseHandler.generateResponse("All Deals have been retrieved successfully.", HttpStatus.OK, dealsResponse);
    }

}
