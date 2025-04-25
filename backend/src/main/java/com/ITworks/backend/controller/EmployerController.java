package com.ITworks.backend.controller;

import com.ITworks.backend.dto.ResponseModel;

import com.ITworks.backend.dto.Job.JobDTO;
import com.ITworks.backend.dto.Job.JobApplicationStatsDTO;
import com.ITworks.backend.dto.Job.JobCreateDTO;
import com.ITworks.backend.dto.Job.JobUpdateDTO;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.mapper.JobMapper;
import com.ITworks.backend.service.EmployerService;
import com.ITworks.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;
    private final JobService jobService;
    private final JobMapper jobMapper;

    @GetMapping("/my-jobs")
    public ResponseEntity<?> getMyJobs() {
        try {
            List<JobDTO> jobs = employerService.getCurrentEmployerJobs();
            return ResponseEntity.ok(new ResponseModel(
                    200,
                    jobs,
                    "Retrieved employer jobs successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    "",
                    e.getMessage()
            ));
        }
    }

    @GetMapping("/my-jobs/status/{status}")
    public ResponseEntity<?> getMyJobsByStatus(@PathVariable String status) {
        try {
            List<JobDTO> jobs = employerService.getCurrentEmployerJobsByStatus(status);
            return ResponseEntity.ok(new ResponseModel(
                    200,
                    jobs,
                    "Retrieved employer jobs successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    "",
                    e.getMessage()
            ));
        }
    }

    @PostMapping("/my-jobs")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobCreateDTO jobCreateDTO) {
        try {
            JobDTO job = jobService.createJob(jobCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(
                    201,
                    job,
                    "Job created successfully"
            ));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(
                    403,
                    null,
                    e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                    400,
                    null,
                    e.getMessage()
            ));
        }
    }

    // Update a job (only for EMPLOYER and only their jobs)
    @PutMapping("/my-jobs/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
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
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(
                    403,
                    null,
                    e.getMessage()
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

    // Delete a job (only for EMPLOYER and only their jobs)
    @DeleteMapping("/my-jobs/{id}")
    @PreAuthorize("hasAuthority('EMPLOYER')")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    null,
                    "Job deleted successfully"
            ));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(
                    403,
                    null,
                    e.getMessage()
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
    @GetMapping("/my-jobs/stats")
    public ResponseEntity<?> getMyJobsStats() {
        try {
            List<JobApplicationStatsDTO> stats = employerService.getCurrentEmployerJobStats();
            return ResponseEntity.ok(new ResponseModel(
                    200,
                    stats,
                    "Retrieved job application statistics successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    "",
                    e.getMessage()
            ));
        }
    }
}