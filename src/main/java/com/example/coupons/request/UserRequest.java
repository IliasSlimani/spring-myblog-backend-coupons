package com.example.coupons.request;

import com.example.coupons.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private Long id;

    private String fname;

    private String lname;

    private String email;

    private String username;

    private String password;


    private List<Long> roles;
}
