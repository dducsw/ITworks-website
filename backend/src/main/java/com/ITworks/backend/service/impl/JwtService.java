package com.ITworks.backend.service.impl;

import com.ITworks.backend.service.IJwtService;
import com.ITworks.backend.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {

    @Value("${spring.application.security.jwt.access_token.secret}")
    private String accessTokenSecret;
    @Value("${spring.application.security.jwt.refresh_token.secret}")
    private String refreshTokenSecret;
    @Value("${spring.application.security.jwt.access_token.expiration}")
    private Long jwtTokenExpiration;
    @Value("${spring.application.security.jwt.refresh_token.expiration}")
    private Long jwtRefreshTokenExpiration;

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, String username) {
        // Sử dụng userType làm role nếu có
        String userType = (String) extraClaims.get("userType");
        if (userType != null) {
            extraClaims.put("role", userType);  // Role sẽ giống với userType
        } else {
            extraClaims.put("role", "USER");  // Mặc định là USER
        }
        return buildToken(extraClaims, username, jwtTokenExpiration, getAccessSecretKey());
    }

    @Override
    public boolean isAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getAccessSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails, TokenType tokenType) {
        final String email = extractEmail(token, tokenType);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType));
    }

    @Override
    public boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getSubject, tokenType);
    }

    // Keep extractEmail method for backward compatibility
    @Override
    public String extractEmail(String token, TokenType tokenType) {
        return extractUsername(token, tokenType);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration, Key secretKey) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token, TokenType tokenType) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenType.equals(TokenType.ACCESS) ? getAccessSecretKey() : getRefreshSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getAccessSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessTokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshTokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver, TokenType tokenType) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, Claims::getExpiration, tokenType);
    }
}