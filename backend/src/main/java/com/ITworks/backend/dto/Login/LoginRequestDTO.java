package com.ITworks.backend.dto.Login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Username không được để trống")
    private String username;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    
    @NotNull(message = "Loại người dùng không được để trống")
    private UserType userType;
    
    public enum UserType {
        CANDIDATE, 
        EMPLOYER
    }
}