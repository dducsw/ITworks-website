package com.ITworks.backend.dto.Job;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobSearchDTO {
    private String action = "Get"; // Get hoặc Search
    private BigDecimal salaryFrom = BigDecimal.ZERO;
    private BigDecimal salaryTo = BigDecimal.ZERO;
    private LocalDateTime postDate;
    private String jcName;
    private String sortOrder = "DESC"; // DESC hoặc ASC
    private Boolean filter = false; // Lọc công việc đang mở
}
