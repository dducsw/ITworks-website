package com.ITworks.backend.controller;

import com.ITworks.backend.dto.Job.*;
import com.ITworks.backend.dto.ResponseModel;
import com.ITworks.backend.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

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