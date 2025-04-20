package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Job;
import com.ITworks.backend.mapper.JobMapper;
import com.ITworks.backend.repositories.CompanyRepository;
import com.ITworks.backend.repositories.EmployerRepository;
import com.ITworks.backend.repositories.JobRepository;
// import com.ITworks.backend.repository.ApplyRepository;
import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobDTO;


import com.ITworks.backend.service.JobService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    
    @Autowired
    private final JobMapper jobMapper;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EmployerRepository employerRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Override
    public List<JobDTO> findAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    public JobDTO findJobById(Integer id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
        return jobMapper.toJobDTO(job);
    }
    @Override
    public List<JobDTO> findJobsByStatus(String status) {
        List<Job> jobs = jobRepository.findByJobStatus(status);
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    public List<JobDTO> findJobsByEmployerIdAndStatus(Integer employerId, String status) {
        List<Job> jobs = jobRepository.findByEmployerIdAndJobStatus(employerId, status);
        return jobMapper.toJobDTOList(jobs);
    }

    @Override
    @Transactional
    public JobDTO saveJob(Job job) {
        // Validate job data
        validateJobData(job);
        
        // Set current datetime as post date if not set
        if (job.getPostDate() == null) {
            job.setPostDate(LocalDateTime.now());
        }
        
        Job saveJob = jobRepository.save(job);
        return jobMapper.toJobDTO(saveJob);
    }
    
    @Override
    @Transactional
    public JobDTO createJob(JobCreateDTO jobCreateDTO) {
        // Convert JobCreateDTO to Job entity
        Job job = new Job();
        job.setJobName(jobCreateDTO.getJobName());
        job.setJobType(jobCreateDTO.getJobType());
        job.setContractType(jobCreateDTO.getContractType());
        job.setLevel(jobCreateDTO.getLevel());
        job.setQuantity(jobCreateDTO.getQuantity());
        job.setSalaryFrom(jobCreateDTO.getSalaryFrom());
        job.setSalaryTo(jobCreateDTO.getSalaryTo());
        job.setRequireExpYear(jobCreateDTO.getRequireExpYear());
        job.setLocation(jobCreateDTO.getLocation());
        job.setJobDescription(jobCreateDTO.getJobDescription());
        job.setExpireDate(jobCreateDTO.getExpireDate());
        job.setJobStatus(jobCreateDTO.getJobStatus());
        job.setEmployerId(jobCreateDTO.getEmployerId());
        job.setTaxNumber(jobCreateDTO.getTaxNumber());

        // Validate job data
        validateJobData(job);

        // Set current datetime as post date
        job.setPostDate(LocalDateTime.now());

        // Save and return the job
        Job newJob = jobRepository.save(job);
        return jobMapper.toJobDTO(newJob);
    }
    @Override
    @Transactional
    public JobDTO updateJob(Integer id, Job jobDetails) {
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
                
                return jobMapper.toJobDTO(jobRepository.save(existingJob));
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
    public List<JobDTO> findJobsByEmployerId(Integer employerId) {
        return jobMapper.toJobDTOList(jobRepository.findByEmployerId(employerId));
    }

    @Override
    public List<JobDTO> findJobsByCompany(String taxNumber) {
        return jobMapper.toJobDTOList(jobRepository.findByTaxNumber(taxNumber));
    }

    @Override
    public List<JobDTO> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        // Using the available method in the repository
        return jobMapper.toJobDTOList(jobRepository.findByJobNameContainingIgnoreCase(keyword));
    }

    @Override
    public List<JobDTO> findJobsByCategory(String categoryName) {
        return jobMapper.toJobDTOList(jobRepository.findByCategory(categoryName));
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
        if (!Pattern.matches("^(\\d{10}|\\d{13})$", taxNumber)) {
            throw new IllegalArgumentException("Tax number must be 10 hoặc 13 chữ số");
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
        if (years != null && years < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative");
        }
    }
    
    private void validateFutureDate(LocalDateTime date, LocalDateTime referenceDate) {
        if (!date.isAfter(referenceDate)) {
            throw new IllegalArgumentException("Expire date must be after post date");
        }
    }
}
