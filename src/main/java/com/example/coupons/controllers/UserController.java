package com.example.coupons.controllers;

import com.example.coupons.model.User;
import com.example.coupons.response.UserResponse;
import com.example.coupons.service.UserService;
import com.example.coupons.utils.ResponseHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ResponseHandler responseHandler;

    @PostMapping("/adduser")
    ResponseEntity<Object> addUser(@RequestBody User user) {
        User user_ = userService.addUser(user);
        UserResponse userResponse = new UserResponse(user_);
        return responseHandler.generateResponse("User has been saved successfully", HttpStatus.OK, userResponse);
    }

    @DeleteMapping("/removeuser/{userid}")
    ResponseEntity<Object> removeUser(@PathVariable Long userid) {
        userService.removeUser(userid);

        return responseHandler.generateResponse("User " + userid + " has been deleted successfully", HttpStatus.OK, "");
    }

    @PostMapping("/updateuser/{userid}")
    ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable("userid") Long id) {

        User user_ = userService.updateUser(user, id);
        UserResponse userResponse = new UserResponse(user_);
        return responseHandler.generateResponse("User " + id + " has been updated successfully", HttpStatus.OK, userResponse);

    }

    @GetMapping("/getuser/{userid}")
    ResponseEntity<Object> getUser(@PathVariable ("userid") Long userid) {
        User user = userService.getUser(userid);
        UserResponse userResponse = new UserResponse(user);
        return responseHandler.generateResponse("User " + userid + " has been retrieved successfully", HttpStatus.OK, userResponse);
    }

    @GetMapping("/getallusers")
    ResponseEntity<Object> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> usersResponse = new ArrayList<>();
        users.forEach(user -> {
            UserResponse userResponse = new UserResponse(user);
            usersResponse.add(userResponse);
        });
        return responseHandler.generateResponse("All Users have been retrieved successfully.", HttpStatus.OK, usersResponse);
    }


}
