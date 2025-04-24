package com.ITworks.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ITworks.backend.service.IJwtService; 
import com.ITworks.backend.enums.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authenticationHeader.replace("Bearer ", "");

        try {
            if (!jwtService.isAccessToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            email = jwtService.extractUsername(jwt, TokenType.ACCESS);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                boolean isTokenValid = jwtService.isValidToken(jwt, userDetails, TokenType.ACCESS);

                if (isTokenValid) {
                    // Lấy thêm thông tin từ claims trong JWT token
                    Claims claims = jwtService.extractAllClaims(jwt, TokenType.ACCESS);
                    String userType = claims.get("userType", String.class);
                    
                    // Tạo danh sách quyền kết hợp từ UserDetails và userType từ JWT
                    Collection<? extends GrantedAuthority> existingAuthorities = userDetails.getAuthorities();
                    List<GrantedAuthority> authorities = new ArrayList<>(existingAuthorities);
                    
                    // Thêm userType vào danh sách quyền
                    if (userType != null && !userType.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(userType));
                    }
                    
                    // Sửa đoạn code tạo UsernamePasswordAuthenticationToken
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("text/plain");
            response.getWriter().write("Token has expired");
            return;
        }

        filterChain.doFilter(request, response);
    }
}