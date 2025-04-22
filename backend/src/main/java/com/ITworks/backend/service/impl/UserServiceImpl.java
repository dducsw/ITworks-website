package com.ITworks.backend.service.impl;

import com.ITworks.backend.dto.Login.*;
import com.ITworks.backend.entity.Candidate;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.User;
import com.ITworks.backend.repositories.CandidateRepository;
import com.ITworks.backend.repositories.EmployerRepository;
import com.ITworks.backend.repositories.UserRepository;
import com.ITworks.backend.service.IJwtService;
import com.ITworks.backend.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final EmployerRepository employerRepository;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user;
        
        try {
            // Tìm user trước
            user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + loginRequest.getUsername()));
                    
            // Log thông tin debug
            System.out.println("Found user with ID: " + user.getId());
            System.out.println("User password in DB: " + user.getPassword());
            System.out.println("Password from request: " + loginRequest.getPassword());
            
            try {
                // Xác thực riêng để bắt lỗi cụ thể
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), 
                                loginRequest.getPassword())
                );
            } catch (Exception authEx) {
                // Log chi tiết về lỗi xác thực
                System.out.println("Authentication error: " + authEx.getClass().getName() + " - " + authEx.getMessage());
                throw new RuntimeException("Authentication failed: " + authEx.getMessage(), authEx);
            }
        } catch (Exception ex) {
            System.out.println("Login error: " + ex.getClass().getName() + " - " + ex.getMessage());
            throw new RuntimeException("Invalid username or password: " + ex.getMessage());
        }

        // Verify user type
        String userType = null;
        Integer typeId = null;
        
        if (loginRequest.getUserType() == LoginRequestDTO.UserType.CANDIDATE) {
            Optional<Candidate> candidate = candidateRepository.findById(user.getId());
            if (candidate.isEmpty()) {
                throw new RuntimeException("This account is not a candidate");
            }
            userType = "CANDIDATE";
            typeId = user.getId();
        } else if (loginRequest.getUserType() == LoginRequestDTO.UserType.EMPLOYER) {
            Optional<Employer> employer = employerRepository.findById(user.getId());
            if (employer.isEmpty()) {
                throw new RuntimeException("This account is not an employer");
            }
            userType = "EMPLOYER";
            typeId = user.getId();
        }
        
        // Create claims for JWT
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("userType", userType);
        
        // Generate JWT token
        String accessToken = jwtService.generateAccessToken(extraClaims, user.getUsername());
        
        // Create and return LoginResponseDTO with the correct fields
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken("");
        response.setTypeId(typeId);
        
        return response;
    }
    
    
    // Helper methods
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}