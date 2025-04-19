package com.ITworks.backend.dto.Job;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    
    // Getters and Setters
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(BigDecimal salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public BigDecimal getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(BigDecimal salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Integer getRequireExpYear() {
        return requireExpYear;
    }

    public void setRequireExpYear(Integer requireExpYear) {
        this.requireExpYear = requireExpYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}