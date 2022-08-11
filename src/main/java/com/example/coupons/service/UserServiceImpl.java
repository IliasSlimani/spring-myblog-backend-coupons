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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService,UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;



    @Override
    public UserResponse addUser(UserRequest userRequest) {
        //Should add role as well
        if(userRequest.getRoles() == null)
            throw new EmptyRequestParam("At least one role id should be defined");

        User get_user = userRepository.findByUsername(userRequest.getUsername());
        if(get_user != null)
            throw new DuplicateResource("User " + userRequest.getUsername() + " Already exists");

        User get_user_email = userRepository.findByEmail(userRequest.getEmail());
        if(get_user_email != null)
            throw new DuplicateResource("Email " + userRequest.getEmail() + " Already exists");

        if(get_user == null && get_user_email == null) {
            try {
                User user = new User();
                user.setFname(userRequest.getFname());
                user.setLname(userRequest.getLname());
                user.setUsername(userRequest.getUsername());
                user.setEmail(userRequest.getEmail());
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                ArrayList<Role> roles = new ArrayList<>();


                userRequest.getRoles().forEach(roleid -> {

                    Role role = roleService.getRole(roleid);
                    List<User> users = role.getUsers();
                    users.add(user);
                    role.setUsers(users);
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

                throw new DuplicateResource("User " + userRequest.getUsername() + " with email " + userRequest.getEmail() + " Already exists");


        }


    }

    @Override
    public Boolean removeUser(Long userid) {

        User get_user = userRepository.findById(userid).orElse(null);

        if(get_user != null) {
            try {
                List<Role> roles = get_user.getRoles();
                roles.forEach(role -> {
                    List<User> users = role.getUsers();
                    users.remove(get_user);
                    role.setUsers(users);

                });
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

            if(userRequest.getEmail() != null)
                user.setEmail(userRequest.getEmail());

            if(userRequest.getFname() != null)
                user.setFname(userRequest.getFname());

            if(userRequest.getRoles() != null) {
                ArrayList<Role> roles = new ArrayList<>();

                userRequest.getRoles().forEach(roleid -> {
                    Role role = roleService.getRole(roleid);

                    if(role != null) {
                        List<User> users = role.getUsers();
                        users.add(user);
                        role.setUsers(users);

                    }


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

    @Override
    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new ResourceNotFound("No user " + username + " found");
        else
            return user;
    }

    @Override
    public UserResponse register(UserRequest userRequest) {

        Role role = roleService.getRoleByName("USER");
        ArrayList<Long> roles = new ArrayList<>();
        roles.add(role.getId());
        userRequest.setRoles(roles);
        UserResponse userResponse = addUser(userRequest);


        return userResponse;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else
            log.info("User found: " + username);
        log.info("roles " + user.getRoles().toString());
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        org.springframework.security.core.userdetails.User userS = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);

        return userS;
    }
}
