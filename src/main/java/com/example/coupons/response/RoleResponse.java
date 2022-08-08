package com.example.coupons.response;

import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    private Long id;


    private String name;


    private List<Map<String,String>> users;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        ArrayList<Map<String,String>> users = new ArrayList<>();
        if(role.getUsers() != null) {
            role.getUsers().forEach(user -> {
                Map<String,String> user_ = new HashMap<>();
                user_.put("id", user.getId().toString());
                user_.put("username", user.getUsername());
                user_.put("fname", user.getFname());
                user_.put("lname", user.getLname());
                users.add(user_);
            });
            this.users = users;
        }

    }
}
