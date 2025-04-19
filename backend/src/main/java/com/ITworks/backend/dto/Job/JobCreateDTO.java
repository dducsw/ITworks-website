package com.ITworks.backend.dto.Job;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobCreateDTO {
    @NotBlank(message = "Tên công việc không được để trống")
    private String jobName;
    
    @NotBlank(message = "Loại công việc không được để trống")
    private String jobType;
    
    @NotBlank(message = "Loại hợp đồng không được để trống")
    private String contractType;
    
    @NotBlank(message = "Cấp bậc không được để trống")
    private String level;
    
    @NotNull(message = "Số lượng tuyển dụng không được để trống")
    @Min(value = 1, message = "Số lượng tuyển dụng phải lớn hơn 0")
    private Integer quantity;
    
    @NotNull(message = "Mức lương tối thiểu không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Mức lương không được âm")
    private BigDecimal salaryFrom;
    
    @NotNull(message = "Mức lương tối đa không được để trống")
    private BigDecimal salaryTo;
    
    private Integer requireExpYear;

    public Integer getRequireExpYear() {
        return requireExpYear;
    }

    public void setRequireExpYear(Integer requireExpYear) {
        this.requireExpYear = requireExpYear;
    }
    
    @NotBlank(message = "Địa điểm làm việc không được để trống")
    private String location;
    
    @NotBlank(message = "Mô tả công việc không được để trống")
    private String jobDescription;
    
    @NotNull(message = "Ngày hết hạn không được để trống")
    @Future(message = "Ngày hết hạn phải sau thời điểm hiện tại")
    private LocalDateTime expireDate;
    
    @NotBlank(message = "Trạng thái công việc không được để trống")
    private String jobStatus;
    
    @NotNull(message = "ID nhà tuyển dụng không được để trống")
    private Integer employerId;
    
    @NotBlank(message = "Mã số thuế không được để trống")
    @Pattern(regexp = "^[0-9]{10}|[0-9]{13}$", message = "Mã số thuế phải có 10 hoặc 13 chữ số")
    private String taxNumber;
    
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
}