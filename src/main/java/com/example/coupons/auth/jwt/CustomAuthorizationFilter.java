package com.example.coupons.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.coupons.exceptions.TokenExpired;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {



    private String secret;

    public CustomAuthorizationFilter(String secret) {
        this.secret = secret;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().contains("/login") || request.getServletPath().contains("/register") || request.getServletPath().contains("/api/token/refresh"))
            filterChain.doFilter(request,response);
        else {
            String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                try {
                    String token = authorizationHeader.substring("Bearer".length()).trim();
                    log.info(token);
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(token);

                    String username = decodedJWT.getSubject();
          // Roles
                    String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    if(roles == null) {
                        response.setStatus(UNAUTHORIZED.value());
                        Map<String,String> errors = new HashMap<>();
                        errors.put("error", "Username Not Found");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), errors);
                    }
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });


                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    filterChain.doFilter(request,response);
                } catch(TokenExpiredException expiredException) {

                    response.setStatus(UNAUTHORIZED.value());
                    Map<String,String> errors = new HashMap<>();
                    errors.put("error", "Token Expired");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                } catch(JWTVerificationException jwtVerificationException) {
                    response.setStatus(UNAUTHORIZED.value());
                    Map<String,String> errors = new HashMap<>();
                    errors.put("error", "Bad Token");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }

                catch (Exception ex) {

                    response.setHeader("error", ex.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> errors = new HashMap<>();
                    errors.put("error", ex.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }

            } else {
                filterChain.doFilter(request,response);
            }
        }

    }
}
