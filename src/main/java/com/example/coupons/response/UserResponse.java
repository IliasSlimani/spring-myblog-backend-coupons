package com.example.coupons.response;

import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {


    private Long id;

    private String fname;

    private String lname;

    private String email;

    private String username;

    private String password;


    private List<Map<String,String>> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.fname = user.getFname();
        this.lname = user.getLname();
        this.username = user.getUsername();
        this.password = user.getPassword();
        if(user.getEmail() != null)
            this.email = user.getEmail();
        List<Map<String,String>> roles = new ArrayList<>();
        user.getRoles().forEach(role -> {
            Map<String,String> role_ = new HashMap<>();
            role_.put("id", role.getId().toString());
            role_.put("name", role.getName());
            roles.add(role_);
        });
        this.roles = roles;

    }
}
