package com.ITworks.backend.dto.Job;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobSearchResponseDTO {
    private Integer jobID;
    private String jobName;
    private String companyName;
    private String jd;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private LocalDateTime expireDate;
    private LocalDateTime postDate;
    private String jobStatus;
}
