package com.ITworks.backend.service;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Candidate;

// import com.ITworks.backend.entity.Job;
// import com.ITworks.backend.entity.WorkExperience;


import java.util.List;
import java.util.Optional;

public interface CandidateService {
    // Basic CRUD operations
    List<Candidate> findAllCandidates();
    Optional<Candidate> findCandidateById(Integer id);
    
    
    // Application related operations
    Apply applyForJob(Integer candidateId, Integer jobId, String coverLetter, String uploadCv);
    void cancelApplication(Integer candidateId, Integer jobId);

    // List<Apply> getCandidateApplications(Integer candidateId);
    
    
    // // Work experience related operations
    // List<WorkExperience> getCandidateWorkExperiences(Integer candidateId);
    
    // Authentication related
    Optional<Candidate> findByUsername(String username);
    Optional<Candidate> findByEmail(String email);
}
