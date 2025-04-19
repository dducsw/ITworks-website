package com.ITworks.backend.controller;

import com.ITworks.backend.entity.Job;
import com.ITworks.backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobResponseDTO;
import com.ITworks.backend.dto.Job.JobUpdateDTO;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.findAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Integer id) {
        return jobService.findJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        Job job = jobService.createJob(jobCreateDTO);
        JobResponseDTO responseDTO = new JobResponseDTO(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Integer id, 
                                                   @Valid @RequestBody JobUpdateDTO jobUpdateDTO) {
        try {
            // Convert DTO to entity
            Job existingJob = jobService.findJobById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " not found"));
            
            // Update only non-null fields
            if (jobUpdateDTO.getJobName() != null) {
                existingJob.setJobName(jobUpdateDTO.getJobName());
            }
            if (jobUpdateDTO.getJobType() != null) {
                existingJob.setJobType(jobUpdateDTO.getJobType());
            }
            if (jobUpdateDTO.getContractType() != null) {
                existingJob.setContractType(jobUpdateDTO.getContractType());
            }
            if (jobUpdateDTO.getLevel() != null) {
                existingJob.setLevel(jobUpdateDTO.getLevel());
            }
            if (jobUpdateDTO.getQuantity() != null) {
                existingJob.setQuantity(jobUpdateDTO.getQuantity());
            }
            if (jobUpdateDTO.getSalaryTo() != null) {
                existingJob.setSalaryTo(jobUpdateDTO.getSalaryTo());
            }
            if (jobUpdateDTO.getRequireExpYear() != null) {
                existingJob.setRequireExpYear(jobUpdateDTO.getRequireExpYear());
            }
            if (jobUpdateDTO.getLocation() != null) {
                existingJob.setLocation(jobUpdateDTO.getLocation());
            }
            if (jobUpdateDTO.getJobDescription() != null) {
                existingJob.setJobDescription(jobUpdateDTO.getJobDescription());
            }
            if (jobUpdateDTO.getExpireDate() != null) {
                existingJob.setExpireDate(jobUpdateDTO.getExpireDate());
            }
            if (jobUpdateDTO.getJobStatus() != null) {
                existingJob.setJobStatus(jobUpdateDTO.getJobStatus());
            }
            
            // Save updated job
            Job updatedJob = jobService.updateJob(id, existingJob);
            JobResponseDTO responseDTO = new JobResponseDTO(updatedJob);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) {
        try {
            // Check if job exists
            if (!jobService.findJobById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // Delete job
            jobService.deleteJob(id);
            return ResponseEntity.ok().body(Map.of("message", "Xóa công việc thành công"));
        } catch (Exception e) {
            // Handle delete error - e.g., foreign key constraint violations
            if (e.getMessage().contains("foreign key constraint")) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Không thể xóa công việc này vì đã có ứng viên ứng tuyển"));
            }
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Lỗi khi xóa công việc: " + e.getMessage()));
        }
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<JobResponseDTO>> getJobsByEmployer(@PathVariable Integer employerId) {
        List<Job> jobs = jobService.findJobsByEmployerId(employerId);
        List<JobResponseDTO> jobDTOs = jobs.stream()
            .map(job -> new JobResponseDTO(job))
            .collect(Collectors.toList());
        return ResponseEntity.ok(jobDTOs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobResponseDTO>> getJobsByStatus(
            @PathVariable String status,
            @RequestParam(required = false) Integer employerId) {
        
        List<Job> jobs;
        if (employerId != null) {
            // Lấy công việc theo employerId và status
            jobs = jobService.findJobsByEmployerIdAndStatus(employerId, status);
        } else {
            // Lấy tất cả công việc theo status
            jobs = jobService.findJobsByStatus(status);
        }
        
        List<JobResponseDTO> jobDTOs = jobs.stream()
            .map(job -> new JobResponseDTO(job))
            .collect(Collectors.toList());
        return ResponseEntity.ok(jobDTOs);
    }
}