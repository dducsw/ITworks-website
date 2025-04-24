package com.ITworks.backend.dto.Job;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class JobUpdateDTO {
    @Size(max = 100, message = "Tên công việc không được vượt quá 100 ký tự")
    private String jobName;
    
    @Size(max = 50, message = "Loại công việc không được vượt quá 50 ký tự")
    private String jobType;
    
    @Size(max = 50, message = "Loại hợp đồng không được vượt quá 50 ký tự")
    private String contractType;
    
    @Size(max = 50, message = "Cấp bậc không được vượt quá 50 ký tự")
    private String level;
    
    @Min(value = 1, message = "Số lượng tuyển dụng phải lớn hơn 0")
    private Integer quantity;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Mức lương không được âm")
    private BigDecimal salaryFrom;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Mức lương tối đa không được âm")
    private BigDecimal salaryTo;
    
    @PositiveOrZero(message = "Số năm kinh nghiệm yêu cầu không được âm")
    private Integer requireExpYear;
    
    @Size(max = 100, message = "Địa điểm làm việc không được vượt quá 100 ký tự")
    private String location;
    
    private String jobDescription;

    @Future(message = "Ngày hết hạn phải sau thời điểm hiện tại")
    private LocalDateTime expireDate;
    
    @Pattern(regexp = "^(Đang mở|Đã đóng|Đã hết hạn)$", 
            message = "Trạng thái công việc phải là một trong các giá trị: Đang mở, Đã đóng, Đã hết hạn")
    private String jobStatus;
    
    /**
     * Custom validation to ensure salaryTo is greater than or equal to salaryFrom
     * This would typically be implemented with a custom validator annotation
     * but for simplicity, you can validate this in your service layer
     */
    public boolean isValidSalaryRange() {
        if (salaryFrom != null && salaryTo != null) {
            return salaryTo.compareTo(salaryFrom) >= 0;
        }
        return true;
    }
}