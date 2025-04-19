package com.ITworks.backend.service.impl;

import com.ITworks.backend.entity.Apply;
import com.ITworks.backend.entity.Candidate;
import com.ITworks.backend.entity.Job;

// import com.ITworks.backend.entity.WorkExperience;
import com.ITworks.backend.entity.Apply.ApplyId;
import com.ITworks.backend.repository.ApplyRepository;
import com.ITworks.backend.repository.CandidateRepository;
import com.ITworks.backend.repository.JobRepository;

// import com.ITworks.backend.repository.WorkExperienceRepository;
import com.ITworks.backend.service.CandidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private ApplyRepository applyRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    // @Autowired
    // private WorkExperienceRepository workExperienceRepository;
    

    @Override
    public List<Candidate> findAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Optional<Candidate> findCandidateById(Integer id) {
        return candidateRepository.findById(id);
    }

    @Override
    @Transactional
    public Apply applyForJob(Integer candidateId, Integer jobId, String coverLetter, String uploadCv) {
        // Verify candidate exists
        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new IllegalArgumentException("Candidate with ID " + candidateId + " not found"));
        
        // Verify job exists and is active
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Job with ID " + jobId + " not found"));
        
        if (!job.getJobStatus().equals("Active")) {
            throw new IllegalStateException("Job is not active");
        }
        
        if (job.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Job application deadline has passed");
        }
        
        // Check if candidate already applied
        ApplyId applyId = new ApplyId(candidateId, jobId);
        if (applyRepository.existsById(applyId)) {
            throw new IllegalStateException("You have already applied for this job");
        }
        
        // Create and save application
        Apply apply = new Apply();
        apply.setId(applyId);
        apply.setCandidate(candidate);
        apply.setJob(job);
        apply.setCoverLetter(coverLetter);
        apply.setUploadCV(uploadCv);
        apply.setDate(LocalDateTime.now());
        apply.setStatus("Đã nộp");
        
        Apply savedApply = applyRepository.save(apply);
        
        
        return savedApply;
    }

    @Override
    @Transactional
    public void cancelApplication(Integer candidateId, Integer jobId) {
        // Find the application
        ApplyId applyId = new ApplyId(candidateId, jobId);
        Apply application = applyRepository.findById(applyId)
            .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        
        // Check if application can be canceled (not if status is "Đã nhận việc" or "Hoàn thành")
        String status = application.getStatus();
        if (status.equals("Đã nhận việc") || status.equals("Hoàn thành")) {
            throw new IllegalStateException("Cannot cancel application with status: " + status);
        }
        
        // Delete application
        applyRepository.deleteById(applyId);
    }

    // @Override
    public List<Apply> getCandidateApplications(Integer candidateId) {
        return applyRepository.findByCandidateId(candidateId);
    }

    // @Override
    // public List<WorkExperience> getCandidateWorkExperiences(Integer candidateId) {
    //     return workExperienceRepository.findByCandidateId(candidateId);
    // }

    @Override
    public Optional<Candidate> findByUsername(String username) {
        return candidateRepository.findByUserUsername(username);
    }

    @Override
    public Optional<Candidate> findByEmail(String email) {
        return candidateRepository.findByUserEmail(email);
    }
}