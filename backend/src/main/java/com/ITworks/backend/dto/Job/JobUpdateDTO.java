package com.ITworks.backend.dto.Job;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class JobUpdateDTO {
    private String jobName;
    private String jobType;
    private String contractType;
    private String level;
    
    @Min(value = 1, message = "Số lượng tuyển dụng phải lớn hơn 0")
    private Integer quantity;
    
    @Min(value = 0, message = "Mức lương không được âm")
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private Integer requireExpYear;
    private String location;
    private String jobDescription;

    @Future(message = "Ngày hết hạn phải sau thời điểm hiện tại")
    private LocalDateTime expireDate;
    
    private String jobStatus;
}