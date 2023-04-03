package com.example.sportsbook_application_backend.config;

import com.example.sportsbook_application_backend.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/user/registration","/user/login","/error").permitAll();
        http.authorizeHttpRequests().requestMatchers("/user/**").hasRole(Role.USER.toString());
        http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole(Role.ADMIN.toString());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//spring will create new session for each request
        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}