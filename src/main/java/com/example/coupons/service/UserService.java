package com.example.coupons.service;

import com.example.coupons.model.User;
import com.example.coupons.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    Boolean removeUser(Long userid);

    User updateUser(User user, Long id);

    User getUser(Long userid);
     List<User> getAllUsers();

     // Update usernames later depends on use case but for now dont


}
