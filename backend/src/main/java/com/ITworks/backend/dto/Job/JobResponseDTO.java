package com.ITworks.backend.dto.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.ITworks.backend.entity.Job; // Ensure this is the correct package for the Job class

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
    
    
    // Constructor
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
    
    // Getters and Setters
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

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

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
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

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}