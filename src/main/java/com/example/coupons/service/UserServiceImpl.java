package com.example.coupons.service;

import com.example.coupons.exceptions.DuplicateResource;
import com.example.coupons.exceptions.EmptyRequestParam;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import com.example.coupons.model.User;
import com.example.coupons.repository.UserRepository;
import com.example.coupons.repository.UserRepository;
import com.example.coupons.request.UserRequest;
import com.example.coupons.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        //Should add role as well
        if(userRequest.getRoles() != null)
            throw new EmptyRequestParam("At least one role id should be defined");

        User get_user = userRepository.findByUsername(userRequest.getUsername());
        if(get_user == null) {
            try {
                User user = new User();
                user.setFname(userRequest.getFname());
                user.setLname(userRequest.getLname());
                user.setUsername(userRequest.getUsername());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                ArrayList<Role> roles = new ArrayList<>();
                userRequest.getRoles().forEach(roleid -> {
                    Role role = roleService.getRole(roleid);
                    roles.add(role);
                });
                user.setRoles(roles);

                User user_ = userRepository.save(user);
                UserResponse userResponse = new UserResponse(user_);
                return userResponse;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new DuplicateResource("User " + userRequest.getUsername() + " Already exists");
        }


    }

    @Override
    public Boolean removeUser(Long userid) {

        User get_user = userRepository.findById(userid).orElse(null);
        if(get_user != null) {
            try {
                userRepository.delete(get_user);
                return true;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new ResourceNotFound("No User " + userid + " has been found");
        }


    }

    @Override
    @Transactional
    public UserResponse updateUser(UserRequest userRequest, Long id) {

        User user_f = userRepository.findById(id).orElse(null);


        if(user_f == null) {
            throw new ResourceNotFound("No User " + id + " found");

        }
        else if(userRequest.getUsername() != null && !userRequest.getUsername().equals(user_f.getUsername()))
            throw new DuplicateResource("Usernames can't be updated");
        else {
            User user = new User();
            user.setId(id);
            user.setUsername(user_f.getUsername());
            if(userRequest.getPassword() != null)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            else
                user.setPassword(user_f.getPassword());
            if(userRequest.getLname() != null)
                user.setLname(userRequest.getLname());

            if(userRequest.getFname() != null)
                user.setFname(userRequest.getFname());

            if(userRequest.getRoles() != null) {
                ArrayList<Role> roles = new ArrayList<>();

                userRequest.getRoles().forEach(roleid -> {
                    Role role = roleService.getRole(roleid);
                    if(role != null)
                        roles.add(role);

                });

                user.setRoles(roles);
            }
            UserResponse userResponse = new UserResponse(user);
            return userResponse;
        }


    }

    @Override
    public User getUser(Long id) {

            User user = userRepository.findById(id).orElse(null);
            if (user == null)
                throw new ResourceNotFound("No user " + id + " found");
            else
                return user;



    }


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user -> {
            UserResponse userResponse = new UserResponse(user);
            userResponses.add(userResponse);
        });

        return userResponses;
    }

    @Override
    @Transactional
    public Map<String,String> addRoletoUser(Long roleid, Long userid) {
        Role role = roleService.getRole(roleid);
        User user = userRepository.findById(userid).orElse(null);

        Map<String,String> response = new HashMap<>();
        if(role == null)
            throw new ResourceNotFound("Role " + roleid + " not found");
        if(user == null)
            throw new ResourceNotFound("User " + userid + " not found");

        if(role != null && user != null) {
            List<Role> roles = user.getRoles();
            List<User> users = role.getUsers();

            if(roles.contains(role))
                throw new DuplicateResource("User " + user.getUsername() + " has already this role " + role.getName());

            roles.add(role);
            users.add(user);

            user.setRoles(roles);
            role.setUsers(users);

            response.put("role", role.getName());
            response.put("username", user.getUsername());
            return response;
        }


        return null;
    }

    @Override
    @Transactional
    public Map<String, String> removeRoleFromUser(Long roleid, Long userid) {
        Role role = roleService.getRole(roleid);
        User user = userRepository.findById(userid).orElse(null);

        Map<String,String> response = new HashMap<>();
        if(role == null)
            throw new ResourceNotFound("Role " + roleid + " not found");
        if(user == null)
            throw new ResourceNotFound("User " + userid + " not found");

        if(role != null && user != null) {
            List<Role> roles = user.getRoles();
            List<User> users = role.getUsers();

            if(roles.contains(role)) {
                roles.remove(role);
                users.remove(user);
                user.setRoles(roles);
                role.setUsers(users);
            } else
                throw new ResourceNotFound("User " + user.getUsername() + " don't have role " + role.getName());



            response.put("role", role.getName());
            response.put("username", user.getUsername());
            return response;
        }
        return null;
    }
}
