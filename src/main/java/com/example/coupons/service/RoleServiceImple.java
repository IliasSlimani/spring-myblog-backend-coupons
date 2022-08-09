package com.example.coupons.service;

import com.example.coupons.exceptions.DuplicateResource;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import com.example.coupons.repository.RoleRepository;
import com.example.coupons.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImple implements RoleService{

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role addRole(Role role) {
        Role get_role = roleRepository.findByName(role.getName());
        if(get_role == null) {
            try {
                Role role_ = roleRepository.save(role);
                return role_;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new DuplicateResource("Role " + role.getName() + " Already exists");
        }


    }

    @Override
    public Boolean removeRole(Long roleid) {

        Role get_role = roleRepository.findById(roleid).orElse(null);
        if(get_role != null) {
            try {
                roleRepository.delete(get_role);
                return true;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new ResourceNotFound("No role " + roleid + " has been found");
        }


    }

    @Override
    @Transactional
    public Role updateRole(Role role, Long id) {

        Role role_f = roleRepository.findById(id).orElse(null);
        Role role_d = roleRepository.findByName(role.getName());

        if(role_f == null) {
            throw new ResourceNotFound("No Role " + id + " found");

        } else if (role_d != null) {
            throw new DuplicateResource("Role " + role.getName() + " Already exists!");
        }
        else {
            role.setId(id);
            Role role_n = roleRepository.save(role);
            return role_n;
        }


    }

    @Override
    public Role getRole(Long id) {
        try {
            Role role = roleRepository.findById(id).orElse(null);
            if(role == null)
                throw new ResourceNotFound("No role " + id + " found");
            else
                return role;

        } catch (Exception ex) {
            throw new InternalError("Internal Problem. Check with your admin");
        }

    }



    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }
}
