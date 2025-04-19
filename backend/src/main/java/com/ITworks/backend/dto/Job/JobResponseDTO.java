package com.ITworks.backend.dto.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ITworks.backend.entity.Job;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JobResponseDTO {
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
    
    // Constructor được giữ nguyên vì chứa logic đặc biệt
    public JobResponseDTO(Job job) {
        this.jobId = job.getJobId();
        this.jobName = job.getJobName();
        this.jobType = job.getJobType();
        this.contractType = job.getContractType();
        this.level = job.getLevel();
        this.quantity = job.getQuantity();
        this.salaryFrom = job.getSalaryFrom();
        this.salaryTo = job.getSalaryTo();
        this.requireExpYear = job.getRequireExpYear();
        this.location = job.getLocation();
        this.jobDescription = job.getJobDescription();
        this.postDate = job.getPostDate();
        this.expireDate = job.getExpireDate();
        this.jobStatus = job.getJobStatus();
        this.employerId = job.getEmployerId();
        this.taxNumber = job.getTaxNumber();
        
        // Get company name if available
        if (job.getCompany() != null) {
            this.companyName = job.getCompany().getCompanyName();
        }
    }
}