package com.ITworks.backend.dto.Login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    public String accessToken;
    public String refreshToken;
    private Integer typeId; // candidateId hoặc employerId
}