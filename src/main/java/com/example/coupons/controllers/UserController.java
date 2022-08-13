package com.example.coupons.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.coupons.model.Role;
import com.example.coupons.model.User;
import com.example.coupons.request.UserRequest;
import com.example.coupons.response.UserResponse;
import com.example.coupons.service.UserService;
import com.example.coupons.utils.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@Data
@RequestMapping(value = "/api", produces ="application/json" )
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ResponseHandler responseHandler;

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/token/refresh")
    void refreshToken(@CookieValue("refresh_token") String refresh, HttpServletRequest request, HttpServletResponse response) throws IOException {


        if(refresh != null) {
            try {
                String token = refresh;

                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create().
                        withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);



                response.setHeader("access-token", access_token);


                response.setStatus(OK.value());
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);



            } catch(TokenExpiredException expiredException) {

                response.setStatus(UNAUTHORIZED.value());
                Map<String,String> errors = new HashMap<>();
                errors.put("error", "Refresh Token Expired");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch(JWTVerificationException jwtVerificationException) {
                response.setStatus(UNAUTHORIZED.value());
                Map<String,String> errors = new HashMap<>();
                errors.put("error", "Bad Refresh Token");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch (Exception ex) {

                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message", ex.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }

        }
    }
    @PostMapping("/adduser")
    ResponseEntity<Object> addUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);

        return responseHandler.generateResponse("User has been saved successfully", HttpStatus.OK, userResponse);
    }

    @DeleteMapping("/removeuser/{userid}")
    ResponseEntity<Object> removeUser(@PathVariable Long userid) {
        userService.removeUser(userid);

        return responseHandler.generateResponse("User " + userid + " has been deleted successfully", HttpStatus.OK, "");
    }

    @PostMapping("/updateuser/{userid}")
    ResponseEntity<Object> updateUser(@RequestBody UserRequest userRequest, @PathVariable("userid") Long id) {

        UserResponse userResponse = userService.updateUser(userRequest, id);

        return responseHandler.generateResponse("User " + id + " has been updated successfully", HttpStatus.OK, userResponse);

    }

    @GetMapping("/getuser/{userid}")
    ResponseEntity<Object> getUser(@PathVariable ("userid") Long userid) {
        User user = userService.getUser(userid);
        UserResponse userResponse = new UserResponse(user);
        return responseHandler.generateResponse("User " + userid + " has been retrieved successfully", HttpStatus.OK, userResponse);
    }

    @GetMapping("/getid")
    ResponseEntity<Object> getID(@RequestHeader("Authorization") String authorization ) {
        String token = authorization.substring("Bearer".length()).trim();

            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String username = decodedJWT.getSubject();
            User user = userService.getUser(username);

        Map<String,Long> response = new HashMap<>();
        response.put("userid", user.getId());

        return responseHandler.generateResponse("User ID has been retrieved successfully", HttpStatus.OK, response);
    }

    @GetMapping("/getallusers")
    ResponseEntity<Object> getAllUsers() {
        List<UserResponse> usersResponse = userService.getAllUsers();


        return responseHandler.generateResponse("All Users have been retrieved successfully.", HttpStatus.OK, usersResponse);
    }

    @PostMapping("/addroletouser/{roleid}/{userid}")
    ResponseEntity<Object> addRoletoUser(@PathVariable("roleid") Long roleid, @PathVariable("userid") Long userid) {
        Map<String, String> response = userService.addRoletoUser(roleid,userid);
        return responseHandler.generateResponse("Role " + response.get("role") + " has been added successfully to user " + response.get("username"), HttpStatus.OK, "");

    }

    @PostMapping("/removerolefromuser/{roleid}/{userid}")
    ResponseEntity<Object> removeRoleFromUser(@PathVariable("roleid") Long roleid,@PathVariable("userid") Long userid) {
        Map<String, String> response = userService.removeRoleFromUser(roleid,userid);
        return responseHandler.generateResponse("Role " + response.get("role") + " has been removed successfully from user " + response.get("username"), HttpStatus.OK, "");

    }

    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = userService.register(userRequest);

        return responseHandler.generateResponse("User has been registered successfully", HttpStatus.OK, userResponse);
    }


}
