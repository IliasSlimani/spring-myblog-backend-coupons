package com.example.coupons.auth;


import com.example.coupons.auth.jwt.CustomAuthenticationFilter;
import com.example.coupons.auth.jwt.CustomAuthorizationFilter;
import com.example.coupons.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserServiceImpl userService;


    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        super.configure(auth);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable();

                http.authorizeRequests().antMatchers("/register", "/login", "/api/categories", "/api/category/**", "/api/getalldeals", "/api/getdeal/**", "/api/coupon/**", "/api/coupons", "/token/refresh").permitAll()
                .antMatchers("/api/addDealtoCoupon/**", "/api/removeDealFromCoupon/**", "/api/adddeal","/api/addcoupontocategory/**","/api/removecouponfromcategory/**","/api/deletecategory/**","/api/updatecategory/**","/api/addcategory","/api/updatecoupon/**","/api/addcoupon","/api/deletecoupon/**","/api/adduser","/api/removeuser/**","/api/updateuser/**","/api/addrole","/api/removerole/**","/api/updaterole/**","/api/addroletouser/**","/api/removerolefromuser/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean()))

                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

                http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

