package com.ITworks.backend.service;

import com.ITworks.backend.dto.Login.*;
import com.ITworks.backend.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Thêm phương thức đăng nhập
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}