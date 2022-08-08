package com.example.coupons.request;

import com.example.coupons.model.Coupon;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealRequest {


    private Long id;

    private String name;


    private String description;

    private Long totaluses;

    private Long dailyuses;

    private String code;


    private List<String> tags;

    private String image;


    private Long couponId;

}
