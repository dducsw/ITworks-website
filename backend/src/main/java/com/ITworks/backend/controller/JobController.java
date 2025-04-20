package com.ITworks.backend.controller;

import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobResponseDTO;
import com.ITworks.backend.dto.Job.JobUpdateDTO;
import com.ITworks.backend.dto.ResponseModel;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        try {
            List<Job> jobs = jobService.findAllJobs();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobs,
                    "Retrieved all jobs successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Integer id) {
        try {
            return jobService.findJobById(id)
                    .map(job -> ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                            200,
                            job,
                            "Job retrieved successfully"
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                            400,
                            null,
                            "Job not found with id: " + id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createJob(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        try {
            Job job = jobService.createJob(jobCreateDTO);
            JobResponseDTO responseDTO = new JobResponseDTO(job);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(
                    201,
                    responseDTO,
                    "Job created successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                    400,
                    null,
                    e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Integer id, 
                                      @Valid @RequestBody JobUpdateDTO jobUpdateDTO) {
        try {
            // Find existing job
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
            
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    responseDTO,
                    "Job updated successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                    400,
                    null,
                    e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) {
        try {
            // Check if job exists
            if (!jobService.findJobById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                        400,
                        null,
                        "Job not found with id: " + id
                ));
            }
            
            // Delete job
            jobService.deleteJob(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    null,
                    "Job deleted successfully"
            ));
        } catch (Exception e) {
            // Handle delete error - e.g., foreign key constraint violations
            if (e.getMessage().contains("foreign key constraint")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                        400,
                        null,
                        "Cannot delete this job because candidates have already applied"
                ));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    "Error deleting job: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<?> getJobsByEmployer(@PathVariable Integer employerId) {
        try {
            List<Job> jobs = jobService.findJobsByEmployerId(employerId);
            List<JobResponseDTO> jobDTOs = jobs.stream()
                .map(job -> new JobResponseDTO(job))
                .collect(Collectors.toList());
                
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobDTOs,
                    "Employer jobs retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getJobsByStatus(
            @PathVariable String status,
            @RequestParam(required = false) Integer employerId) {
        
        try {
            List<Job> jobs;
            if (employerId != null) {
                // Get jobs by employerId and status
                jobs = jobService.findJobsByEmployerIdAndStatus(employerId, status);
            } else {
                // Get all jobs by status
                jobs = jobService.findJobsByStatus(status);
            }
            
            List<JobResponseDTO> jobDTOs = jobs.stream()
                .map(job -> new JobResponseDTO(job))
                .collect(Collectors.toList());
                
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobDTOs,
                    "Jobs with status '" + status + "' retrieved successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }
}