package com.example.coupons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Deal {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500)
    private String description;

    private Long totaluses;

    private Long dailyuses;

    private String code;

    @ElementCollection
    private List<String> tags;

    private String image;

    @ManyToOne
    private Coupon coupon;


}
