package com.ITworks.backend.service;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import com.ITworks.backend.enums.TokenType; // Adjust the package path if necessary

import io.jsonwebtoken.Claims;

public interface IJwtService {
    String generateAccessToken(Map<String, Object> extraClaims, String email);
    boolean isAccessToken(String token);
    String extractEmail(String token, TokenType tokenType);
    String extractUsername(String token, TokenType tokenType);
    boolean isTokenExpired(String token, TokenType tokenType);
    boolean isValidToken(String token, UserDetails userDetails, TokenType tokenType);
    Claims extractAllClaims(String token, TokenType tokenType);

}

