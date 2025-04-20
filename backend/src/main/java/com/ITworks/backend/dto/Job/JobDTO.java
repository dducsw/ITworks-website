package com.ITworks.backend.dto.Job;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobDTO {
    private Integer jobId;
    private String jobName;
    private String jobType;
    private String contractType;
    private String level;
    private Integer quantity;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private Integer requireExpYear;
    private String location;
    private String jobDescription;
    private LocalDateTime postDate;
    private LocalDateTime expireDate;
    private String jobStatus;
    private Integer employerId;
    private String taxNumber;
    private String companyName;
}
