package com.example.coupons.service;

import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RoleService {

    Role addRole(Role role);

    Boolean removeRole(Long roleid);

    Role updateRole(Role role, Long id);

    Role getRole(Long roleid);

    Map<String,String> addRoletoUser(Long roleid, Long userId);

    Map<String,String> removeRoleFromUser(Long roleid, Long userId);

    List<Role> getAllRoles();
}