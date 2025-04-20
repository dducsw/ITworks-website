package com.ITworks.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseModel {
    public int httpCode;
    public Object data;
    public String message;

    public ResponseModel(int httpCode, Object data, String message) {
        this.httpCode = httpCode;
        this.data = data;
        this.message = message;
    }
}