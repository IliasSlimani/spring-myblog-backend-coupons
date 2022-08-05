package com.example.coupons.service;

import com.example.coupons.model.User;

public interface UserService {

    User getUser(Long id);
    User saveUser(User user);

}
