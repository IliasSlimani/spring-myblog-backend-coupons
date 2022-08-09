package com.example.coupons.model;

import com.example.coupons.request.CouponRequest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponid;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(length=500,nullable = false)
    private String descr;

    @Column(length=500,nullable = false)
    private String tips;
    @Column(nullable = false)
    private Long nleft;
    @Column(nullable = false)
    private String rating;
    @Column(nullable = false)
    private String image;

    @ElementCollection
    private List<String> type;

    @Column(nullable = false)
    private Long nuses;

    @ElementCollection
    private List<String> tag;
    @Column(nullable = false)
    private Long nRating;

    @OneToMany(mappedBy = "coupon")
    private List<Deal> deals;

    @ManyToOne
    private Category category;


}
