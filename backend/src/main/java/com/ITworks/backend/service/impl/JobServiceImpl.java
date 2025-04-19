package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Job;
import com.ITworks.backend.repository.JobRepository;
import com.ITworks.backend.repository.EmployerRepository;
import com.ITworks.backend.repository.CompanyRepository;
// import com.ITworks.backend.repository.ApplyRepository;


import com.ITworks.backend.service.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Override
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> findJobById(Integer id) {
        return jobRepository.findById(id);
    }

    @Override
    @Transactional
    public Job saveJob(Job job) {
        // Validate job data
        validateJobData(job);
        
        // Set current datetime as post date if not set
        if (job.getPostDate() == null) {
            job.setPostDate(LocalDateTime.now());
        }
        
        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public Job updateJob(Integer id, Job jobDetails) {
        return jobRepository.findById(id)
            .map(existingJob -> {
                // Update only non-null fields from jobDetails
                if (jobDetails.getJobType() != null) {
                    existingJob.setJobType(jobDetails.getJobType());
                }
                if (jobDetails.getContractType() != null) {
                    existingJob.setContractType(jobDetails.getContractType());
                }
                if (jobDetails.getLevel() != null) {
                    existingJob.setLevel(jobDetails.getLevel());
                }
                if (jobDetails.getQuantity() != null) {
                    validatePositiveQuantity(jobDetails.getQuantity());
                    existingJob.setQuantity(jobDetails.getQuantity());
                }
                if (jobDetails.getSalaryFrom() != null) {
                    validateNonNegativeSalary(jobDetails.getSalaryFrom());
                    existingJob.setSalaryFrom(jobDetails.getSalaryFrom());
                }
                if (jobDetails.getSalaryTo() != null) {
                    BigDecimal fromSalary = jobDetails.getSalaryFrom() != null ? 
                                         jobDetails.getSalaryFrom() : existingJob.getSalaryFrom();
                    validateSalaryRange(fromSalary, jobDetails.getSalaryTo());
                    existingJob.setSalaryTo(jobDetails.getSalaryTo());
                }
                if (jobDetails.getRequireExpYear() != null) {
                    validateNonNegativeExperience(jobDetails.getRequireExpYear());
                    existingJob.setRequireExpYear(jobDetails.getRequireExpYear());
                }
                if (jobDetails.getLocation() != null) {
                    existingJob.setLocation(jobDetails.getLocation());
                }
                if (jobDetails.getJobDescription() != null) {
                    existingJob.setJobDescription(jobDetails.getJobDescription());
                }
                if (jobDetails.getJobName() != null) {
                    existingJob.setJobName(jobDetails.getJobName());
                }
                if (jobDetails.getExpireDate() != null) {
                    validateFutureDate(jobDetails.getExpireDate(), existingJob.getPostDate());
                    existingJob.setExpireDate(jobDetails.getExpireDate());
                }
                if (jobDetails.getJobStatus() != null) {
                    existingJob.setJobStatus(jobDetails.getJobStatus());
                }
                
                return jobRepository.save(existingJob);
            })
            .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteJob(Integer id) {
        Job job = jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
        
        jobRepository.delete(job);
    }

    @Override
    public List<Job> findJobsByEmployerId(Integer employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    @Override
    public List<Job> findJobsByCompany(Integer taxNumber) {
        return jobRepository.findByTaxNumber(taxNumber.toString());
    }

    @Override
    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        // Using the available method in the repository
        return jobRepository.findByJobNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Job> findActiveJobs() {
        return jobRepository.findByJobStatusAndExpireDateAfter("Active", LocalDateTime.now());
    }

    @Override
    public List<Job> findJobsByCategory(String categoryName) {
        return jobRepository.findByCategory(categoryName);
    }
    
    // Private validation methods
    private void validateJobData(Job job) {
        // Required fields
        if (job.getJobType() == null || job.getJobType().trim().isEmpty()) {
            throw new IllegalArgumentException("Job type cannot be empty");
        }
        
        if (job.getContractType() == null || job.getContractType().trim().isEmpty()) {
            throw new IllegalArgumentException("Contract type cannot be empty");
        }
        
        if (job.getLevel() == null || job.getLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("Level cannot be empty");
        }
        
        validatePositiveQuantity(job.getQuantity());
        validateNonNegativeSalary(job.getSalaryFrom());
        validateSalaryRange(job.getSalaryFrom(), job.getSalaryTo());
        
        if (job.getRequireExpYear() != null) {
            validateNonNegativeExperience(job.getRequireExpYear());
        }
        
        if (job.getLocation() == null || job.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        
        if (job.getJobDescription() == null || job.getJobDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Job description cannot be empty");
        }
        
        if (job.getJobName() == null || job.getJobName().trim().isEmpty()) {
            throw new IllegalArgumentException("Job name cannot be empty");
        }
        
        if (job.getExpireDate() == null) {
            throw new IllegalArgumentException("Expire date cannot be empty");
        }
        
        validateFutureDate(job.getExpireDate(), LocalDateTime.now());
        
        if (job.getJobStatus() == null || job.getJobStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Job status cannot be empty");
        }
        
        // Check employer and company
        Integer employerId = job.getEmployerId();
        if (employerId == null || !employerRepository.existsById(employerId)) {
            throw new IllegalArgumentException("Invalid employer ID");
        }
        
        String taxNumber = job.getTaxNumber();
        if (taxNumber == null || !companyRepository.existsById(taxNumber)) {
            throw new IllegalArgumentException("Invalid tax number");
        }
        
        // Validate tax number format
        if (!Pattern.matches("^[0-9].*$", taxNumber) || (taxNumber.length() != 10 && taxNumber.length() != 13)) {
            throw new IllegalArgumentException("Tax number must start with a digit and be 10 or 13 characters long");
        }
        
        // // Check employer belongs to the company
        // if (!employerRepository.existsByEmployerIdAndTaxNumber(employerId, taxNumber)) {
        //     throw new IllegalArgumentException("Employer does not belong to this company");
        // }
    }
    
    private void validatePositiveQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }
    
    private void validateNonNegativeSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }
    
    private void validateSalaryRange(BigDecimal from, BigDecimal to) {
        if (to == null) {
            throw new IllegalArgumentException("Maximum salary cannot be empty");
        }
        
        if (from != null && to.compareTo(from) < 0) {
            throw new IllegalArgumentException("Maximum salary must be greater than or equal to minimum salary");
        }
    }
    
    private void validateNonNegativeExperience(Integer years) {
        if (years < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative");
        }
    }
    
    private void validateFutureDate(LocalDateTime date, LocalDateTime referenceDate) {
        if (date.isBefore(referenceDate) || date.isEqual(referenceDate)) {
            throw new IllegalArgumentException("Expire date must be after post date");
        }
    }
}
