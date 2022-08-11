package com.example.coupons.service;

import com.example.coupons.model.User;
import com.example.coupons.model.User;
import com.example.coupons.request.UserRequest;
import com.example.coupons.response.UserResponse;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserResponse addUser(UserRequest userRequest);

    Boolean removeUser(Long userid);

    UserResponse updateUser(UserRequest userRequest, Long id);

    User getUser(Long userid);
     List<UserResponse> getAllUsers();

    Map<String,String> addRoletoUser(Long roleid, Long userid);

    Map<String, String> removeRoleFromUser(Long roleid, Long userid);
     // Update usernames later depends on use case but for now dont

    User getUser(String username);

    UserResponse register(UserRequest userRequest);
}
