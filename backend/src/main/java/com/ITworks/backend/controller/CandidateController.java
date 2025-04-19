package com.ITworks.backend.controller;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Candidate;


import com.ITworks.backend.service.CandidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    
    @Autowired
    private CandidateService candidateService;
    
    // Basic CRUD operations
    
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.findAllCandidates());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Integer id) {
        return candidateService.findCandidateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Application related endpoints
    
    @PostMapping("/{candidateId}/applications")
    public ResponseEntity<Apply> applyForJob(
            @PathVariable Integer candidateId,
            @RequestParam Integer jobId,
            @RequestParam(required = false) String coverLetter,
            @RequestParam(required = false) String uploadCv) {
        try {
            Apply application = candidateService.applyForJob(candidateId, jobId, coverLetter, uploadCv);
            return ResponseEntity.status(HttpStatus.CREATED).body(application);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    
    @DeleteMapping("/{candidateId}/applications/{jobId}")
    public ResponseEntity<Void> cancelApplication(
            @PathVariable Integer candidateId,
            @PathVariable Integer jobId) {
        try {
            candidateService.cancelApplication(candidateId, jobId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{candidateId}/applications")
    public ResponseEntity<List<Apply>> getCandidateApplications(@PathVariable Integer candidateId) {
        return ResponseEntity.ok(candidateService.getCandidateApplications(candidateId));
    }
    
    // @GetMapping("/{candidateId}/applications/active")
    // public ResponseEntity<List<Apply>> getCandidateActiveApplications(@PathVariable Integer candidateId) {
    //     return ResponseEntity.ok(candidateService.getCandidateActiveApplications(candidateId));
    // }
    
    // Job related endpoints
    
    
}
