package com.ITworks.backend.controller;

import com.ITworks.backend.dto.Job.*;
import com.ITworks.backend.dto.ResponseModel;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.service.JobService;
import com.ITworks.backend.mapper.JobMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        try {
            List<JobDTO> jobs = jobService.findAllJobs();
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
            JobDTO job = jobService.findJobById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    job,
                    "Job retrieved successfully"
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

    @PostMapping
    public ResponseEntity<?> createJob(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        try {
            JobDTO job = jobService.createJob(jobCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(
                    201,
                    job,
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
            Job existingJob = jobMapper.toEntity(jobService.findJobById(id));
            
            Job updatedJob = jobMapper.updateJobFromDTO(jobUpdateDTO, existingJob);
            JobDTO updatedJobDTO = jobService.updateJob(id, updatedJob);
            

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    updatedJobDTO,
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
            jobService.deleteJob(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    null,
                    "Job deleted successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                    400,
                    null,
                    e.getMessage()
            ));
        } catch (Exception e) {
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
            List<JobDTO> jobs = jobService.findJobsByEmployerId(employerId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobs,
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
            List<JobDTO> jobs;
            if (employerId != null) {
                jobs = jobService.findJobsByEmployerIdAndStatus(employerId, status);
            } else {
                jobs = jobService.findJobsByStatus(status);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobs,
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