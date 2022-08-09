package com.example.coupons.controllers;

import com.example.coupons.model.Role;
import com.example.coupons.response.RoleResponse;
import com.example.coupons.service.RoleService;
import com.example.coupons.utils.ResponseHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    private ResponseHandler responseHandler;

    @PostMapping("/addrole")
    ResponseEntity<Object> addRole(@RequestBody Role role) {
        Role role_ = roleService.addRole(role);
        RoleResponse roleResponse = new RoleResponse(role_);
        return responseHandler.generateResponse("Role has been saved successfully", HttpStatus.OK, roleResponse);
    }

    @DeleteMapping("/removerole/{roleid}")
    ResponseEntity<Object> removeRole(@PathVariable Long roleid) {
        roleService.removeRole(roleid);

        return responseHandler.generateResponse("Role " + roleid + " has been deleted successfully", HttpStatus.OK, "");
    }

    @PostMapping("/updaterole/{roleid}")
    ResponseEntity<Object> updateRole(@RequestBody Role role, @PathVariable("roleid") Long id) {

        Role role_ = roleService.updateRole(role, id);
        RoleResponse roleResponse = new RoleResponse(role_);
        return responseHandler.generateResponse("Role " + id + " has been updated successfully", HttpStatus.OK, roleResponse);

    }

    @GetMapping("/getrole/{roleid}")
    ResponseEntity<Object> getRole(@PathVariable ("roleid") Long roleid) {
        Role role = roleService.getRole(roleid);
        RoleResponse roleResponse = new RoleResponse(role);

        return responseHandler.generateResponse("Role " + roleid + " has been retrieved successfully", HttpStatus.OK, roleResponse);
    }

    @GetMapping("/getallroles")
    ResponseEntity<Object> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponse> rolesResponse = new ArrayList<>();
        roles.forEach(role -> {
            RoleResponse roleResponse = new RoleResponse(role);
            rolesResponse.add(roleResponse);
        });
        return responseHandler.generateResponse("All Roles have been retrieved successfully.", HttpStatus.OK, rolesResponse);
    }


}
