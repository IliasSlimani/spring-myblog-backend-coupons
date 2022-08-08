package com.example.coupons.repository;

import com.example.coupons.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CouponsRepository extends JpaRepository<Coupon, Long> {


    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Coupon cp set cp.name=:name, cp.descr=:desc, cp.tips=:tips, cp.nleft=:nleft, cp.rating=:rating, cp.image=:image, cp.nuses=:nuses, cp.nRating=:nrating where cp.couponid = :id ")
    void updateCoupon(@Param("id") Integer id, @Param("name") String name, @Param("desc") String desc, @Param("tips") String tips, @Param("nleft") Long nleft, @Param("rating") String rating, @Param("image") String image, @Param("nuses") Long nsuses, @Param("nrating") Long nrating  );
}
