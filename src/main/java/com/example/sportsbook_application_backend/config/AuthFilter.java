package com.example.sportsbook_application_backend.config;

import com.example.sportsbook_application_backend.exception.FieldException;
import com.example.sportsbook_application_backend.exception.WrongCredentialsException;
import com.example.sportsbook_application_backend.model.dto.user.UserLoginDTO;
import com.example.sportsbook_application_backend.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Configuration
public class AuthFilter {
    private final AuthenticationManager manager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    public AuthFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Bean(name = "CustomAuthFilter")
    public AuthenticationFilter authFilter() {
        AuthenticationConverter authenticationConverter = this::authConverter;
        AuthenticationFilter filter = new AuthenticationFilter(manager, authenticationConverter);
        filter.setRequestMatcher(AuthFilter::matches);
        filter.setSuccessHandler(this::successHandler);
        filter.setFailureHandler(this::failureHandler);
        return filter;
    }


    private void successHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            User principal = (User) authentication.getPrincipal();
            httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateToken(userService.getUserByUsername(principal.getUsername())));
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void failureHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        if(e instanceof FieldException)
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        else
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
    }

    private static boolean matches(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getMethod().equals("POST") && httpServletRequest.getRequestURI().equals("/user/login");
    }

    private Authentication authConverter(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            Gson gson = new Gson();
            UserLoginDTO user = gson.fromJson(requestWrapper.getReader(), UserLoginDTO.class);
            userService.validateLoginFields(user);
            userService.checkUserCredentials(user);
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        } catch (IOException e) {
            throw new WrongCredentialsException("Wrong credentials!");
        }

    }
}