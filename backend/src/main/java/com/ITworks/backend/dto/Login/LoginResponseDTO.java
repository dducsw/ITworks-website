package com.ITworks.backend.dto.Login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String userType;
    private Integer typeId; // candidateId hoặc employerId
}