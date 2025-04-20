package com.ITworks.backend.controller;

import com.ITworks.backend.dto.ResponseModel;
import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Employer;
import com.ITworks.backend.entity.Job;
import com.ITworks.backend.service.EmployerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    // Get all employers
    @GetMapping
    public ResponseEntity<?> getAllEmployers() {
        try {
            List<Employer> employers = employerService.findAllEmployers();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    employers,
                    "Retrieved all employers successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }

    // Get employer by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployerById(@PathVariable Integer id) {
        try {
            return employerService.findEmployerById(id)
                    .map(employer -> ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                            200,
                            employer,
                            "Employer retrieved successfully"
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                            400,
                            null,
                            "Employer not found with id: " + id
                    )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                    500,
                    null,
                    e.getMessage()
            ));
        }
    }

    // Get all jobs for an employer
    @GetMapping("/{employerId}/jobs")
    public ResponseEntity<?> getEmployerJobs(@PathVariable Integer employerId) {
        try {
            List<Job> jobs = employerService.getEmployerJobs(employerId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobs,
                    "Retrieved jobs successfully"
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

    // Get jobs by status for an employer
    @GetMapping("/{employerId}/jobs/status/{status}")
    public ResponseEntity<?> getEmployerJobsByStatus(
            @PathVariable Integer employerId,
            @PathVariable String status) {
        try {
            List<Job> jobs = employerService.getEmployerJobsByStatus(employerId, status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    jobs,
                    "Retrieved jobs with status '" + status + "' successfully"
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

    // Get applications for a specific job
    @GetMapping("/{employerId}/jobs/{jobId}/applications")
    public ResponseEntity<?> getJobApplications(
            @PathVariable Integer employerId,
            @PathVariable Integer jobId) {
        try {
            List<Apply> applications = employerService.getJobApplications(employerId, jobId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                    200,
                    applications,
                    "Retrieved job applications successfully"
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

    // Update application status
    @PutMapping("/{employerId}/applications/{jobId}/candidates/{candidateId}")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Integer employerId,
            @PathVariable Integer jobId,
            @PathVariable Integer candidateId,
            @RequestBody Map<String, String> statusUpdate) {
        
        String newStatus = statusUpdate.get("status");
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(
                    400,
                    null,
                    "Status cannot be empty"
            ));
        }
        
        try {
            boolean updated = employerService.updateApplicationStatus(candidateId, jobId, newStatus);
            if (updated) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(
                        200,
                        null,
                        "Application status updated successfully"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel(
                        500,
                        null,
                        "Failed to update application status"
                ));
            }
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
}