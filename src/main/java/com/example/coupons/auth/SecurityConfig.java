package com.example.coupons.auth;


import com.example.coupons.auth.jwt.CustomAuthenticationFilter;
import com.example.coupons.auth.jwt.CustomAuthorizationFilter;
import com.example.coupons.auth.jwt.JwtUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.hibernate.cfg.AvailableSettings.USER;


@Configuration
@Slf4j
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
         log.info("inside configure security");
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
                                        .antMatchers("/register", "/login", "/api/**").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .and()
                                        .addFilter(new CustomAuthenticationFilter(authenticationManager))
                                        .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                                        .exceptionHandling()
                                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).httpBasic(Customizer.withDefaults());

                return http.build();
    }

    }

