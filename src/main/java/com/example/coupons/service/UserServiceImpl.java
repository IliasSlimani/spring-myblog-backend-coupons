package com.example.coupons.service;

import com.example.coupons.exceptions.DuplicateResource;
import com.example.coupons.exceptions.InternalError;
import com.example.coupons.exceptions.ResourceNotFound;
import com.example.coupons.model.User;
import com.example.coupons.model.User;
import com.example.coupons.repository.UserRepository;
import com.example.coupons.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public User addUser(User user) {
        User get_user = userRepository.findByUsername(user.getUsername());
        if(get_user == null) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User user_ = userRepository.save(user);
                return user_;
            } catch(Exception ex) {
                throw new InternalError("Internal Problem. Check with your admin");
            }
        } else {
            throw new DuplicateResource("User " + user.getUsername() + " Already exists");
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
    public User updateUser(User user, Long id) {

        User user_f = userRepository.findById(id).orElse(null);

        log.info(user.getUsername());
        log.info(user_f.getUsername());

        if(user_f == null) {
            throw new ResourceNotFound("No User " + id + " found");

        }
        else if(user.getUsername() != null && !user.getUsername().equals(user_f.getUsername()))
            throw new DuplicateResource("Username can't be updated");
        else {
            user.setId(id);
            user.setUsername(user_f.getUsername());
            if(user.getPassword() != null)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            else
                user.setPassword(user_f.getPassword());
            User user_n = userRepository.save(user);
            return user_n;
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
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }
}
